package com.pos_magistral.app.controladores;

import com.pos_magistral.app.entidades.caja;
import com.pos_magistral.app.entidades.mesa;
import com.pos_magistral.app.entidades.pago;
import com.pos_magistral.app.entidades.pedido;
import com.pos_magistral.app.repositorio.caja_repositorio;
import com.pos_magistral.app.repositorio.mesa_repositorio;
import com.pos_magistral.app.repositorio.pago_repositorio;
import com.pos_magistral.app.repositorio.pedido_repositorio;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

@Controller
public class pago_web {

    private final pago_repositorio pagoRepositorio;
    private final pedido_repositorio pedidoRepositorio;
    private final caja_repositorio cajaRepositorio;
    private final mesa_repositorio mesaRepositorio;

    public pago_web(pago_repositorio pagoRepositorio,
                    pedido_repositorio pedidoRepositorio,
                    caja_repositorio cajaRepositorio,
                    mesa_repositorio mesaRepositorio) {
        this.pagoRepositorio = pagoRepositorio;
        this.pedidoRepositorio = pedidoRepositorio;
        this.cajaRepositorio = cajaRepositorio;
        this.mesaRepositorio = mesaRepositorio;
    }

    // ===================== MESERO =====================

    // ===================== CAJERO =====================

    // Registrar un pago. modo=total cobra todo lo pendiente; modo=dividido cobra solo las
    // unidades que se marquen en los campos pagar_0, pagar_1, ... (uno por cada línea del pedido)
    @PostMapping("/cajero/pedidos/{id}/pagar")
    public String cajeroPagar(@PathVariable("id") String id,
                              @RequestParam(value = "modo", defaultValue = "total") String modo,
                              @RequestParam(value = "metodo", defaultValue = "EFECTIVO") String metodo,
                              @RequestParam(value = "conPropina", required = false) String conPropina,
                              @RequestParam(value = "recibido", defaultValue = "") String recibido,
                              @RequestParam(value = "liberarMesa", required = false) String liberarMesa,
                              @RequestParam Map<String, String> params,
                              HttpSession sesion,
                              RedirectAttributes ra) {

        // Sin caja abierta no se cobra
        Optional<caja> ocaja = cajaRepositorio.findFirstByEstado(caja.ABIERTA);
        if (ocaja.isEmpty()) {
            ra.addFlashAttribute("error", "Abre la caja antes de cobrar.");
            return "redirect:/cajero/caja";
        }
        caja unaCaja = ocaja.get();

        Optional<pedido> op = pedidoRepositorio.findById(id);
        if (op.isEmpty()) {
            ra.addFlashAttribute("error", "El pedido no existe.");
            return "redirect:/cajero/pedidos";
        }
        pedido unPedido = op.get();

        if (!pedido.TOMADO.equals(unPedido.getEstado())) {
            ra.addFlashAttribute("error", "Este pedido ya no está por cobrar.");
            return "redirect:/cajero/pedidos";
        }

        boolean esDomicilio = unPedido.isDomicilio();
        boolean esDividido = "dividido".equals(modo);
        String urlCobro = "/cajero/pedidos/" + id + "/cobrar?modo=" + (esDividido ? "dividido" : "total");

        // Qué unidades se cobran en este pago (los precios son los guardados en el pedido)
        List<pedido.Item> items = unPedido.getItems();
        int[] aPagar = new int[items.size()];
        List<pago.Linea> lineas = new ArrayList<>();
        double subtotal = 0;

        for (int i = 0; i < items.size(); i++) {
            pedido.Item linea = items.get(i);
            int pendiente = linea.getCantidadPendiente();

            int cantidad;
            if (esDividido) {
                cantidad = Math.min(aEntero(params.get("pagar_" + i)), pendiente);
            } else {
                cantidad = pendiente;
            }

            if (cantidad > 0) {
                aPagar[i] = cantidad;
                pago.Linea lineaPago = new pago.Linea(linea.getComidaId(), linea.getNombre(),
                        linea.getPrecioUnitario(), cantidad);
                lineas.add(lineaPago);
                subtotal += lineaPago.getSubtotal();
            }
        }

        if (lineas.isEmpty()) {
            ra.addFlashAttribute("error", "Elige al menos un producto para cobrar.");
            return "redirect:" + urlCobro;
        }

        // Medio de pago
        if (!pago.EFECTIVO.equals(metodo) && !pago.TARJETA.equals(metodo) && !pago.TRANSFERENCIA.equals(metodo)) {
            metodo = pago.EFECTIVO;
        }

        // Propina voluntaria (10% del subtotal, en pesos enteros)
        boolean incluyePropina = conPropina != null;
        double propina = incluyePropina ? Math.round(subtotal * pago.PORCENTAJE_PROPINA) : 0;
        double total = subtotal + propina;

        // Con cuánto pagó el cliente y cuánto se le devuelve
        double recibidoNum;
        double devuelto = 0;

        if (pago.EFECTIVO.equals(metodo)) {
            long escrito = aPesos(recibido);
            // Si el cajero no escribe nada, se toma como pago exacto
            recibidoNum = (escrito == 0) ? total : escrito;

            if (recibidoNum < total) {
                ra.addFlashAttribute("error", "El efectivo recibido ($ " + formato(recibidoNum)
                        + ") no alcanza para el total ($ " + formato(total) + ").");
                return "redirect:" + urlCobro;
            }
            devuelto = recibidoNum - total;
        } else {
            // Tarjeta o transferencia: se cobra exacto, sin vueltos
            recibidoNum = total;
        }

        // Se marcan las unidades como pagadas
        boolean todoPagado = true;
        for (int i = 0; i < items.size(); i++) {
            pedido.Item linea = items.get(i);
            linea.setCantidadPagada(linea.getCantidadPagada() + aPagar[i]);
            if (linea.getCantidadPendiente() > 0) {
                todoPagado = false;
            }
        }

        if (todoPagado) {
            unPedido.setEstado(pedido.PAGADO);
        }
        pedidoRepositorio.save(unPedido);

        // La mesa solo se libera si es un pedido de mesa, se marcó la casilla Y quedó pagado por completo
        boolean mesaLiberada = false;
        if (todoPagado && liberarMesa != null && !esDomicilio && unPedido.getMesaId() != null) {
            Optional<mesa> om = mesaRepositorio.findById(unPedido.getMesaId());
            if (om.isPresent() && unPedido.getId().equals(om.get().getPedidoActualId())) {
                mesa unaMesa = om.get();
                unaMesa.setEstado(mesa.LIBRE);
                unaMesa.setPedidoActualId(null);
                mesaRepositorio.save(unaMesa);
                mesaLiberada = true;
            }
        }

        // Se guarda el registro del pago (en un domicilio mesaId queda vacío y numeroMesa en 0)
        pago nuevoPago = new pago();
        nuevoPago.setPedidoId(unPedido.getId());
        nuevoPago.setNumeroOrden(unPedido.getNumeroOrden());
        nuevoPago.setMesaId(unPedido.getMesaId());
        nuevoPago.setNumeroMesa(unPedido.getNumeroMesa());
        nuevoPago.setNombreCliente(unPedido.getNombreCliente());
        nuevoPago.setCajaId(unaCaja.getId());
        nuevoPago.setCajeroNombre((String) sesion.getAttribute("usuarioNombre"));
        nuevoPago.setMetodo(metodo);
        nuevoPago.setLineas(lineas);
        nuevoPago.setSubtotal(subtotal);
        nuevoPago.setConPropina(incluyePropina);
        nuevoPago.setPropina(propina);
        nuevoPago.setTotal(total);
        nuevoPago.setRecibido(recibidoNum);
        nuevoPago.setDevuelto(devuelto);
        nuevoPago.setLiberoMesa(mesaLiberada);
        nuevoPago.setFecha(LocalDateTime.now());
        nuevoPago = pagoRepositorio.save(nuevoPago);

        StringBuilder mensaje = new StringBuilder("Pago registrado.");
        if (devuelto > 0) {
            mensaje.append(" Vueltos: $ ").append(formato(devuelto)).append(".");
        }
        if (todoPagado) {
            mensaje.append(" El pedido quedó pagado por completo.");
        } else {
            mensaje.append(" Quedan $ ").append(formato(unPedido.getTotalPendiente())).append(" por cobrar.");
        }
        if (mesaLiberada) {
            mensaje.append(" La mesa ").append(unPedido.getNumeroMesa()).append(" quedó libre.");
        }

        ra.addFlashAttribute("mensaje", mensaje.toString());
        return "redirect:/cajero/pagos/" + nuevoPago.getId() + "/factura";
    }

