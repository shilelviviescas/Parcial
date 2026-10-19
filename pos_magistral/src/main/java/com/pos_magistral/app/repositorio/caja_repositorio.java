package com.pos_magistral.app.repositorio;

import com.pos_magistral.app.entidades.caja;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface caja_repositorio extends MongoRepository<caja, String> {

    // La caja que está abierta ahora (debe haber una sola a la vez)
    Optional<caja> findFirstByEstado(String estado);

    // Historial de cajas, la más reciente primero
    List<caja> findAllByOrderByFechaAperturaDesc();
}