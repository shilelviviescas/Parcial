package com.pos_magistral.app.controladores;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pos_magistral.app.entidades.caja;
import com.pos_magistral.app.entidades.usuario;
import com.pos_magistral.app.repositorio.caja_repositorio;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/cajas")
public class caja_apirest {

    private final caja_repositorio repo;

    public caja_apirest(caja_repositorio repo) {
        this.repo = repo;
    }

    private static boolean noAdmin(HttpSession s) {
        return !usuario.ROL_ADMIN.equals(s.getAttribute("rol"));
    }

    // Historial de aperturas y cierres, la más reciente primero
    @GetMapping
    public ResponseEntity<?> listar(HttpSession s) {
        if (noAdmin(s)) return ResponseEntity.status(403).build();
        return ResponseEntity.ok(repo.findAllByOrderByFechaAperturaDesc());
    }

    // Caja abierta ahora mismo
    @GetMapping("/abierta")
    public ResponseEntity<?> abierta(HttpSession s) {
        if (noAdmin(s)) return ResponseEntity.status(403).build();
        return repo.findFirstByEstado(caja.ABIERTA).<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> uno(@PathVariable("id") String id, HttpSession s) {
        if (noAdmin(s)) return ResponseEntity.status(403).build();
        return repo.findById(id).<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}