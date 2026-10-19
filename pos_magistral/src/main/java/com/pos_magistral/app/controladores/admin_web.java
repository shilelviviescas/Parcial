package com.pos_magistral.app.controladores;

import java.time.LocalDate;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pos_magistral.app.entidades.pago;
import com.pos_magistral.app.entidades.usuario;
import com.pos_magistral.app.repositorio.caja_repositorio;
import com.pos_magistral.app.repositorio.pago_repositorio;
import com.pos_magistral.app.repositorio.usuario_repositorio;

import jakarta.servlet.http.HttpSession;

@Controller
public class admin_web {

    private final usuario_repositorio usuarioRepo;
    private final caja_repositorio cajaRepo;
    private final pago_repositorio pagoRepo;
    private final PasswordEncoder codificador;

    public admin_web(usuario_repositorio usuarioRepo, caja_repositorio cajaRepo,
                     pago_repositorio pagoRepo, PasswordEncoder codificador) {
        this.usuarioRepo = usuarioRepo;
        this.cajaRepo = cajaRepo;
        this.pagoRepo = pagoRepo;
        this.codificador = codificador;
    }

    @GetMapping("/admin/panel")
    public String panel(@RequestParam(value = "editar", required = false) String editarId,
                        HttpSession sesion, Model m) {
        LocalDate hoy = LocalDate.now();
        LocalDate iniMes = hoy.withDayOfMonth(1);

        List<pago> delDia = pagoRepo.findByFechaBetweenOrderByFechaAsc(
                hoy.atStartOfDay(), hoy.plusDays(1).atStartOfDay());
        List<pago> delMes = pagoRepo.findByFechaBetweenOrderByFechaAsc(
                iniMes.atStartOfDay(), iniMes.plusMonths(1).atStartOfDay());

        m.addAttribute("titulo", "Administración | MAGISTRAL");
        m.addAttribute("nombre", sesion.getAttribute("usuarioNombre"));
        m.addAttribute("ventasDia", suma(delDia));
        m.addAttribute("pagosDia", delDia.size());
        m.addAttribute("ventasMes", suma(delMes));
        m.addAttribute("pagosMes", delMes.size());
        m.addAttribute("usuarios", usuarioRepo.findAll());
        m.addAttribute("cajas", cajaRepo.findAllByOrderByFechaAperturaDesc());
        if (editarId != null) {
            usuarioRepo.findById(editarId).ifPresent(u -> m.addAttribute("editar", u));
        }
        return "admin/panel";
    }

    @PostMapping("/admin/usuarios/crear")
    public String crear(@RequestParam("nombre") String nombre,
                        @RequestParam("correo") String correo,
                        @RequestParam("clave") String clave,
                        @RequestParam("rol") String rol,
                        RedirectAttributes ra) {
        String c = correo.trim().toLowerCase();
        if (nombre.isBlank() || c.isEmpty() || clave.length() < 6) {
            ra.addFlashAttribute("error", "Completa los campos (clave mínimo 6 caracteres).");
        } else if (!List.of(usuario.ROL_CAJERO, usuario.ROL_MESERO, usuario.ROL_CLIENTE).contains(rol)) {
            ra.addFlashAttribute("error", "Rol no válido.");
        } else if (usuarioRepo.existsByCorreo(c)) {
            ra.addFlashAttribute("error", "Ya existe un usuario con ese correo.");
        } else {
            usuarioRepo.save(new usuario(nombre.trim(), c, codificador.encode(clave), rol));
            ra.addFlashAttribute("mensaje", "Usuario creado como " + rol + ".");
        }
        return "redirect:/admin/panel";
    }

    @PostMapping("/admin/usuarios/{id}/actualizar")
    public String actualizar(@PathVariable("id") String id,
                             @RequestParam("nombre") String nombre,
                             @RequestParam(value = "documento", defaultValue = "") String documento,
                             @RequestParam(value = "telefono", defaultValue = "") String telefono,
                             @RequestParam(value = "direccion", defaultValue = "") String direccion,
                             RedirectAttributes ra) {
        usuarioRepo.findById(id).ifPresentOrElse(u -> {
            u.setNombre(nombre.trim());
            u.setDocumento(documento.trim());
            u.setTelefono(telefono.trim());
            u.setDireccion(direccion.trim());
            usuarioRepo.save(u);
            ra.addFlashAttribute("mensaje", "Datos de facturación actualizados.");
        }, () -> ra.addFlashAttribute("error", "Usuario no encontrado."));
        return "redirect:/admin/panel";
    }

    private static double suma(List<pago> pagos) {
        return pagos.stream().mapToDouble(p -> p.getTotal() == null ? 0 : p.getTotal()).sum();
    }
}