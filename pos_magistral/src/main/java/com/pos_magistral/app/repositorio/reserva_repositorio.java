package com.pos_magistral.app.repositorio;

import com.pos_magistral.app.entidades.reserva;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface reserva_repositorio extends MongoRepository<reserva, String> {

    // Las reservas de un cliente, la más reciente primero: es "Mis reservas"
    List<reserva> findByClienteIdOrderByFechaCreacionDesc(String clienteId);

    // Todas las reservas, las más próximas primero (para el administrador más adelante)
    List<reserva> findAllByOrderByFechaAscHoraAsc();
}