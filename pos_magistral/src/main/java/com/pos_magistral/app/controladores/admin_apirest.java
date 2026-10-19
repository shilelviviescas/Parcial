package com.pos_magistral.app.controladores;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pos_magistral.app.entidades.pago;
import com.pos_magistral.app.entidades.usuario;
import com.pos_magistral.app.repositorio.caja_repositorio;
import com.pos_magistral.app.repositorio.pago_repositorio;
import com.pos_magistral.app.repositorio.usuario_repositorio;

import jakarta.servlet.http.HttpSession;

// Equivalente REST de admin_web (/admin/panel).
// Crear/actualizar usuarios: POST y PUT /api/usuarios. Cajas: /api/cajas.
@RestController
@RequestMapping("/api/admin")
public class admin_apirest {

    private final usuario_repositorio usuarioRepo;
    private final caja_repositorio cajaRepo;
    private final pago_repositorio pagoRepo;

    public admin_apirest(usuario_repositorio usuarioRepo, caja_repositorio cajaRepo,
                         pago_repositorio pagoRepo) {
        this.usuarioRepo = usuarioRepo;
        this.cajaRepo = cajaRepo;
        this.pagoRepo = pagoRepo;
    }

    private static boolean noAdmin(HttpSession s) {
        return !usuario.ROL_ADMIN.equals(s.getAttribute("rol"));
    }

    private static double suma(List<pago> l) {
        return l.stream().mapToDouble(p -> p.getTotal() == null ? 0 : p.getTotal()).sum();
    }

    // GET /api/admin/panel: ventas de hoy y del mes, usuarios (sin clave) y cajas
    @GetMapping("/panel")
    public ResponseEntity<?> panel(HttpSession s) {
        if (noAdmin(s)) return ResponseEntity.status(403).build();
        LocalDate hoy = LocalDate.now();
        LocalDate ini = hoy.withDayOfMonth(1);
        List<pago> dia = pagoRepo.findByFechaBetweenOrderByFechaAsc(hoy.atStartOfDay(), hoy.plusDays(1).atStartOfDay());
        List<pago> mes = pagoRepo.findByFechaBetweenOrderByFechaAsc(ini.atStartOfDay(), ini.plusMonths(1).atStartOfDay());
        List<usuario> usuarios = usuarioRepo.findAll();
        usuarios.forEach(u -> u.setClave(null));

        Map<String, Object> r = new LinkedHashMap<>();
        r.put("ventasDia", suma(dia));
        r.put("pagosDia", dia.size());
        r.put("ventasMes", suma(mes));
        r.put("pagosMes", mes.size());
        r.put("usuarios", usuarios);
        r.put("cajas", cajaRepo.findAllByOrderByFechaAperturaDesc());
        return ResponseEntity.ok(r);
    }
}