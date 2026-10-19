package com.pos_magistral.app.entidades;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "comidas")
public class comida {

    public static final String CAT_HAMBURGUESA = "HAMBURGUESA";
    public static final String CAT_SALCHIPAPA = "SALCHIPAPA";
    public static final String CAT_BEBIDA = "BEBIDA";

    @Id
    private String id;
    private String nombre;
    private String descripcion;
    private Double precio;
    private String categoria;
    private boolean disponible = true;

    public comida() {
    }

    public comida(String nombre, String descripcion, Double precio, String categoria) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.categoria = categoria;
        this.disponible = true;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { this.precio = precio; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }
}