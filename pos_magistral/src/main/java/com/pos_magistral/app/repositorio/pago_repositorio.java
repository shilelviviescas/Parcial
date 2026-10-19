package com.pos_magistral.app.repositorio;

import com.pos_magistral.app.entidades.pago;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface pago_repositorio extends MongoRepository<pago, String> {

    // Todos los pagos de un pedido (en cuenta dividida hay uno por persona)
    List<pago> findByPedidoIdOrderByFechaAsc(String pedidoId);

    // Pagos de una caja: sirve para el cierre de caja
    List<pago> findByCajaIdOrderByFechaAsc(String cajaId);

    // Pagos de un rango de fechas: sirve para las ventas del día
    List<pago> findByFechaBetweenOrderByFechaAsc(LocalDateTime desde, LocalDateTime hasta);
}