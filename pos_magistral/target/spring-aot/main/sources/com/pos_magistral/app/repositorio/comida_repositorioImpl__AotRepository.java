package com.pos_magistral.app.repositorio;

import com.pos_magistral.app.entidades.comida;
import java.lang.String;
import java.util.List;
import org.springframework.aot.generate.Generated;
import org.springframework.data.mongodb.core.ExecutableFindOperation;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.repository.aot.MongoAotRepositoryFragmentSupport;
import org.springframework.data.repository.core.support.RepositoryFactoryBeanSupport;

/**
 * AOT generated MongoDB repository implementation for {@link comida_repositorio}.
 */
@Generated
public class comida_repositorioImpl__AotRepository extends MongoAotRepositoryFragmentSupport {
  private final MongoOperations operations;

  public comida_repositorioImpl__AotRepository(MongoOperations operations,
      RepositoryFactoryBeanSupport.FragmentCreationContext context) {
    super(operations, context);
    this.operations = operations;
  }

  /**
   * AOT generated implementation of {@link comida_repositorio#findByCategoria(java.lang.String)}.
   */
  public List<comida> findByCategoria(String categoria) {
    class ExpressionMarker{};

    BasicQuery filterQuery = createQuery(ExpressionMarker.class.getEnclosingMethod(), "{\"categoria\": ?0}", categoria);

    ExecutableFindOperation.FindWithQuery<comida> finder = operations.query(comida.class);
    return finder.matching(filterQuery).all();
  }

  /**
   * AOT generated implementation of {@link comida_repositorio#findByCategoriaAndDisponibleTrue(java.lang.String)}.
   */
  public List<comida> findByCategoriaAndDisponibleTrue(String categoria) {
    class ExpressionMarker{};

    BasicQuery filterQuery = createQuery(ExpressionMarker.class.getEnclosingMethod(), "{\"categoria\": ?0, \"disponible\": true}", categoria);

    ExecutableFindOperation.FindWithQuery<comida> finder = operations.query(comida.class);
    return finder.matching(filterQuery).all();
  }

  /**
   * AOT generated implementation of {@link comida_repositorio#findByDisponibleTrue()}.
   */
  public List<comida> findByDisponibleTrue() {

    BasicQuery filterQuery = new BasicQuery(parse("{\"disponible\": true}"));

    ExecutableFindOperation.FindWithQuery<comida> finder = operations.query(comida.class);
    return finder.matching(filterQuery).all();
  }
}
