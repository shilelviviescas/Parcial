package com.pos_magistral.app.controladores;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pos_magistral.app.entidades.comida;
import com.pos_magistral.app.entidades.usuario;
import com.pos_magistral.app.repositorio.comida_repositorio;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/comidas")
public class comida_apirest {

    private final comida_repositorio repo;

    public comida_apirest(comida_repositorio repo) {
        this.repo = repo;
    }

    private static boolean noAdmin(HttpSession s) {
        return !usuario.ROL_ADMIN.equals(s.getAttribute("rol"));
    }

    @GetMapping
    public ResponseEntity<?> listar(HttpSession s) {
        if (noAdmin(s)) return ResponseEntity.status(403).build();
        List<comida> l = repo.findAll();
        return ResponseEntity.ok(l);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> uno(@PathVariable("id") String id, HttpSession s) {
        if (noAdmin(s)) return ResponseEntity.status(403).build();
        return repo.findById(id).<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody comida c, HttpSession s) {
        if (noAdmin(s)) return ResponseEntity.status(403).build();
        c.setId(null);
        return ResponseEntity.ok(repo.save(c));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable("id") String id, @RequestBody comida c, HttpSession s) {
        if (noAdmin(s)) return ResponseEntity.status(403).build();
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        c.setId(id);
        return ResponseEntity.ok(repo.save(c));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> borrar(@PathVariable("id") String id, HttpSession s) {
        if (noAdmin(s)) return ResponseEntity.status(403).build();
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}