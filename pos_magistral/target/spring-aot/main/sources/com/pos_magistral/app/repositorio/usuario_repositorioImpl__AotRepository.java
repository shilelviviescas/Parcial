package com.pos_magistral.app.repositorio;

import com.pos_magistral.app.entidades.usuario;
import java.lang.String;
import java.util.Optional;
import org.springframework.aot.generate.Generated;
import org.springframework.data.mongodb.core.ExecutableFindOperation;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.repository.aot.MongoAotRepositoryFragmentSupport;
import org.springframework.data.repository.core.support.RepositoryFactoryBeanSupport;

/**
 * AOT generated MongoDB repository implementation for {@link usuario_repositorio}.
 */
@Generated
public class usuario_repositorioImpl__AotRepository extends MongoAotRepositoryFragmentSupport {
  private final MongoOperations operations;

  public usuario_repositorioImpl__AotRepository(MongoOperations operations,
      RepositoryFactoryBeanSupport.FragmentCreationContext context) {
    super(operations, context);
    this.operations = operations;
  }

  /**
   * AOT generated implementation of {@link usuario_repositorio#existsByCorreo(java.lang.String)}.
   */
  public boolean existsByCorreo(String correo) {
    class ExpressionMarker{};

    BasicQuery filterQuery = createQuery(ExpressionMarker.class.getEnclosingMethod(), "{\"correo\": ?0}", correo);

    ExecutableFindOperation.FindWithQuery<usuario> finder = operations.query(usuario.class);
    return finder.matching(filterQuery).exists();
  }

  /**
   * AOT generated implementation of {@link usuario_repositorio#findByCorreo(java.lang.String)}.
   */
  public Optional<usuario> findByCorreo(String correo) {
    class ExpressionMarker{};

    BasicQuery filterQuery = createQuery(ExpressionMarker.class.getEnclosingMethod(), "{\"correo\": ?0}", correo);

    ExecutableFindOperation.FindWithQuery<usuario> finder = operations.query(usuario.class);
    return finder.matching(filterQuery).one();
  }
}
