package com.pos_magistral.app.repositorio;

import com.pos_magistral.app.entidades.caja;
import java.lang.String;
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
 * AOT generated MongoDB repository implementation for {@link caja_repositorio}.
 */
@Generated
public class caja_repositorioImpl__AotRepository extends MongoAotRepositoryFragmentSupport {
  private final MongoOperations operations;

  public caja_repositorioImpl__AotRepository(MongoOperations operations,
      RepositoryFactoryBeanSupport.FragmentCreationContext context) {
    super(operations, context);
    this.operations = operations;
  }

  /**
   * AOT generated implementation of {@link caja_repositorio#findAllByOrderByFechaAperturaDesc()}.
   */
  public List<caja> findAllByOrderByFechaAperturaDesc() {

    BasicQuery filterQuery = new BasicQuery(new Document());
    Document sort = parse("{\"fechaApertura\": -1}");
    filterQuery.setSortObject(sort);

    ExecutableFindOperation.FindWithQuery<caja> finder = operations.query(caja.class);
    return finder.matching(filterQuery).all();
  }

  /**
   * AOT generated implementation of {@link caja_repositorio#findFirstByEstado(java.lang.String)}.
   */
  public Optional<caja> findFirstByEstado(String estado) {
    class ExpressionMarker{};

    BasicQuery filterQuery = createQuery(ExpressionMarker.class.getEnclosingMethod(), "{\"estado\": ?0}", estado);
    filterQuery.limit(1);

    ExecutableFindOperation.FindWithQuery<caja> finder = operations.query(caja.class);
    return finder.matching(filterQuery).first();
  }
}
