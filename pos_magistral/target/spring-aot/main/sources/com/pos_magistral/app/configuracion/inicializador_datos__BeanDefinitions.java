package com.pos_magistral.app.configuracion;

import com.pos_magistral.app.repositorio.comida_repositorio;
import com.pos_magistral.app.repositorio.mesa_repositorio;
import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link inicializador_datos}.
 */
@Generated
public class inicializador_datos__BeanDefinitions {
  /**
   * Get the bean instance supplier for 'inicializador_datos'.
   */
  private static BeanInstanceSupplier<inicializador_datos> getInicializadordatosInstanceSupplier() {
    return BeanInstanceSupplier.<inicializador_datos>forConstructor(mesa_repositorio.class, comida_repositorio.class)
            .withGenerator((registeredBean, args) -> new inicializador_datos(args.get(0), args.get(1)));
  }

  /**
   * Get the bean definition for 'inicializador_datos'.
   */
  public static BeanDefinition getInicializadordatosBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(inicializador_datos.class);
    beanDefinition.setInstanceSupplier(getInicializadordatosInstanceSupplier());
    return beanDefinition;
  }
}
