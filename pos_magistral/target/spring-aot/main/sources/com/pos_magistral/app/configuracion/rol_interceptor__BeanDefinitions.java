package com.pos_magistral.app.configuracion;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link rol_interceptor}.
 */
@Generated
public class rol_interceptor__BeanDefinitions {
  /**
   * Get the bean definition for 'rol_interceptor'.
   */
  public static BeanDefinition getRolinterceptorBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(rol_interceptor.class);
    beanDefinition.setInstanceSupplier(rol_interceptor::new);
    return beanDefinition;
  }
}
