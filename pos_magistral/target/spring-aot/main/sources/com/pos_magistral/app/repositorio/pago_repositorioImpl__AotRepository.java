package com.pos_magistral.app.repositorio;

import com.pos_magistral.app.entidades.pago;
import java.lang.String;
import java.time.LocalDateTime;
import java.util.List;
import org.bson.Document;
import org.springframework.aot.generate.Generated;
import org.springframework.data.mongodb.core.ExecutableFindOperation;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.repository.aot.MongoAotRepositoryFragmentSupport;
import org.springframework.data.repository.core.support.RepositoryFactoryBeanSupport;

/**
 * AOT generated MongoDB repository implementation for {@link pago_repositorio}.
 */
@Generated
public class pago_repositorioImpl__AotRepository extends MongoAotRepositoryFragmentSupport {
  private final MongoOperations operations;

  public pago_repositorioImpl__AotRepository(MongoOperations operations,
      RepositoryFactoryBeanSupport.FragmentCreationContext context) {
    super(operations, context);
    this.operations = operations;
  }

  /**
   * AOT generated implementation of {@link pago_repositorio#findByCajaIdOrderByFechaAsc(java.lang.String)}.
   */
  public List<pago> findByCajaIdOrderByFechaAsc(String cajaId) {
    class ExpressionMarker{};

    BasicQuery filterQuery = createQuery(ExpressionMarker.class.getEnclosingMethod(), "{\"cajaId\": ?0}", cajaId);
    Document sort = parse("{\"fecha\": 1}");
    filterQuery.setSortObject(sort);

    ExecutableFindOperation.FindWithQuery<pago> finder = operations.query(pago.class);
    return finder.matching(filterQuery).all();
  }

  /**
   * AOT generated implementation of {@link pago_repositorio#findByFechaBetweenOrderByFechaAsc(java.time.LocalDateTime,java.time.LocalDateTime)}.
   */
  public List<pago> findByFechaBetweenOrderByFechaAsc(LocalDateTime desde, LocalDateTime hasta) {
    class ExpressionMarker{};

    BasicQuery filterQuery = createQuery(ExpressionMarker.class.getEnclosingMethod(), "{\"fecha\": {\"$gt\": ?0, \"$lt\": ?1}}", desde, hasta);
    Document sort = parse("{\"fecha\": 1}");
    filterQuery.setSortObject(sort);

    ExecutableFindOperation.FindWithQuery<pago> finder = operations.query(pago.class);
    return finder.matching(filterQuery).all();
  }

  /**
   * AOT generated implementation of {@link pago_repositorio#findByPedidoIdOrderByFechaAsc(java.lang.String)}.
   */
  public List<pago> findByPedidoIdOrderByFechaAsc(String pedidoId) {
    class ExpressionMarker{};

    BasicQuery filterQuery = createQuery(ExpressionMarker.class.getEnclosingMethod(), "{\"pedidoId\": ?0}", pedidoId);
    Document sort = parse("{\"fecha\": 1}");
    filterQuery.setSortObject(sort);

    ExecutableFindOperation.FindWithQuery<pago> finder = operations.query(pago.class);
    return finder.matching(filterQuery).all();
  }
}
