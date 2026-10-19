package com.pos_magistral.app.controladores;

import com.pos_magistral.app.repositorio.mesa_repositorio;
import com.pos_magistral.app.repositorio.pedido_repositorio;
import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link mesa_web}.
 */
@Generated
public class mesa_web__BeanDefinitions {
  /**
   * Get the bean instance supplier for 'mesa_web'.
   */
  private static BeanInstanceSupplier<mesa_web> getMesawebInstanceSupplier() {
    return BeanInstanceSupplier.<mesa_web>forConstructor(mesa_repositorio.class, pedido_repositorio.class)
            .withGenerator((registeredBean, args) -> new mesa_web(args.get(0), args.get(1)));
  }

  /**
   * Get the bean definition for 'mesa_web'.
   */
  public static BeanDefinition getMesawebBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(mesa_web.class);
    beanDefinition.setInstanceSupplier(getMesawebInstanceSupplier());
    return beanDefinition;
  }
}
