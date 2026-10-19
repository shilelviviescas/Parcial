package com.pos_magistral.app.controladores;

import com.pos_magistral.app.repositorio.caja_repositorio;
import com.pos_magistral.app.repositorio.mesa_repositorio;
import com.pos_magistral.app.repositorio.pago_repositorio;
import com.pos_magistral.app.repositorio.pedido_repositorio;
import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link pago_web}.
 */
@Generated
public class pago_web__BeanDefinitions {
  /**
   * Get the bean instance supplier for 'pago_web'.
   */
  private static BeanInstanceSupplier<pago_web> getPagowebInstanceSupplier() {
    return BeanInstanceSupplier.<pago_web>forConstructor(pago_repositorio.class, pedido_repositorio.class, caja_repositorio.class, mesa_repositorio.class)
            .withGenerator((registeredBean, args) -> new pago_web(args.get(0), args.get(1), args.get(2), args.get(3)));
  }

  /**
   * Get the bean definition for 'pago_web'.
   */
  public static BeanDefinition getPagowebBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(pago_web.class);
    beanDefinition.setInstanceSupplier(getPagowebInstanceSupplier());
    return beanDefinition;
  }
}