    // Factura para el cliente: productos con precio, subtotal sin propina y total con propina
    @GetMapping("/cajero/pagos/{id}/factura")
    public String cajeroFactura(@PathVariable("id") String id, Model model, RedirectAttributes ra) {

        Optional<pago> opg = pagoRepositorio.findById(id);

        if (opg.isEmpty()) {
            ra.addFlashAttribute("error", "El pago no existe.");
            return "redirect:/cajero/pedidos";
        }

        pago unPago = opg.get();

        double propinaSugerida = Math.round(unPago.getSubtotal() * pago.PORCENTAJE_PROPINA);

        model.addAttribute("titulo", "Factura | MAGISTRAL");
        model.addAttribute("pago", unPago);
        model.addAttribute("porcentajePropina", (int) Math.round(pago.PORCENTAJE_PROPINA * 100));
        model.addAttribute("propinaSugerida", propinaSugerida);
        model.addAttribute("totalConPropina", unPago.getSubtotal() + propinaSugerida);

        // Un pago sin mesa (numeroMesa 0) es de un pedido a domicilio
        model.addAttribute("esDomicilio", unPago.getNumeroMesa() == 0);

        pedidoRepositorio.findById(unPago.getPedidoId()).ifPresent(p -> {

            // Datos de entrega, si es un domicilio
            model.addAttribute("pedidoDelPago", p);

            // Si es cuenta dividida y todavía falta cobrar, se ofrece seguir cobrando
            if (pedido.TOMADO.equals(p.getEstado())) {
                model.addAttribute("pedidoPendienteId", p.getId());
                model.addAttribute("totalPendiente", p.getTotalPendiente());
            }
        });

        return "cajero/factura";
    }

