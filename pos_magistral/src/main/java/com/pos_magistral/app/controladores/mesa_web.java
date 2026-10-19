package com.pos_magistral.app.controladores;

import com.pos_magistral.app.entidades.mesa;
import com.pos_magistral.app.entidades.pedido;
import com.pos_magistral.app.repositorio.mesa_repositorio;
import com.pos_magistral.app.repositorio.pedido_repositorio;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class mesa_web {

    private final mesa_repositorio mesaRepositorio;
    private final pedido_repositorio pedidoRepositorio;

    public mesa_web(mesa_repositorio mesaRepositorio, pedido_repositorio pedidoRepositorio) {
        this.mesaRepositorio = mesaRepositorio;
        this.pedidoRepositorio = pedidoRepositorio;
    }

    // ===================== MESERO =====================

    @GetMapping("/mesero/mesas")
    public String meseroListar(Model model) {

        List<mesa> mesas = mesaRepositorio.findAllByOrderByNumeroAsc();

        Map<String, pedido> pedidosPorMesa = new HashMap<>();
        long libres = 0;
        long ocupadas = 0;

        for (mesa m : mesas) {
            if (mesa.OCUPADA.equals(m.getEstado())) {
                ocupadas++;
            } else {
                libres++;
            }

            if (m.getPedidoActualId() != null) {
                pedidoRepositorio.findById(m.getPedidoActualId())
                        .ifPresent(p -> pedidosPorMesa.put(m.getId(), p));
            }
        }

        model.addAttribute("titulo", "Mesas | MAGISTRAL");
        model.addAttribute("mesas", mesas);
        model.addAttribute("pedidosPorMesa", pedidosPorMesa);
        model.addAttribute("libres", libres);
        model.addAttribute("ocupadas", ocupadas);
        return "mesero/mesas";
    }

    // Al empezar a tomar el pedido, la mesa pasa a OCUPADA
    @PostMapping("/mesero/mesas/{id}/ocupar")
    public String meseroOcupar(@PathVariable String id, RedirectAttributes ra) {

        Optional<mesa> om = mesaRepositorio.findById(id);

        if (om.isEmpty()) {
            ra.addFlashAttribute("error", "La mesa no existe.");
            return "redirect:/mesero/mesas";
        }

        mesa unaMesa = om.get();

        // Si ya tiene un pedido activo, se va directo a verlo
        if (unaMesa.getPedidoActualId() != null) {
            return "redirect:/mesero/pedidos/" + unaMesa.getPedidoActualId();
        }

        unaMesa.setEstado(mesa.OCUPADA);
        mesaRepositorio.save(unaMesa);

        return "redirect:/mesero/pedidos/nuevo/" + id;
    }

    // Cancelar la toma del pedido: la mesa vuelve a quedar libre
    @PostMapping("/mesero/mesas/{id}/liberar")
    public String meseroLiberar(@PathVariable String id, RedirectAttributes ra) {

        Optional<mesa> om = mesaRepositorio.findById(id);

        if (om.isEmpty()) {
            ra.addFlashAttribute("error", "La mesa no existe.");
            return "redirect:/mesero/mesas";
        }

        mesa unaMesa = om.get();

        // Una mesa con pedido activo no se libera a la fuerza: primero se finaliza el pedido
        if (unaMesa.getPedidoActualId() != null) {
            ra.addFlashAttribute("error", "La mesa " + unaMesa.getNumero() + " tiene un pedido activo. Finalízalo primero.");
            return "redirect:/mesero/mesas";
        }

        unaMesa.setEstado(mesa.LIBRE);
        mesaRepositorio.save(unaMesa);

        ra.addFlashAttribute("mensaje", "La mesa " + unaMesa.getNumero() + " quedó libre.");
        return "redirect:/mesero/mesas";
    }

    // ===================== CAJERO =====================

    // ===================== ADMIN =====================

    // ===================== CLIENTE =====================
}