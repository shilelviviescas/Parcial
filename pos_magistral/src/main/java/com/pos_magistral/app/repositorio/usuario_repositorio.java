package com.pos_magistral.app.repositorio;

import com.pos_magistral.app.entidades.usuario;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface usuario_repositorio extends MongoRepository<usuario, String> {

    Optional<usuario> findByCorreo(String correo);

    boolean existsByCorreo(String correo);
}