    // Ventas de un día (por defecto hoy): mesas y domicilios, medios de pago y total pagado
    @GetMapping("/cajero/ventas")
    public String cajeroVentas(@RequestParam(value = "fecha", required = false) String fecha, Model model) {

        LocalDate dia = LocalDate.now();
        if (fecha != null && !fecha.isBlank()) {
            try {
                dia = LocalDate.parse(fecha);
            } catch (DateTimeParseException e) {
                dia = LocalDate.now();
            }
        }

        LocalDateTime desde = dia.atStartOfDay();
        LocalDateTime hasta = dia.plusDays(1).atStartOfDay().minusNanos(1);

        List<pago> pagos = pagoRepositorio.findByFechaBetweenOrderByFechaAsc(desde, hasta);

        // Por mesa solo cuentan los pagos de mesa; los domicilios van aparte
        Map<Integer, Double> porMesaTotal = new TreeMap<>();
        Map<Integer, Integer> porMesaCantidad = new TreeMap<>();

        double totalMesas = 0;
        double totalDomicilio = 0;
        int cantMesas = 0;
        int cantDomicilio = 0;

        double totalEfectivo = 0;
        double totalTarjeta = 0;
        double totalTransferencia = 0;
        int cantEfectivo = 0;
        int cantTarjeta = 0;
        int cantTransferencia = 0;
        double totalPropinas = 0;

        for (pago p : pagos) {
            double total = p.getTotal() != null ? p.getTotal() : 0;

            if (p.getNumeroMesa() == 0) {
                totalDomicilio += total;
                cantDomicilio++;
            } else {
                totalMesas += total;
                cantMesas++;
                porMesaTotal.merge(p.getNumeroMesa(), total, Double::sum);
                porMesaCantidad.merge(p.getNumeroMesa(), 1, Integer::sum);
            }

            if (pago.EFECTIVO.equals(p.getMetodo())) {
                totalEfectivo += total;
                cantEfectivo++;
            } else if (pago.TARJETA.equals(p.getMetodo())) {
                totalTarjeta += total;
                cantTarjeta++;
            } else if (pago.TRANSFERENCIA.equals(p.getMetodo())) {
                totalTransferencia += total;
                cantTransferencia++;
            }

            totalPropinas += p.getPropina() != null ? p.getPropina() : 0;
        }

        // Los pagos más recientes primero
        List<pago> recientes = new ArrayList<>(pagos);
        Collections.reverse(recientes);

        model.addAttribute("titulo", "Ventas | MAGISTRAL");
        model.addAttribute("fecha", dia);
        model.addAttribute("fechaTexto", dia.toString());
        model.addAttribute("esHoy", dia.equals(LocalDate.now()));
        model.addAttribute("pagos", recientes);
        model.addAttribute("porMesaTotal", porMesaTotal);
        model.addAttribute("porMesaCantidad", porMesaCantidad);
        model.addAttribute("totalMesas", totalMesas);
        model.addAttribute("cantMesas", cantMesas);
        model.addAttribute("totalDomicilio", totalDomicilio);
        model.addAttribute("cantDomicilio", cantDomicilio);
        model.addAttribute("totalEfectivo", totalEfectivo);
        model.addAttribute("totalTarjeta", totalTarjeta);
        model.addAttribute("totalTransferencia", totalTransferencia);
        model.addAttribute("cantEfectivo", cantEfectivo);
        model.addAttribute("cantTarjeta", cantTarjeta);
        model.addAttribute("cantTransferencia", cantTransferencia);
        model.addAttribute("totalPagado", totalEfectivo + totalTarjeta + totalTransferencia);
        model.addAttribute("totalPropinas", totalPropinas);
        model.addAttribute("cantidadPagos", pagos.size());

        return "cajero/ventas";
    }

    // ===================== ADMIN =====================

    // ===================== CLIENTE =====================

    // Convierte el texto del formulario en un número entre 0 y 99
    private static int aEntero(String valor) {
        if (valor == null || valor.isBlank()) {
            return 0;
        }
        try {
            int n = Integer.parseInt(valor.trim());
            return Math.max(0, Math.min(99, n));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    // Convierte lo que escribe el cajero en pesos enteros: "50.000" o "$ 50000" dan 50000
    private static long aPesos(String valor) {
        if (valor == null) {
            return 0;
        }
        String soloNumeros = valor.replaceAll("[^0-9]", "");
        if (soloNumeros.isEmpty() || soloNumeros.length() > 12) {
            return 0;
        }
        return Long.parseLong(soloNumeros);
    }

    // Formato de pesos con punto de miles: 18900 -> 18.900
    private static String formato(double valor) {
        return String.format(Locale.forLanguageTag("es-CO"), "%,d", Math.round(valor));
    }
}