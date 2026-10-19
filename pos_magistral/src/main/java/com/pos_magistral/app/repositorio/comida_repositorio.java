package com.pos_magistral.app.repositorio;

import com.pos_magistral.app.entidades.comida;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface comida_repositorio extends MongoRepository<comida, String> {

    List<comida> findByCategoriaAndDisponibleTrue(String categoria);

    List<comida> findByCategoria(String categoria);

    List<comida> findByDisponibleTrue();
}