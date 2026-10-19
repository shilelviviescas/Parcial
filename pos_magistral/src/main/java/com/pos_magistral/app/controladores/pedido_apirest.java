package com.pos_magistral.app.controladores;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pos_magistral.app.entidades.usuario;
import com.pos_magistral.app.repositorio.pedido_repositorio;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/pedidos")
public class pedido_apirest {

    private final pedido_repositorio repo;

    public pedido_apirest(pedido_repositorio repo) {
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
}