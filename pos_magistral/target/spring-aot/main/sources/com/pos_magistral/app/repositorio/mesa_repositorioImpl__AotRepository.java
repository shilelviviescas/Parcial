package com.pos_magistral.app.repositorio;

import com.pos_magistral.app.entidades.mesa;
import java.util.List;
import java.util.Optional;
import org.bson.Document;
import org.springframework.aot.generate.Generated;
import org.springframework.data.mongodb.core.ExecutableFindOperation;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.repository.aot.MongoAotRepositoryFragmentSupport;
import org.springframework.data.repository.core.support.RepositoryFactoryBeanSupport;

/**
 * AOT generated MongoDB repository implementation for {@link mesa_repositorio}.
 */
@Generated
public class mesa_repositorioImpl__AotRepository extends MongoAotRepositoryFragmentSupport {
  private final MongoOperations operations;

  public mesa_repositorioImpl__AotRepository(MongoOperations operations,
      RepositoryFactoryBeanSupport.FragmentCreationContext context) {
    super(operations, context);
    this.operations = operations;
  }

  /**
   * AOT generated implementation of {@link mesa_repositorio#findAllByOrderByNumeroAsc()}.
   */
  public List<mesa> findAllByOrderByNumeroAsc() {

    BasicQuery filterQuery = new BasicQuery(new Document());
    Document sort = parse("{\"numero\": 1}");
    filterQuery.setSortObject(sort);

    ExecutableFindOperation.FindWithQuery<mesa> finder = operations.query(mesa.class);
    return finder.matching(filterQuery).all();
  }

  /**
   * AOT generated implementation of {@link mesa_repositorio#findByNumero(int)}.
   */
  public Optional<mesa> findByNumero(int numero) {
    class ExpressionMarker{};

    BasicQuery filterQuery = createQuery(ExpressionMarker.class.getEnclosingMethod(), "{\"numero\": ?0}", numero);

    ExecutableFindOperation.FindWithQuery<mesa> finder = operations.query(mesa.class);
    return finder.matching(filterQuery).one();
  }
}
