package com.pos_magistral.app.controladores;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pos_magistral.app.entidades.reserva;
import com.pos_magistral.app.entidades.usuario;
import com.pos_magistral.app.repositorio.reserva_repositorio;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/reservas")
public class reserva_apirest {

    private final reserva_repositorio repo;

    public reserva_apirest(reserva_repositorio repo) {
        this.repo = repo;
    }

    private static boolean noAdmin(HttpSession s) {
        return !usuario.ROL_ADMIN.equals(s.getAttribute("rol"));
    }

    @GetMapping
    public ResponseEntity<?> listar(HttpSession s) {
        if (noAdmin(s)) return ResponseEntity.status(403).build();
        return ResponseEntity.ok(repo.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> uno(@PathVariable("id") String id, HttpSession s) {
        if (noAdmin(s)) return ResponseEntity.status(403).build();
        return repo.findById(id).<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody reserva r, HttpSession s) {
        if (noAdmin(s)) return ResponseEntity.status(403).build();
        r.setId(null);
        return ResponseEntity.ok(repo.save(r));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable("id") String id, @RequestBody reserva r, HttpSession s) {
        if (noAdmin(s)) return ResponseEntity.status(403).build();
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        r.setId(id);
        return ResponseEntity.ok(repo.save(r));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> borrar(@PathVariable("id") String id, HttpSession s) {
        if (noAdmin(s)) return ResponseEntity.status(403).build();
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}