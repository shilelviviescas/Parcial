package com.pos_magistral.app.controladores;

import com.pos_magistral.app.repositorio.usuario_repositorio;
import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Bean definitions for {@link usuario_web}.
 */
@Generated
public class usuario_web__BeanDefinitions {
  /**
   * Get the bean instance supplier for 'usuario_web'.
   */
  private static BeanInstanceSupplier<usuario_web> getUsuariowebInstanceSupplier() {
    return BeanInstanceSupplier.<usuario_web>forConstructor(usuario_repositorio.class, PasswordEncoder.class)
            .withGenerator((registeredBean, args) -> new usuario_web(args.get(0), args.get(1)));
  }

  /**
   * Get the bean definition for 'usuario_web'.
   */
  public static BeanDefinition getUsuariowebBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(usuario_web.class);
    beanDefinition.setInstanceSupplier(getUsuariowebInstanceSupplier());
    return beanDefinition;
  }
}
