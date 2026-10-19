package com.pos_magistral.app.controladores;

import com.pos_magistral.app.entidades.caja;
import com.pos_magistral.app.entidades.comida;
import com.pos_magistral.app.entidades.mesa;
import com.pos_magistral.app.entidades.pago;
import com.pos_magistral.app.entidades.pedido;
import com.pos_magistral.app.repositorio.caja_repositorio;
import com.pos_magistral.app.repositorio.comida_repositorio;
import com.pos_magistral.app.repositorio.mesa_repositorio;
import com.pos_magistral.app.repositorio.pedido_repositorio;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class pedido_web {

    private final pedido_repositorio pedidoRepositorio;
    private final mesa_repositorio mesaRepositorio;
    private final comida_repositorio comidaRepositorio;
    private final caja_repositorio cajaRepositorio;

    public pedido_web(pedido_repositorio pedidoRepositorio,
                      mesa_repositorio mesaRepositorio,
                      comida_repositorio comidaRepositorio,
                      caja_repositorio cajaRepositorio) {
        this.pedidoRepositorio = pedidoRepositorio;
        this.mesaRepositorio = mesaRepositorio;
        this.comidaRepositorio = comidaRepositorio;
        this.cajaRepositorio = cajaRepositorio;
    }

    // ===================== MESERO =====================

    // Historial de pedidos de mesa (los de domicilio no aparecen aquí)
    @GetMapping("/mesero/pedidos")
    public String meseroListar(Model model) {

        List<pedido> delSalon = new ArrayList<>();
        for (pedido p : pedidoRepositorio.findAllByOrderByFechaDesc()) {
            if (!p.isDomicilio()) {
                delSalon.add(p);
            }
        }

        model.addAttribute("titulo", "Pedidos | MAGISTRAL");
        model.addAttribute("pedidos", delSalon);
        return "mesero/pedidos";
    }

    // Formulario para tomar el pedido de una mesa
    @GetMapping("/mesero/pedidos/nuevo/{mesaId}")
    public String meseroNuevo(@PathVariable("mesaId") String mesaId, Model model, RedirectAttributes ra) {

        Optional<mesa> om = mesaRepositorio.findById(mesaId);

        if (om.isEmpty()) {
            ra.addFlashAttribute("error", "La mesa no existe.");
            return "redirect:/mesero/mesas";
        }

        mesa unaMesa = om.get();

        // Si la mesa ya tiene un pedido activo, se muestra ese pedido
        if (unaMesa.getPedidoActualId() != null) {
            return "redirect:/mesero/pedidos/" + unaMesa.getPedidoActualId();
        }

        // Por si entran directo por la URL: se asegura que la mesa quede ocupada
        if (!mesa.OCUPADA.equals(unaMesa.getEstado())) {
            unaMesa.setEstado(mesa.OCUPADA);
            mesaRepositorio.save(unaMesa);
        }

        model.addAttribute("titulo", "Tomar pedido | MAGISTRAL");
        model.addAttribute("mesa", unaMesa);
        model.addAttribute("hamburguesas", comidaRepositorio.findByCategoriaAndDisponibleTrue(comida.CAT_HAMBURGUESA));
        model.addAttribute("salchipapas", comidaRepositorio.findByCategoriaAndDisponibleTrue(comida.CAT_SALCHIPAPA));
        model.addAttribute("bebidas", comidaRepositorio.findByCategoriaAndDisponibleTrue(comida.CAT_BEBIDA));
        return "mesero/pedido-form";
    }

    // Guardar la orden con el nombre del cliente
    @PostMapping("/mesero/pedidos/guardar")
    public String meseroGuardar(@RequestParam("mesaId") String mesaId,
                                @RequestParam("nombreCliente") String nombreCliente,
                                @RequestParam Map<String, String> params,
                                RedirectAttributes ra) {

        Optional<mesa> om = mesaRepositorio.findById(mesaId);

        if (om.isEmpty()) {
            ra.addFlashAttribute("error", "La mesa no existe.");
            return "redirect:/mesero/mesas";
        }

        mesa unaMesa = om.get();

        // Si ya tiene pedido activo, no se crea otro
        if (unaMesa.getPedidoActualId() != null) {
            return "redirect:/mesero/pedidos/" + unaMesa.getPedidoActualId();
        }

        if (nombreCliente == null || nombreCliente.isBlank()) {
            ra.addFlashAttribute("error", "Escribe el nombre del cliente.");
            return "redirect:/mesero/pedidos/nuevo/" + mesaId;
        }

        List<pedido.Item> items = armarItems(params);

        if (items.isEmpty()) {
            ra.addFlashAttribute("error", "Agrega al menos un plato al pedido.");
            return "redirect:/mesero/pedidos/nuevo/" + mesaId;
        }

        pedido nuevoPedido = new pedido();
        nuevoPedido.setNumeroOrden(siguienteNumeroOrden());
        nuevoPedido.setTipo(pedido.TIPO_MESA);
        nuevoPedido.setMesaId(unaMesa.getId());
        nuevoPedido.setNumeroMesa(unaMesa.getNumero());
        nuevoPedido.setNombreCliente(nombreCliente.trim());
        nuevoPedido.setItems(items);
        nuevoPedido.setTotal(sumarItems(items));
        nuevoPedido.setEstado(pedido.TOMADO);
        nuevoPedido.setFecha(LocalDateTime.now());
        nuevoPedido = pedidoRepositorio.save(nuevoPedido);

        unaMesa.setEstado(mesa.OCUPADA);
        unaMesa.setPedidoActualId(nuevoPedido.getId());
        mesaRepositorio.save(unaMesa);

        ra.addFlashAttribute("mensaje", "Pedido #" + nuevoPedido.getNumeroOrden()
                + " guardado para " + nuevoPedido.getNombreCliente() + ".");
        return "redirect:/mesero/pedidos/" + nuevoPedido.getId();
    }

    // Detalle del pedido
    @GetMapping("/mesero/pedidos/{id}")
    public String meseroDetalle(@PathVariable("id") String id, Model model, RedirectAttributes ra) {

        Optional<pedido> op = pedidoRepositorio.findById(id);

        if (op.isEmpty()) {
            ra.addFlashAttribute("error", "El pedido no existe.");
            return "redirect:/mesero/pedidos";
        }

        pedido unPedido = op.get();

        if (unPedido.isDomicilio()) {
            ra.addFlashAttribute("error", "Ese pedido es a domicilio y lo gestiona el cajero.");
            return "redirect:/mesero/pedidos";
        }

        model.addAttribute("titulo", "Pedido | MAGISTRAL");
        model.addAttribute("pedido", unPedido);
        model.addAttribute("mesaOcupada", mesaOcupadaPorPedido(unPedido));
        return "mesero/pedido-detalle";
    }

    // Cancelar un pedido (el cliente se fue o no lo quiso): NO cuenta como venta
    @PostMapping("/mesero/pedidos/{id}/cancelar")
    public String meseroCancelar(@PathVariable("id") String id, RedirectAttributes ra) {

        Optional<pedido> op = pedidoRepositorio.findById(id);

        if (op.isEmpty()) {
            ra.addFlashAttribute("error", "El pedido no existe.");
            return "redirect:/mesero/pedidos";
        }

        pedido unPedido = op.get();

        if (unPedido.isDomicilio()) {
            ra.addFlashAttribute("error", "Ese pedido es a domicilio y lo gestiona el cajero.");
            return "redirect:/mesero/pedidos";
        }

        // Solo se puede cancelar un pedido que sigue activo
        if (!pedido.TOMADO.equals(unPedido.getEstado())) {
            ra.addFlashAttribute("error", "Este pedido ya está cerrado y no se puede cancelar.");
            return "redirect:/mesero/pedidos/" + id;
        }

        // Si ya se cobró algo, no se puede cancelar (ya entró dinero a la caja)
        for (pedido.Item linea : unPedido.getItems()) {
            if (linea.getCantidadPagada() > 0) {
                ra.addFlashAttribute("error", "Este pedido ya tiene pagos registrados y no se puede cancelar.");
                return "redirect:/mesero/pedidos/" + id;
            }
        }

        unPedido.setEstado(pedido.CANCELADO);
        pedidoRepositorio.save(unPedido);

        liberarMesaDelPedido(unPedido);

        ra.addFlashAttribute("mensaje", "Pedido cancelado. La mesa " + unPedido.getNumeroMesa() + " quedó libre.");
        return "redirect:/mesero/mesas";
    }

    // Liberar la mesa de un pedido que ya se pagó (el cliente ya se fue)
    @PostMapping("/mesero/pedidos/{id}/liberar-mesa")
    public String meseroLiberarMesa(@PathVariable("id") String id, RedirectAttributes ra) {
        return liberarMesaPagada(id, ra, "/mesero/mesas");
    }

    // ===================== CAJERO =====================

    // Pedidos por cobrar (separados en mesa y domicilio) y mesas que ya pagaron pero siguen ocupadas
    @GetMapping("/cajero/pedidos")
    public String cajeroListar(Model model) {

        List<pedido> porCobrarMesa = new ArrayList<>();
        List<pedido> porCobrarDomicilio = new ArrayList<>();

        for (pedido p : pedidoRepositorio.findByEstadoOrderByFechaAsc(pedido.TOMADO)) {
            if (p.isDomicilio()) {
                porCobrarDomicilio.add(p);
            } else {
                porCobrarMesa.add(p);
            }
        }

        List<pedido> pagadosConMesa = new ArrayList<>();
        for (mesa m : mesaRepositorio.findAllByOrderByNumeroAsc()) {
            if (m.getPedidoActualId() != null) {
                pedidoRepositorio.findById(m.getPedidoActualId()).ifPresent(p -> {
                    if (pedido.PAGADO.equals(p.getEstado())) {
                        pagadosConMesa.add(p);
                    }
                });
            }
        }

        model.addAttribute("titulo", "Pedidos por cobrar | MAGISTRAL");
        model.addAttribute("pedidos", porCobrarMesa);
        model.addAttribute("pedidosDomicilio", porCobrarDomicilio);
        model.addAttribute("pagadosConMesa", pagadosConMesa);
        model.addAttribute("cajaAbierta", cajaRepositorio.findFirstByEstado(caja.ABIERTA).isPresent());
        return "cajero/pedidos";
    }

    // Pantalla de cobro: modo=total cobra todo, modo=dividido cobra solo lo que elija cada persona
    @GetMapping("/cajero/pedidos/{id}/cobrar")
    public String cajeroCobrar(@PathVariable("id") String id,
                               @RequestParam(name = "modo", defaultValue = "total") String modo,
                               Model model,
                               RedirectAttributes ra) {

        if (cajaRepositorio.findFirstByEstado(caja.ABIERTA).isEmpty()) {
            ra.addFlashAttribute("error", "Abre la caja antes de cobrar.");
            return "redirect:/cajero/caja";
        }

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

        model.addAttribute("titulo", "Cobrar pedido | MAGISTRAL");
        model.addAttribute("pedido", unPedido);
        model.addAttribute("modo", "dividido".equals(modo) ? "dividido" : "total");
        model.addAttribute("porcentajePropina", (int) Math.round(pago.PORCENTAJE_PROPINA * 100));
        return "cajero/cobrar";
    }

    // Comanda de cocina: solo lo que hay que preparar (hamburguesas y salchipapas), con la mesa y la hora
    @GetMapping("/cajero/pedidos/{id}/comanda")
    public String cajeroComanda(@PathVariable("id") String id, Model model, RedirectAttributes ra) {

        Optional<pedido> op = pedidoRepositorio.findById(id);

        if (op.isEmpty()) {
            ra.addFlashAttribute("error", "El pedido no existe.");
            return "redirect:/cajero/pedidos";
        }

        pedido unPedido = op.get();

        List<pedido.Item> paraPreparar = new ArrayList<>();
        for (pedido.Item linea : unPedido.getItems()) {
            Optional<comida> oc = comidaRepositorio.findById(linea.getComidaId());
            if (oc.isPresent()) {
                String categoria = oc.get().getCategoria();
                if (comida.CAT_HAMBURGUESA.equals(categoria) || comida.CAT_SALCHIPAPA.equals(categoria)) {
                    paraPreparar.add(linea);
                }
            }
        }

        model.addAttribute("titulo", "Comanda | MAGISTRAL");
        model.addAttribute("pedido", unPedido);
        model.addAttribute("items", paraPreparar);
        return "cajero/comanda";
    }

    // Liberar la mesa de un pedido que ya se pagó
    @PostMapping("/cajero/pedidos/{id}/liberar-mesa")
    public String cajeroLiberarMesa(@PathVariable("id") String id, RedirectAttributes ra) {
        return liberarMesaPagada(id, ra, "/cajero/pedidos");
    }

    // ===================== ADMIN =====================

    // ===================== CLIENTE =====================

    // Menú del cliente: el mismo de la base de datos, con selector de cantidad y datos de entrega
    @GetMapping("/cliente/menu")
    public String clienteMenu(Model model, HttpSession sesion) {
        model.addAttribute("titulo", "Menú | MAGISTRAL");
        model.addAttribute("menu", armarMenu());
        model.addAttribute("usuarioNombre", sesion.getAttribute("usuarioNombre"));
        return "cliente/menu";
    }

    // Guardar un pedido a domicilio
    @PostMapping("/cliente/pedidos/guardar")
    public String clienteGuardar(@RequestParam(name = "nombreRecibe", defaultValue = "") String nombreRecibe,
                                 @RequestParam(name = "telefono", defaultValue = "") String telefono,
                                 @RequestParam(name = "direccion", defaultValue = "") String direccion,
                                 @RequestParam(name = "metodoPago", defaultValue = "EFECTIVO") String metodoPago,
                                 @RequestParam Map<String, String> params,
                                 HttpSession sesion,
                                 RedirectAttributes ra) {

        String nombreLimpio = recortar(nombreRecibe, 60);
        String direccionLimpia = recortar(direccion, 200);
        String telefonoLimpio = telefono.replaceAll("[^0-9]", "");

        // Si algo falla, el formulario vuelve con lo que el cliente ya había escrito
        ra.addFlashAttribute("nombreRecibe", nombreLimpio);
        ra.addFlashAttribute("telefono", telefonoLimpio);
        ra.addFlashAttribute("direccion", direccionLimpia);
        ra.addFlashAttribute("metodoPago", metodoPago);

        if (nombreLimpio.isEmpty()) {
            ra.addFlashAttribute("error", "Escribe el nombre de quien recibe el pedido.");
            return "redirect:/cliente/menu";
        }

        if (telefonoLimpio.length() < 7 || telefonoLimpio.length() > 15) {
            ra.addFlashAttribute("error", "Escribe un teléfono válido (solo números).");
            return "redirect:/cliente/menu";
        }

        if (direccionLimpia.isEmpty()) {
            ra.addFlashAttribute("error", "Escribe la dirección de entrega.");
            return "redirect:/cliente/menu";
        }

        String metodo = metodoPago;
        if (!pago.EFECTIVO.equals(metodo) && !pago.TARJETA.equals(metodo) && !pago.TRANSFERENCIA.equals(metodo)) {
            metodo = pago.EFECTIVO;
        }

        // Los precios SIEMPRE salen de la base de datos, el navegador solo manda cantidades
        List<pedido.Item> items = armarItems(params);

        if (items.isEmpty()) {
            ra.addFlashAttribute("error", "Agrega al menos un plato al pedido.");
            return "redirect:/cliente/menu";
        }

        pedido nuevoPedido = new pedido();
        nuevoPedido.setNumeroOrden(siguienteNumeroOrden());
        nuevoPedido.setTipo(pedido.TIPO_DOMICILIO);
        nuevoPedido.setClienteId((String) sesion.getAttribute("usuarioId"));
        nuevoPedido.setNombreRecibe(nombreLimpio);
        nuevoPedido.setNombreCliente(nombreLimpio);
        nuevoPedido.setTelefono(telefonoLimpio);
        nuevoPedido.setDireccion(direccionLimpia);
        nuevoPedido.setMetodoPagoElegido(metodo);
        nuevoPedido.setItems(items);
        nuevoPedido.setTotal(sumarItems(items));
        nuevoPedido.setEstado(pedido.TOMADO);
        nuevoPedido.setFecha(LocalDateTime.now());
        nuevoPedido = pedidoRepositorio.save(nuevoPedido);

        ra.addFlashAttribute("mensaje", "¡Pedido #" + nuevoPedido.getNumeroOrden() + " recibido! Ya lo estamos preparando.");
        return "redirect:/cliente/pedidos/" + nuevoPedido.getId() + "/factura";
    }

    // Mis pedidos: los pedidos a domicilio que ha hecho este cliente
    @GetMapping("/cliente/pedidos")
    public String clienteListar(Model model, HttpSession sesion) {

        String clienteId = (String) sesion.getAttribute("usuarioId");

        model.addAttribute("titulo", "Mis pedidos | MAGISTRAL");
        model.addAttribute("pedidos", pedidoRepositorio.findByClienteIdOrderByFechaDesc(clienteId));
        return "cliente/pedidos";
    }

    // Factura en pantalla: solo se puede abrir la de un pedido propio
    @GetMapping("/cliente/pedidos/{id}/factura")
    public String clienteFactura(@PathVariable("id") String id,
                                 Model model,
                                 HttpSession sesion,
                                 RedirectAttributes ra) {

        String clienteId = (String) sesion.getAttribute("usuarioId");

        Optional<pedido> op = pedidoRepositorio.findById(id);

        if (op.isEmpty() || clienteId == null || !clienteId.equals(op.get().getClienteId())) {
            ra.addFlashAttribute("error", "No encontramos ese pedido.");
            return "redirect:/cliente/pedidos";
        }

        pedido unPedido = op.get();

        double subtotal = unPedido.getTotal() != null ? unPedido.getTotal() : 0;
        double propinaSugerida = Math.round(subtotal * pago.PORCENTAJE_PROPINA);

        model.addAttribute("titulo", "Factura | MAGISTRAL");
        model.addAttribute("pedido", unPedido);
        model.addAttribute("porcentajePropina", (int) Math.round(pago.PORCENTAJE_PROPINA * 100));
        model.addAttribute("propinaSugerida", propinaSugerida);
        model.addAttribute("totalConPropina", subtotal + propinaSugerida);
        return "cliente/factura";
    }

    // ===================== AUXILIARES =====================

    // Arma las líneas del pedido con las cantidades del formulario (cantidad_{idComida}) y precios de la base
    private List<pedido.Item> armarItems(Map<String, String> params) {

        List<pedido.Item> items = new ArrayList<>();

        for (comida c : comidaRepositorio.findAll()) {

            if (!c.isDisponible() || c.getPrecio() == null) {
                continue;
            }

            int cantidad = aEntero(params.get("cantidad_" + c.getId()));

            if (cantidad > 0) {
                items.add(new pedido.Item(c.getId(), c.getNombre(), c.getPrecio(), cantidad));
            }
        }

        return items;
    }

    private static double sumarItems(List<pedido.Item> items) {
        double total = 0;
        for (pedido.Item linea : items) {
            total += linea.getSubtotal();
        }
        return total;
    }

    // Número de orden consecutivo: el del último pedido más uno
    private int siguienteNumeroOrden() {
        return pedidoRepositorio.findTopByOrderByNumeroOrdenDesc()
                .map(ultimo -> ultimo.getNumeroOrden() + 1)
                .orElse(1);
    }

    // Platos disponibles agrupados por categoría (hamburguesas, salchipapas y bebidas primero)
    private Map<String, List<comida>> armarMenu() {

        Map<String, List<comida>> menu = new LinkedHashMap<>();
        menu.put(comida.CAT_HAMBURGUESA, new ArrayList<>());
        menu.put(comida.CAT_SALCHIPAPA, new ArrayList<>());
        menu.put(comida.CAT_BEBIDA, new ArrayList<>());

        for (comida plato : comidaRepositorio.findAll()) {
            if (!plato.isDisponible()) {
                continue;
            }

            String categoria = "OTROS";
            if (plato.getCategoria() != null && !plato.getCategoria().isBlank()) {
                categoria = plato.getCategoria().trim().toUpperCase();
            }

            menu.computeIfAbsent(categoria, k -> new ArrayList<>()).add(plato);
        }

        menu.entrySet().removeIf(e -> e.getValue().isEmpty());
        return menu;
    }

    // Libera la mesa solo si el pedido ya está pagado y todavía la tiene ocupada
    private String liberarMesaPagada(String id, RedirectAttributes ra, String destino) {

        Optional<pedido> op = pedidoRepositorio.findById(id);

        if (op.isEmpty()) {
            ra.addFlashAttribute("error", "El pedido no existe.");
            return "redirect:" + destino;
        }

        pedido unPedido = op.get();

        if (unPedido.isDomicilio()) {
            ra.addFlashAttribute("error", "Un pedido a domicilio no tiene mesa.");
            return "redirect:" + destino;
        }

        if (!pedido.PAGADO.equals(unPedido.getEstado())) {
            ra.addFlashAttribute("error", "Solo se libera la mesa de un pedido que ya está pagado.");
            return "redirect:" + destino;
        }

        if (!mesaOcupadaPorPedido(unPedido)) {
            ra.addFlashAttribute("mensaje", "La mesa " + unPedido.getNumeroMesa() + " ya estaba libre.");
            return "redirect:" + destino;
        }

        liberarMesaDelPedido(unPedido);

        ra.addFlashAttribute("mensaje", "La mesa " + unPedido.getNumeroMesa() + " quedó libre.");
        return "redirect:" + destino;
    }

    // ¿La mesa de este pedido sigue ocupada por él? (un pedido a domicilio no tiene mesa)
    private boolean mesaOcupadaPorPedido(pedido unPedido) {
        if (unPedido.getMesaId() == null) {
            return false;
        }
        Optional<mesa> om = mesaRepositorio.findById(unPedido.getMesaId());
        return om.isPresent() && unPedido.getId().equals(om.get().getPedidoActualId());
    }

    // Libera la mesa, pero solo si este pedido era el que tenía activo
    private void liberarMesaDelPedido(pedido unPedido) {
        if (unPedido.getMesaId() == null) {
            return;
        }
        mesaRepositorio.findById(unPedido.getMesaId()).ifPresent(m -> {
            if (unPedido.getId().equals(m.getPedidoActualId())) {
                m.setEstado(mesa.LIBRE);
                m.setPedidoActualId(null);
                mesaRepositorio.save(m);
            }
        });
    }

    // Quita espacios y corta el texto a un máximo de caracteres
    private static String recortar(String texto, int maximo) {
        if (texto == null) {
            return "";
        }
        String limpio = texto.trim();
        return limpio.length() > maximo ? limpio.substring(0, maximo) : limpio;
    }

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
}