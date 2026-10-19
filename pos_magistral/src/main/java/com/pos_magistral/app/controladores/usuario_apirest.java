package com.pos_magistral.app.controladores;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.pos_magistral.app.entidades.usuario;
import com.pos_magistral.app.repositorio.usuario_repositorio;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/usuarios")
public class usuario_apirest {

    private final usuario_repositorio repo;
    private final PasswordEncoder codificador;

    public usuario_apirest(usuario_repositorio repo, PasswordEncoder codificador) {
        this.repo = repo;
        this.codificador = codificador;
    }

    private static boolean noAdmin(HttpSession s) {
        return !usuario.ROL_ADMIN.equals(s.getAttribute("rol"));
    }

    private static usuario sinClave(usuario u) {
        u.setClave(null);
        return u;
    }

    @GetMapping
    public ResponseEntity<?> listar(HttpSession s) {
        if (noAdmin(s)) return ResponseEntity.status(403).build();
        List<usuario> l = repo.findAll();
        l.forEach(usuario_apirest::sinClave);
        return ResponseEntity.ok(l);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> uno(@PathVariable("id") String id, HttpSession s) {
        if (noAdmin(s)) return ResponseEntity.status(403).build();
        return repo.findById(id).<ResponseEntity<?>>map(u -> ResponseEntity.ok(sinClave(u)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody usuario u, HttpSession s) {
        if (noAdmin(s)) return ResponseEntity.status(403).build();
        if (u.getCorreo() == null || u.getClave() == null || u.getClave().length() < 6)
            return ResponseEntity.badRequest().body("Correo y clave (mín. 6) son obligatorios.");
        String correo = u.getCorreo().trim().toLowerCase();
        if (repo.existsByCorreo(correo))
            return ResponseEntity.status(409).body("Ya existe un usuario con ese correo.");
        if (!List.of(usuario.ROL_CAJERO, usuario.ROL_MESERO, usuario.ROL_CLIENTE).contains(u.getRol()))
            return ResponseEntity.badRequest().body("Rol no válido (CAJERO, MESERO o CLIENTE).");
        u.setId(null);
        u.setCorreo(correo);
        u.setClave(codificador.encode(u.getClave()));
        return ResponseEntity.ok(sinClave(repo.save(u)));
    }

    // Actualiza nombre y datos de factura
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable("id") String id, @RequestBody usuario datos, HttpSession s) {
        if (noAdmin(s)) return ResponseEntity.status(403).build();
        return repo.findById(id).<ResponseEntity<?>>map(u -> {
            if (datos.getNombre() != null) u.setNombre(datos.getNombre().trim());
            if (datos.getDocumento() != null) u.setDocumento(datos.getDocumento().trim());
            if (datos.getTelefono() != null) u.setTelefono(datos.getTelefono().trim());
            if (datos.getDireccion() != null) u.setDireccion(datos.getDireccion().trim());
            return ResponseEntity.ok(sinClave(repo.save(u)));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> borrar(@PathVariable("id") String id, HttpSession s) {
        if (noAdmin(s)) return ResponseEntity.status(403).build();
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}