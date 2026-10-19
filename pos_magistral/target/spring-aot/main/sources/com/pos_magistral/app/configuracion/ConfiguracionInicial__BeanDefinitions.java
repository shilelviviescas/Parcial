package com.pos_magistral.app.configuracion;

import com.pos_magistral.app.repositorio.usuario_repositorio;
import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.ConfigurationClassUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Bean definitions for {@link ConfiguracionInicial}.
 */
@Generated
public class ConfiguracionInicial__BeanDefinitions {
  /**
   * Get the bean definition for 'configuracionInicial'.
   */
  public static BeanDefinition getConfiguracionInicialBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(ConfiguracionInicial.class);
    beanDefinition.setTargetType(ConfiguracionInicial.class);
    ConfigurationClassUtils.initializeConfigurationClass(ConfiguracionInicial.class);
    beanDefinition.setInstanceSupplier(ConfiguracionInicial$$SpringCGLIB$$0::new);
    return beanDefinition;
  }

  /**
   * Get the bean instance supplier for 'codificadorDeClaves'.
   */
  private static BeanInstanceSupplier<PasswordEncoder> getCodificadorDeClavesInstanceSupplier() {
    return BeanInstanceSupplier.<PasswordEncoder>forFactoryMethod(ConfiguracionInicial$$SpringCGLIB$$0.class, "codificadorDeClaves")
            .withGenerator((registeredBean) -> registeredBean.getBeanFactory().getBean("configuracionInicial", ConfiguracionInicial.class).codificadorDeClaves());
  }

  /**
   * Get the bean definition for 'codificadorDeClaves'.
   */
  public static BeanDefinition getCodificadorDeClavesBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(PasswordEncoder.class);
    beanDefinition.setFactoryBeanName("configuracionInicial");
    beanDefinition.setInstanceSupplier(getCodificadorDeClavesInstanceSupplier());
    return beanDefinition;
  }

  /**
   * Get the bean instance supplier for 'crearUsuariosIniciales'.
   */
  private static BeanInstanceSupplier<CommandLineRunner> getCrearUsuariosInicialesInstanceSupplier(
      ) {
    return BeanInstanceSupplier.<CommandLineRunner>forFactoryMethod(ConfiguracionInicial$$SpringCGLIB$$0.class, "crearUsuariosIniciales", usuario_repositorio.class, PasswordEncoder.class)
            .withGenerator((registeredBean, args) -> registeredBean.getBeanFactory().getBean("configuracionInicial", ConfiguracionInicial.class).crearUsuariosIniciales(args.get(0), args.get(1)));
  }

  /**
   * Get the bean definition for 'crearUsuariosIniciales'.
   */
  public static BeanDefinition getCrearUsuariosInicialesBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(CommandLineRunner.class);
    beanDefinition.setFactoryBeanName("configuracionInicial");
    beanDefinition.setInstanceSupplier(getCrearUsuariosInicialesInstanceSupplier());
    return beanDefinition;
  }
}
