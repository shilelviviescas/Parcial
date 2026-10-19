package com.pos_magistral.app.configuracion;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ConfigurationClassUtils;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Bean definitions for {@link SeguridadConfig}.
 */
@Generated
public class SeguridadConfig__BeanDefinitions {
  /**
   * Get the bean definition for 'seguridadConfig'.
   */
  public static BeanDefinition getSeguridadConfigBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(SeguridadConfig.class);
    beanDefinition.setTargetType(SeguridadConfig.class);
    ConfigurationClassUtils.initializeConfigurationClass(SeguridadConfig.class);
    beanDefinition.setInstanceSupplier(SeguridadConfig$$SpringCGLIB$$0::new);
    return beanDefinition;
  }

  /**
   * Get the bean instance supplier for 'filterChain'.
   */
  private static BeanInstanceSupplier<SecurityFilterChain> getFilterChainInstanceSupplier() {
    return BeanInstanceSupplier.<SecurityFilterChain>forFactoryMethod(SeguridadConfig$$SpringCGLIB$$0.class, "filterChain", HttpSecurity.class)
            .withGenerator((registeredBean, args) -> registeredBean.getBeanFactory().getBean("seguridadConfig", SeguridadConfig.class).filterChain(args.get(0)));
  }

  /**
   * Get the bean definition for 'filterChain'.
   */
  public static BeanDefinition getFilterChainBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(SecurityFilterChain.class);
    beanDefinition.setFactoryBeanName("seguridadConfig");
    beanDefinition.setInstanceSupplier(getFilterChainInstanceSupplier());
    return beanDefinition;
  }
}
