package com.pos_magistral.app.repositorio;

import com.pos_magistral.app.entidades.comida;
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
 * Bean definitions for {@link comida_repositorio}.
 */
@Generated
public class comida_repositorio__BeanDefinitions {
  /**
   * Get the bean instance supplier for 'comida_repositorio'.
   */
  private static BeanInstanceSupplier<MongoRepositoryFactoryBean> getComidarepositorioInstanceSupplier(
      ) {
    return BeanInstanceSupplier.<MongoRepositoryFactoryBean>forConstructor(Class.class)
            .withGenerator((registeredBean, args) -> new MongoRepositoryFactoryBean(args.get(0)));
  }

  /**
   * Get the bean definition for 'comida_repositorio'.
   */
  public static BeanDefinition getComidarepositorioBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(MongoRepositoryFactoryBean.class);
    beanDefinition.setTargetType(ResolvableType.forClassWithGenerics(MongoRepositoryFactoryBean.class, comida_repositorio.class, comida.class, String.class));
    beanDefinition.setLazyInit(false);
    beanDefinition.getConstructorArgumentValues().addIndexedArgumentValue(0, "com.pos_magistral.app.repositorio.comida_repositorio");
    beanDefinition.getPropertyValues().addPropertyValue("queryLookupStrategyKey", QueryLookupStrategy.Key.CREATE_IF_NOT_FOUND);
    beanDefinition.getPropertyValues().addPropertyValue("lazyInit", false);
    beanDefinition.getPropertyValues().addPropertyValue("namedQueries", new RuntimeBeanReference("mongo.named-queries#4"));
    beanDefinition.getPropertyValues().addPropertyValue("repositoryFragments", new RuntimeBeanReference("mongodb.comida_repositorio.fragments#0"));
    beanDefinition.getPropertyValues().addPropertyValue("mongoOperations", new RuntimeBeanReference("mongoTemplate"));
    beanDefinition.getPropertyValues().addPropertyValue("createIndexesForQueryMethods", false);
    beanDefinition.getPropertyValues().addPropertyValue("repositoryFragmentsFunction", new RepositoryFactoryBeanSupport.RepositoryFragmentsFunction() {
      public RepositoryComposition.RepositoryFragments getRepositoryFragments(
          BeanFactory beanFactory, RepositoryFactoryBeanSupport.FragmentCreationContext context) {
        MongoOperations operations = beanFactory.getBean(MongoOperations.class);
        return RepositoryComposition.RepositoryFragments.just(new com.pos_magistral.app.repositorio.comida_repositorioImpl__AotRepository(operations, context));
      }
    });
    beanDefinition.setInstanceSupplier(getComidarepositorioInstanceSupplier());
    return beanDefinition;
  }
}
