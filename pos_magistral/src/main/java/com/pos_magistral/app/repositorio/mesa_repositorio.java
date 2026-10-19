package com.pos_magistral.app.repositorio;

import com.pos_magistral.app.entidades.mesa;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface mesa_repositorio extends MongoRepository<mesa, String> {

    List<mesa> findAllByOrderByNumeroAsc();

    Optional<mesa> findByNumero(int numero);
}