package com.pos_magistral.app.configuracion;

import com.pos_magistral.app.entidades.usuario;
import com.pos_magistral.app.repositorio.usuario_repositorio;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class ConfiguracionInicial {

    @Bean
    public PasswordEncoder codificadorDeClaves() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CommandLineRunner crearUsuariosIniciales(usuario_repositorio repositorio, PasswordEncoder codificador) {
        return args -> {
            crear(repositorio, codificador, "Administrador", "admin@magistral.com", "Admin123", usuario.ROL_ADMIN);
            crear(repositorio, codificador, "Mesero de prueba", "mesero@magistral.com", "Mesero123", usuario.ROL_MESERO);
            crear(repositorio, codificador, "Cajero de prueba", "cajero@magistral.com", "Cajero123", usuario.ROL_CAJERO);
            crear(repositorio, codificador, "Cliente de prueba", "cliente@magistral.com", "Cliente123", usuario.ROL_CLIENTE);
        };
    }

    private void crear(usuario_repositorio repositorio, PasswordEncoder codificador,
                       String nombre, String correo, String clave, String rol) {
        if (!repositorio.existsByCorreo(correo)) {
            repositorio.save(new usuario(nombre, correo, codificador.encode(clave), rol));
        }
    }
}