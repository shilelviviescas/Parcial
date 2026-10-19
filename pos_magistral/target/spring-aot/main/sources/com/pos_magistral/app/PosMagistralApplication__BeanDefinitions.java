package com.pos_magistral.app;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link PosMagistralApplication}.
 */
@Generated
public class PosMagistralApplication__BeanDefinitions {
  /**
   * Get the bean definition for 'posMagistralApplication'.
   */
  public static BeanDefinition getPosMagistralApplicationBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(PosMagistralApplication.class);
    beanDefinition.setInstanceSupplier(PosMagistralApplication::new);
    return beanDefinition;
  }
}
