package com.pos_magistral.app.controladores;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pos_magistral.app.entidades.mesa;
import com.pos_magistral.app.entidades.usuario;
import com.pos_magistral.app.repositorio.mesa_repositorio;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/mesas")
public class mesa_apirest {

    private final mesa_repositorio repo;

    public mesa_apirest(mesa_repositorio repo) {
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
    public ResponseEntity<?> crear(@RequestBody mesa m, HttpSession s) {
        if (noAdmin(s)) return ResponseEntity.status(403).build();
        m.setId(null);
        return ResponseEntity.ok(repo.save(m));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable("id") String id, @RequestBody mesa m, HttpSession s) {
        if (noAdmin(s)) return ResponseEntity.status(403).build();
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        m.setId(id);
        return ResponseEntity.ok(repo.save(m));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> borrar(@PathVariable("id") String id, HttpSession s) {
        if (noAdmin(s)) return ResponseEntity.status(403).build();
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}