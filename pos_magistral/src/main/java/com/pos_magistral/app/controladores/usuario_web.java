package com.pos_magistral.app.controladores;

import com.pos_magistral.app.entidades.usuario;
import com.pos_magistral.app.repositorio.usuario_repositorio;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class usuario_web {

    private final usuario_repositorio usuarioRepositorio;
    private final PasswordEncoder codificador;

    public usuario_web(usuario_repositorio usuarioRepositorio, PasswordEncoder codificador) {
        this.usuarioRepositorio = usuarioRepositorio;
        this.codificador = codificador;
    }

    // ===================== PÚBLICO (login, registro, logout) =====================

    @GetMapping("/login")
    public String loginForm(HttpSession sesion) {

        String destino = panelDe((String) sesion.getAttribute("rol"));

        if (destino != null) {
            return "redirect:" + destino;
        }

        return "redirect:/#login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("correo") String correo,
                        @RequestParam("clave") String clave,
                        HttpSession sesion,
                        RedirectAttributes ra) {

        String correoLimpio = correo == null ? "" : correo.trim().toLowerCase();

        Optional<usuario> ou = usuarioRepositorio.findByCorreo(correoLimpio);

        if (ou.isEmpty() || !codificador.matches(clave, ou.get().getClave())) {
            ra.addFlashAttribute("error", "El correo o la clave no coinciden.");
            ra.addFlashAttribute("correo", correoLimpio);
            return "redirect:/";
        }

        usuario u = ou.get();

        sesion.setAttribute("rol", u.getRol());
        sesion.setAttribute("usuarioId", u.getId());
        sesion.setAttribute("usuarioNombre", u.getNombre());
        sesion.setAttribute("usuarioCorreo", u.getCorreo());

        String destino = panelDe(u.getRol());

        if (destino != null) {
            return "redirect:" + destino;
        }

        ra.addFlashAttribute("mensaje", "Bienvenido, " + u.getNombre() + ". Tu rol no tiene panel asignado.");
        return "redirect:/";
    }

    @GetMapping("/registro")
    public String registroForm() {
        return "registro";
    }

    @PostMapping("/registro")
    public String registro(@RequestParam("nombre") String nombre,
                           @RequestParam("correo") String correo,
                           @RequestParam("clave") String clave,
                           @RequestParam("confirmarClave") String confirmarClave,
                           RedirectAttributes ra) {

        String nombreLimpio = nombre == null ? "" : nombre.trim();
        String correoLimpio = correo == null ? "" : correo.trim().toLowerCase();

        ra.addFlashAttribute("nombre", nombreLimpio);
        ra.addFlashAttribute("correo", correoLimpio);

        if (nombreLimpio.isEmpty() || correoLimpio.isEmpty() || clave == null || clave.isEmpty()) {
            ra.addFlashAttribute("error", "Completa todos los campos.");
            return "redirect:/registro";
        }

        if (clave.length() < 6) {
            ra.addFlashAttribute("error", "La clave debe tener al menos 6 caracteres.");
            return "redirect:/registro";
        }

        if (!clave.equals(confirmarClave)) {
            ra.addFlashAttribute("error", "Las claves no coinciden.");
            return "redirect:/registro";
        }

        if (usuarioRepositorio.existsByCorreo(correoLimpio)) {
            ra.addFlashAttribute("error", "Ya existe una cuenta con ese correo.");
            return "redirect:/registro";
        }

        usuario nuevo = new usuario(nombreLimpio, correoLimpio, codificador.encode(clave), usuario.ROL_CLIENTE);
        usuarioRepositorio.save(nuevo);

        ra.addFlashAttribute("mensaje", "Cuenta creada, ya puedes iniciar sesión.");
        ra.addFlashAttribute("correo", correoLimpio);
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession sesion, RedirectAttributes ra) {
        sesion.invalidate();
        ra.addFlashAttribute("mensaje", "Cerraste sesión. Hasta pronto.");
        return "redirect:/";
    }

    // ===================== AUXILIARES =====================

    // Pantalla de inicio de cada rol
    private static String panelDe(String rol) {
        if (usuario.ROL_MESERO.equals(rol)) {
            return "/mesero/mesas";
        }
        if (usuario.ROL_CAJERO.equals(rol)) {
            return "/cajero/pedidos";
        }
        if (usuario.ROL_ADMIN.equals(rol)) {
            return "/admin/panel";
        }
        if (usuario.ROL_CLIENTE.equals(rol)) {
            return "/cliente/menu";
        }
        return null;
    }
}