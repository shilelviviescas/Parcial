package com.pos_magistral.app.repositorio;

import com.pos_magistral.app.entidades.pedido;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface pedido_repositorio extends MongoRepository<pedido, String> {

    List<pedido> findAllByOrderByFechaDesc();

    List<pedido> findByMesaId(String mesaId);

    // Pedidos de un estado, los más antiguos primero (el cajero ve los TOMADOS en orden de llegada)
    List<pedido> findByEstadoOrderByFechaAsc(String estado);

    // Los pedidos de un cliente (a domicilio), el más reciente primero: es "Mis pedidos"
    List<pedido> findByClienteIdOrderByFechaDesc(String clienteId);

    // El último pedido creado, para darle al siguiente su número de orden consecutivo
    Optional<pedido> findTopByOrderByNumeroOrdenDesc();
}