package com.pos_magistral.app.controladores;

import com.pos_magistral.app.repositorio.caja_repositorio;
import com.pos_magistral.app.repositorio.comida_repositorio;
import com.pos_magistral.app.repositorio.mesa_repositorio;
import com.pos_magistral.app.repositorio.pedido_repositorio;
import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link pedido_web}.
 */
@Generated
public class pedido_web__BeanDefinitions {
  /**
   * Get the bean instance supplier for 'pedido_web'.
   */
  private static BeanInstanceSupplier<pedido_web> getPedidowebInstanceSupplier() {
    return BeanInstanceSupplier.<pedido_web>forConstructor(pedido_repositorio.class, mesa_repositorio.class, comida_repositorio.class, caja_repositorio.class)
            .withGenerator((registeredBean, args) -> new pedido_web(args.get(0), args.get(1), args.get(2), args.get(3)));
  }

  /**
   * Get the bean definition for 'pedido_web'.
   */
  public static BeanDefinition getPedidowebBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(pedido_web.class);
    beanDefinition.setInstanceSupplier(getPedidowebInstanceSupplier());
    return beanDefinition;
  }
}
