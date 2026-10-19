package com.pos_magistral.app.repositorio;

import com.pos_magistral.app.entidades.pedido;
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
 * AOT generated MongoDB repository implementation for {@link pedido_repositorio}.
 */
@Generated
public class pedido_repositorioImpl__AotRepository extends MongoAotRepositoryFragmentSupport {
  private final MongoOperations operations;

  public pedido_repositorioImpl__AotRepository(MongoOperations operations,
      RepositoryFactoryBeanSupport.FragmentCreationContext context) {
    super(operations, context);
    this.operations = operations;
  }

  /**
   * AOT generated implementation of {@link pedido_repositorio#findAllByOrderByFechaDesc()}.
   */
  public List<pedido> findAllByOrderByFechaDesc() {

    BasicQuery filterQuery = new BasicQuery(new Document());
    Document sort = parse("{\"fecha\": -1}");
    filterQuery.setSortObject(sort);

    ExecutableFindOperation.FindWithQuery<pedido> finder = operations.query(pedido.class);
    return finder.matching(filterQuery).all();
  }

  /**
   * AOT generated implementation of {@link pedido_repositorio#findByClienteIdOrderByFechaDesc(java.lang.String)}.
   */
  public List<pedido> findByClienteIdOrderByFechaDesc(String clienteId) {
    class ExpressionMarker{};

    BasicQuery filterQuery = createQuery(ExpressionMarker.class.getEnclosingMethod(), "{\"clienteId\": ?0}", clienteId);
    Document sort = parse("{\"fecha\": -1}");
    filterQuery.setSortObject(sort);

    ExecutableFindOperation.FindWithQuery<pedido> finder = operations.query(pedido.class);
    return finder.matching(filterQuery).all();
  }

  /**
   * AOT generated implementation of {@link pedido_repositorio#findByEstadoOrderByFechaAsc(java.lang.String)}.
   */
  public List<pedido> findByEstadoOrderByFechaAsc(String estado) {
    class ExpressionMarker{};

    BasicQuery filterQuery = createQuery(ExpressionMarker.class.getEnclosingMethod(), "{\"estado\": ?0}", estado);
    Document sort = parse("{\"fecha\": 1}");
    filterQuery.setSortObject(sort);

    ExecutableFindOperation.FindWithQuery<pedido> finder = operations.query(pedido.class);
    return finder.matching(filterQuery).all();
  }

  /**
   * AOT generated implementation of {@link pedido_repositorio#findByMesaId(java.lang.String)}.
   */
  public List<pedido> findByMesaId(String mesaId) {
    class ExpressionMarker{};

    BasicQuery filterQuery = createQuery(ExpressionMarker.class.getEnclosingMethod(), "{\"mesaId\": ?0}", mesaId);

    ExecutableFindOperation.FindWithQuery<pedido> finder = operations.query(pedido.class);
    return finder.matching(filterQuery).all();
  }

  /**
   * AOT generated implementation of {@link pedido_repositorio#findTopByOrderByNumeroOrdenDesc()}.
   */
  public Optional<pedido> findTopByOrderByNumeroOrdenDesc() {

    BasicQuery filterQuery = new BasicQuery(new Document());
    Document sort = parse("{\"numeroOrden\": -1}");
    filterQuery.setSortObject(sort);
    filterQuery.limit(1);

    ExecutableFindOperation.FindWithQuery<pedido> finder = operations.query(pedido.class);
    return finder.matching(filterQuery).first();
  }
}
