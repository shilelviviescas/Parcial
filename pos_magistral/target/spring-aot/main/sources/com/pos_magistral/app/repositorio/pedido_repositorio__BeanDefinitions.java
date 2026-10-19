package com.pos_magistral.app.repositorio;

import com.pos_magistral.app.entidades.pedido;
import java.lang.Class;
import java.lang.String;
import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.core.ResolvableType;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactoryBean;
import org.springframework.data.repository.core.support.RepositoryComposition;
import org.springframework.data.repository.core.support.RepositoryFactoryBeanSupport;
import org.springframework.data.repository.query.QueryLookupStrategy;

/**
 * Bean definitions for {@link pedido_repositorio}.
 */
@Generated
public class pedido_repositorio__BeanDefinitions {
  /**
   * Get the bean instance supplier for 'pedido_repositorio'.
   */
  private static BeanInstanceSupplier<MongoRepositoryFactoryBean> getPedidorepositorioInstanceSupplier(
      ) {
    return BeanInstanceSupplier.<MongoRepositoryFactoryBean>forConstructor(Class.class)
            .withGenerator((registeredBean, args) -> new MongoRepositoryFactoryBean(args.get(0)));
  }

  /**
   * Get the bean definition for 'pedido_repositorio'.
   */
  public static BeanDefinition getPedidorepositorioBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(MongoRepositoryFactoryBean.class);
    beanDefinition.setTargetType(ResolvableType.forClassWithGenerics(MongoRepositoryFactoryBean.class, pedido_repositorio.class, pedido.class, String.class));
    beanDefinition.setLazyInit(false);
    beanDefinition.getConstructorArgumentValues().addIndexedArgumentValue(0, "com.pos_magistral.app.repositorio.pedido_repositorio");
    beanDefinition.getPropertyValues().addPropertyValue("queryLookupStrategyKey", QueryLookupStrategy.Key.CREATE_IF_NOT_FOUND);
    beanDefinition.getPropertyValues().addPropertyValue("lazyInit", false);
    beanDefinition.getPropertyValues().addPropertyValue("namedQueries", new RuntimeBeanReference("mongo.named-queries#1"));
    beanDefinition.getPropertyValues().addPropertyValue("repositoryFragments", new RuntimeBeanReference("mongodb.pedido_repositorio.fragments#0"));
    beanDefinition.getPropertyValues().addPropertyValue("mongoOperations", new RuntimeBeanReference("mongoTemplate"));
    beanDefinition.getPropertyValues().addPropertyValue("createIndexesForQueryMethods", false);
    beanDefinition.getPropertyValues().addPropertyValue("repositoryFragmentsFunction", new RepositoryFactoryBeanSupport.RepositoryFragmentsFunction() {
      public RepositoryComposition.RepositoryFragments getRepositoryFragments(
          BeanFactory beanFactory, RepositoryFactoryBeanSupport.FragmentCreationContext context) {
        MongoOperations operations = beanFactory.getBean(MongoOperations.class);
        return RepositoryComposition.RepositoryFragments.just(new com.pos_magistral.app.repositorio.pedido_repositorioImpl__AotRepository(operations, context));
      }
    });
    beanDefinition.setInstanceSupplier(getPedidorepositorioInstanceSupplier());
    return beanDefinition;
  }
}
