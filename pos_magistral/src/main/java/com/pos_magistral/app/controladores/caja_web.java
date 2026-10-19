package com.pos_magistral.app.controladores;

import com.pos_magistral.app.entidades.caja;
import com.pos_magistral.app.entidades.pago;
import com.pos_magistral.app.entidades.pedido;
import com.pos_magistral.app.repositorio.caja_repositorio;
import com.pos_magistral.app.repositorio.pago_repositorio;
import com.pos_magistral.app.repositorio.pedido_repositorio;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Controller
public class caja_web {

    private final caja_repositorio cajaRepositorio;
    private final pago_repositorio pagoRepositorio;
    private final pedido_repositorio pedidoRepositorio;

    public caja_web(caja_repositorio cajaRepositorio,
                    pago_repositorio pagoRepositorio,
                    pedido_repositorio pedidoRepositorio) {
        this.cajaRepositorio = cajaRepositorio;
        this.pagoRepositorio = pagoRepositorio;
        this.pedidoRepositorio = pedidoRepositorio;
    }

    // ===================== MESERO =====================

    // ===================== CAJERO =====================

    // Estado de la caja: si está abierta muestra el resumen en vivo, si no, el formulario de apertura
    @GetMapping("/cajero/caja")
    public String cajeroVer(Model model) {

        model.addAttribute("titulo", "Caja | MAGISTRAL");

        Optional<caja> oc = cajaRepositorio.findFirstByEstado(caja.ABIERTA);

        if (oc.isPresent()) {
            caja unaCaja = oc.get();
            double[] t = totalesDePagos(pagoRepositorio.findByCajaIdOrderByFechaAsc(unaCaja.getId()));

            model.addAttribute("cajaActual", unaCaja);
            model.addAttribute("totalEfectivo", t[0]);
            model.addAttribute("totalTarjeta", t[1]);
            model.addAttribute("totalTransferencia", t[2]);
            model.addAttribute("totalVentas", t[0] + t[1] + t[2]);
            model.addAttribute("cantidadPagos", (int) t[3]);
            model.addAttribute("efectivoEsperado", unaCaja.getMontoInicial() + t[0]);
            model.addAttribute("pedidosPendientes",
                    pedidoRepositorio.findByEstadoOrderByFechaAsc(pedido.TOMADO).size());
        }

        // Últimas 10 cajas cerradas
        List<caja> historial = new ArrayList<>();
        for (caja c : cajaRepositorio.findAllByOrderByFechaAperturaDesc()) {
            if (caja.CERRADA.equals(c.getEstado())) {
                historial.add(c);
                if (historial.size() == 10) {
                    break;
                }
            }
        }
        model.addAttribute("historial", historial);

        return "cajero/caja";
    }

    // Abrir caja con el efectivo con el que empieza
    @PostMapping("/cajero/caja/abrir")
    public String cajeroAbrir(@RequestParam(defaultValue = "0") String montoInicial,
                              HttpSession sesion,
                              RedirectAttributes ra) {

        if (cajaRepositorio.findFirstByEstado(caja.ABIERTA).isPresent()) {
            ra.addFlashAttribute("error", "Ya hay una caja abierta.");
            return "redirect:/cajero/caja";
        }

        caja nueva = new caja();
        nueva.setEstado(caja.ABIERTA);
        nueva.setCajeroId((String) sesion.getAttribute("usuarioId"));
        nueva.setCajeroNombre((String) sesion.getAttribute("usuarioNombre"));
        nueva.setMontoInicial((double) aPesos(montoInicial));
        nueva.setFechaApertura(LocalDateTime.now());
        cajaRepositorio.save(nueva);

        ra.addFlashAttribute("mensaje", "Caja abierta con $ " + formato(nueva.getMontoInicial()) + ".");
        return "redirect:/cajero/caja";
    }

    // Cerrar caja: se guardan los totales por medio de pago y se compara el efectivo contado con el esperado
    @PostMapping("/cajero/caja/cerrar")
    public String cajeroCerrar(@RequestParam(defaultValue = "0") String efectivoContado,
                               RedirectAttributes ra) {

        Optional<caja> oc = cajaRepositorio.findFirstByEstado(caja.ABIERTA);

        if (oc.isEmpty()) {
            ra.addFlashAttribute("error", "No hay ninguna caja abierta.");
            return "redirect:/cajero/caja";
        }

        caja unaCaja = oc.get();
        double[] t = totalesDePagos(pagoRepositorio.findByCajaIdOrderByFechaAsc(unaCaja.getId()));

        double contado = aPesos(efectivoContado);
        double esperado = unaCaja.getMontoInicial() + t[0];
        double diferencia = contado - esperado;

        unaCaja.setTotalEfectivo(t[0]);
        unaCaja.setTotalTarjeta(t[1]);
        unaCaja.setTotalTransferencia(t[2]);
        unaCaja.setTotalVentas(t[0] + t[1] + t[2]);
        unaCaja.setCantidadPagos((int) t[3]);
        unaCaja.setEfectivoEsperado(esperado);
        unaCaja.setEfectivoContado(contado);
        unaCaja.setDiferencia(diferencia);
        unaCaja.setFechaCierre(LocalDateTime.now());
        unaCaja.setEstado(caja.CERRADA);
        cajaRepositorio.save(unaCaja);

        String resultado;
        if (diferencia == 0) {
            resultado = "El efectivo cuadra.";
        } else if (diferencia > 0) {
            resultado = "Sobran $ " + formato(diferencia) + " en efectivo.";
        } else {
            resultado = "Faltan $ " + formato(Math.abs(diferencia)) + " en efectivo.";
        }

        ra.addFlashAttribute("mensaje", "Caja cerrada. " + resultado);
        return "redirect:/cajero/caja";
    }

    // ===================== ADMIN =====================

    // ===================== CLIENTE =====================

    // Suma lo cobrado por medio de pago: {efectivo, tarjeta, transferencia, cantidad de pagos}
    private static double[] totalesDePagos(List<pago> pagos) {
        double efectivo = 0;
        double tarjeta = 0;
        double transferencia = 0;

        for (pago p : pagos) {
            double total = p.getTotal() != null ? p.getTotal() : 0;

            if (pago.EFECTIVO.equals(p.getMetodo())) {
                efectivo += total;
            } else if (pago.TARJETA.equals(p.getMetodo())) {
                tarjeta += total;
            } else if (pago.TRANSFERENCIA.equals(p.getMetodo())) {
                transferencia += total;
            }
        }

        return new double[]{efectivo, tarjeta, transferencia, pagos.size()};
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