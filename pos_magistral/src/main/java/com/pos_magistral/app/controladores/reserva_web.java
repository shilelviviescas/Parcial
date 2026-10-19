package com.pos_magistral.app.controladores;

import com.pos_magistral.app.entidades.reserva;
import com.pos_magistral.app.repositorio.reserva_repositorio;
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
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Optional;

@Controller
public class reserva_web {

    // Máximo de personas por reserva (las mesas son de 4 puestos, así que 20 son 5 mesas)
    private static final int MAX_PERSONAS = 20;

    private final reserva_repositorio reservaRepositorio;

    public reserva_web(reserva_repositorio reservaRepositorio) {
        this.reservaRepositorio = reservaRepositorio;
    }

    // ===================== MESERO =====================

    // ===================== CAJERO =====================

    // ===================== ADMIN =====================

    // ===================== CLIENTE =====================

    // Formulario para reservar y lista de mis reservas
    @GetMapping("/cliente/reservas")
    public String clienteVer(Model model, HttpSession sesion) {

        String clienteId = (String) sesion.getAttribute("usuarioId");

        model.addAttribute("titulo", "Reservas | MAGISTRAL");
        model.addAttribute("reservas", reservaRepositorio.findByClienteIdOrderByFechaCreacionDesc(clienteId));
        model.addAttribute("ocasiones", reserva.OCASIONES);
        model.addAttribute("fechaMinima", LocalDate.now().toString());
        model.addAttribute("maxPersonas", MAX_PERSONAS);
        return "cliente/reservas";
    }

    // Guardar una reserva nueva
    @PostMapping("/cliente/reservas/guardar")
    public String clienteGuardar(@RequestParam(name = "fecha", defaultValue = "") String fecha,
                                 @RequestParam(name = "hora", defaultValue = "") String hora,
                                 @RequestParam(name = "personas", defaultValue = "1") String personas,
                                 @RequestParam(name = "decoracion", required = false) String decoracion,
                                 @RequestParam(name = "ocasion", defaultValue = "") String ocasion,
                                 HttpSession sesion,
                                 RedirectAttributes ra) {

        boolean pideDecoracion = decoracion != null;

        // Si algo falla, el formulario vuelve con lo que el cliente ya había escrito
        ra.addFlashAttribute("fecha", fecha);
        ra.addFlashAttribute("hora", hora);
        ra.addFlashAttribute("personas", personas);
        ra.addFlashAttribute("decoracion", pideDecoracion);
        ra.addFlashAttribute("ocasion", ocasion);

        LocalDate dia;
        LocalTime horaReserva;

        try {
            dia = LocalDate.parse(fecha);
        } catch (DateTimeParseException e) {
            ra.addFlashAttribute("error", "Elige la fecha de la reserva.");
            return "redirect:/cliente/reservas";
        }

        try {
            horaReserva = LocalTime.parse(hora);
        } catch (DateTimeParseException e) {
            ra.addFlashAttribute("error", "Elige la hora de la reserva.");
            return "redirect:/cliente/reservas";
        }

        if (!LocalDateTime.of(dia, horaReserva).isAfter(LocalDateTime.now())) {
            ra.addFlashAttribute("error", "La fecha y la hora de la reserva deben ser futuras.");
            return "redirect:/cliente/reservas";
        }

        int cantidadPersonas = aEntero(personas);
        if (cantidadPersonas < 1 || cantidadPersonas > MAX_PERSONAS) {
            ra.addFlashAttribute("error", "Las reservas son de 1 a " + MAX_PERSONAS + " personas.");
            return "redirect:/cliente/reservas";
        }

        // La ocasión solo importa (y es obligatoria) si pidió decoración
        String ocasionElegida = null;
        if (pideDecoracion) {
            if (!reserva.OCASIONES.contains(ocasion)) {
                ra.addFlashAttribute("error", "Elige para qué ocasión es la decoración.");
                return "redirect:/cliente/reservas";
            }
            ocasionElegida = ocasion;
        }

        reserva nueva = new reserva();
        nueva.setClienteId((String) sesion.getAttribute("usuarioId"));
        nueva.setNombreCliente((String) sesion.getAttribute("usuarioNombre"));
        nueva.setFecha(dia);
        nueva.setHora(horaReserva);
        nueva.setPersonas(cantidadPersonas);
        nueva.setDecoracion(pideDecoracion);
        nueva.setOcasion(ocasionElegida);
        nueva.setEstado(reserva.PENDIENTE);
        nueva.setFechaCreacion(LocalDateTime.now());
        reservaRepositorio.save(nueva);

        // Reserva guardada: ya no hace falta que el formulario recuerde nada
        ra.addFlashAttribute("fecha", "");
        ra.addFlashAttribute("hora", "");
        ra.addFlashAttribute("personas", "");
        ra.addFlashAttribute("decoracion", false);
        ra.addFlashAttribute("ocasion", "");

        ra.addFlashAttribute("mensaje", "Reserva enviada. Queda pendiente de confirmación.");
        return "redirect:/cliente/reservas";
    }

    // Cancelar una reserva propia
    @PostMapping("/cliente/reservas/{id}/cancelar")
    public String clienteCancelar(@PathVariable("id") String id, HttpSession sesion, RedirectAttributes ra) {

        String clienteId = (String) sesion.getAttribute("usuarioId");

        Optional<reserva> or = reservaRepositorio.findById(id);

        // Solo se puede cancelar una reserva propia
        if (or.isEmpty() || clienteId == null || !clienteId.equals(or.get().getClienteId())) {
            ra.addFlashAttribute("error", "No encontramos esa reserva.");
            return "redirect:/cliente/reservas";
        }

        reserva r = or.get();

        if (reserva.CANCELADA.equals(r.getEstado())) {
            ra.addFlashAttribute("mensaje", "Esa reserva ya estaba cancelada.");
            return "redirect:/cliente/reservas";
        }

        r.setEstado(reserva.CANCELADA);
        reservaRepositorio.save(r);

        ra.addFlashAttribute("mensaje", "Reserva cancelada.");
        return "redirect:/cliente/reservas";
    }

    // Convierte el texto del formulario en un número (0 si no es válido)
    private static int aEntero(String valor) {
        if (valor == null || valor.isBlank()) {
            return 0;
        }
        try {
            return Integer.parseInt(valor.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}