package com.pos_magistral.app.configuracion;

import com.pos_magistral.app.entidades.comida;
import com.pos_magistral.app.entidades.mesa;
import com.pos_magistral.app.repositorio.comida_repositorio;
import com.pos_magistral.app.repositorio.mesa_repositorio;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class inicializador_datos implements CommandLineRunner {

    private final mesa_repositorio mesaRepositorio;
    private final comida_repositorio comidaRepositorio;

    public inicializador_datos(mesa_repositorio mesaRepositorio, comida_repositorio comidaRepositorio) {
        this.mesaRepositorio = mesaRepositorio;
        this.comidaRepositorio = comidaRepositorio;
    }

    @Override
    public void run(String... args) {

        // 15 mesas de 4 puestos cada una
        if (mesaRepositorio.count() == 0) {
            for (int i = 1; i <= 15; i++) {
                mesaRepositorio.save(new mesa(i, 4));
            }
        }

        if (comidaRepositorio.count() == 0) {
            comidaRepositorio.saveAll(List.of(
                new comida("Hamburguesa Clásica", "Carne de res, queso, lechuga, tomate y salsas de la casa.", 18900.0, comida.CAT_HAMBURGUESA),
                new comida("Hamburguesa Doble Carne", "Doble carne, doble queso, cebolla caramelizada y pepinillos.", 26900.0, comida.CAT_HAMBURGUESA),
                new comida("Hamburguesa BBQ Bacon", "Carne, tocineta crocante, queso cheddar y salsa BBQ.", 24900.0, comida.CAT_HAMBURGUESA),
                new comida("Hamburguesa Pollo Crispy", "Pollo apanado, lechuga, queso y mayonesa de ajo.", 21900.0, comida.CAT_HAMBURGUESA),

                new comida("Salchipapa Clásica", "Papa a la francesa, salchicha, queso y salsas.", 15900.0, comida.CAT_SALCHIPAPA),
                new comida("Salchipapa Mixta", "Salchicha, pollo desmechado, chorizo, queso y salsas.", 22900.0, comida.CAT_SALCHIPAPA),
                new comida("Salchipapa Magistral", "Papa, salchicha, carne, huevo de codorniz, queso gratinado y tocineta.", 28900.0, comida.CAT_SALCHIPAPA),

                new comida("Gaseosa", "Botella personal 400 ml.", 4500.0, comida.CAT_BEBIDA),
                new comida("Limonada Natural", "Limón fresco, hielo y un toque de azúcar.", 6500.0, comida.CAT_BEBIDA),
                new comida("Jugo Natural", "Fruta de temporada en agua o leche.", 7500.0, comida.CAT_BEBIDA),
                new comida("Agua", "Botella 600 ml.", 3000.0, comida.CAT_BEBIDA)
            ));
        }
    }
}