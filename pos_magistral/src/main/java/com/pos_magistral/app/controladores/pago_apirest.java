package com.pos_magistral.app.controladores;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pos_magistral.app.entidades.pago;
import com.pos_magistral.app.entidades.usuario;
import com.pos_magistral.app.repositorio.pago_repositorio;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/pagos")
public class pago_apirest {

    private final pago_repositorio repo;

    public pago_apirest(pago_repositorio repo) {
        this.repo = repo;
    }

    private static boolean noAdmin(HttpSession s) {
        return !usuario.ROL_ADMIN.equals(s.getAttribute("rol"));
    }

    private static double suma(List<pago> l) {
        return l.stream().mapToDouble(p -> p.getTotal() == null ? 0 : p.getTotal()).sum();
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

    // Ventas de hoy y del mes: GET /api/pagos/resumen
    @GetMapping("/resumen")
    public ResponseEntity<?> resumen(HttpSession s) {
        if (noAdmin(s)) return ResponseEntity.status(403).build();
        LocalDate hoy = LocalDate.now();
        LocalDate ini = hoy.withDayOfMonth(1);
        List<pago> dia = repo.findByFechaBetweenOrderByFechaAsc(hoy.atStartOfDay(), hoy.plusDays(1).atStartOfDay());
        List<pago> mes = repo.findByFechaBetweenOrderByFechaAsc(ini.atStartOfDay(), ini.plusMonths(1).atStartOfDay());
        Map<String, Object> r = new LinkedHashMap<>();
        r.put("ventasDia", suma(dia));
        r.put("pagosDia", dia.size());
        r.put("ventasMes", suma(mes));
        r.put("pagosMes", mes.size());
        return ResponseEntity.ok(r);
    }
}