package com.pos_magistral.app.controladores;

import com.pos_magistral.app.entidades.comida;
import com.pos_magistral.app.repositorio.comida_repositorio;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
public class comida_web {

    private final comida_repositorio comidaRepositorio;

    public comida_web(comida_repositorio comidaRepositorio) {
        this.comidaRepositorio = comidaRepositorio;
    }

    // ===================== PÚBLICO (index) =====================

    @GetMapping("/")
    public String inicio(Model model) {

        List<comida> todas = comidaRepositorio.findAll();
        System.out.println("[MENU] platos encontrados en la base: " + todas.size());

        // Este orden va primero; las categorías nuevas se agregan al final
        Map<String, List<comida>> menu = new LinkedHashMap<>();
        menu.put(comida.CAT_HAMBURGUESA, new ArrayList<>());
        menu.put(comida.CAT_SALCHIPAPA, new ArrayList<>());
        menu.put(comida.CAT_BEBIDA, new ArrayList<>());

        for (comida plato : todas) {
            if (!plato.isDisponible()) {
                continue;
            }

            String categoria = "OTROS";
            if (plato.getCategoria() != null && !plato.getCategoria().isBlank()) {
                categoria = plato.getCategoria().trim().toUpperCase();
            }

            menu.computeIfAbsent(categoria, k -> new ArrayList<>()).add(plato);
        }

        // Si una categoría no tiene platos, no se muestra
        menu.entrySet().removeIf(e -> e.getValue().isEmpty());

        System.out.println("[MENU] categorías que se muestran: " + menu.keySet());

        model.addAttribute("menu", menu);
        return "index";
    }

    // ===================== MESERO =====================

    // ===================== CAJERO =====================

    // ===================== ADMIN =====================

    // ===================== CLIENTE =====================
}