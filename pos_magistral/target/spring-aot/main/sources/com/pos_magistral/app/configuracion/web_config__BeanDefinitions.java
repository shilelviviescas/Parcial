package com.pos_magistral.app.configuracion;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ConfigurationClassUtils;

/**
 * Bean definitions for {@link web_config}.
 */
@Generated
public class web_config__BeanDefinitions {
  /**
   * Get the bean instance supplier for 'web_config'.
   */
  private static BeanInstanceSupplier<web_config> getWebconfigInstanceSupplier() {
    return BeanInstanceSupplier.<web_config>forConstructor(rol_interceptor.class)
            .withGenerator((registeredBean, args) -> new web_config$$SpringCGLIB$$0(args.get(0)));
  }

  /**
   * Get the bean definition for 'web_config'.
   */
  public static BeanDefinition getWebconfigBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(web_config.class);
    beanDefinition.setTargetType(web_config.class);
    ConfigurationClassUtils.initializeConfigurationClass(web_config.class);
    beanDefinition.setInstanceSupplier(getWebconfigInstanceSupplier());
    return beanDefinition;
  }
}
