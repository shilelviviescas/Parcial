package com.pos_magistral.app.controladores;

import com.pos_magistral.app.repositorio.caja_repositorio;
import com.pos_magistral.app.repositorio.pago_repositorio;
import com.pos_magistral.app.repositorio.pedido_repositorio;
import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link caja_web}.
 */
@Generated
public class caja_web__BeanDefinitions {
  /**
   * Get the bean instance supplier for 'caja_web'.
   */
  private static BeanInstanceSupplier<caja_web> getCajawebInstanceSupplier() {
    return BeanInstanceSupplier.<caja_web>forConstructor(caja_repositorio.class, pago_repositorio.class, pedido_repositorio.class)
            .withGenerator((registeredBean, args) -> new caja_web(args.get(0), args.get(1), args.get(2)));
  }

  /**
   * Get the bean definition for 'caja_web'.
   */
  public static BeanDefinition getCajawebBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(caja_web.class);
    beanDefinition.setInstanceSupplier(getCajawebInstanceSupplier());
    return beanDefinition;
  }
}
