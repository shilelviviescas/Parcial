package com.pos_magistral.app.controladores;

import com.pos_magistral.app.repositorio.comida_repositorio;
import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link comida_web}.
 */
@Generated
public class comida_web__BeanDefinitions {
  /**
   * Get the bean instance supplier for 'comida_web'.
   */
  private static BeanInstanceSupplier<comida_web> getComidawebInstanceSupplier() {
    return BeanInstanceSupplier.<comida_web>forConstructor(comida_repositorio.class)
            .withGenerator((registeredBean, args) -> new comida_web(args.get(0)));
  }

  /**
   * Get the bean definition for 'comida_web'.
   */
  public static BeanDefinition getComidawebBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(comida_web.class);
    beanDefinition.setInstanceSupplier(getComidawebInstanceSupplier());
    return beanDefinition;
  }
}
