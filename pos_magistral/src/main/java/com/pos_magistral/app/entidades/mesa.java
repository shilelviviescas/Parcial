package com.pos_magistral.app.entidades;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "mesas")
public class mesa {

    public static final String LIBRE = "LIBRE";
    public static final String OCUPADA = "OCUPADA";

    @Id
    private String id;
    private int numero;
    private int capacidad = 4;
    private String estado = LIBRE;
    private String pedidoActualId;

    public mesa() {
    }

    public mesa(int numero) {
        this.numero = numero;
        this.capacidad = 4;
        this.estado = LIBRE;
    }

    public mesa(int numero, int capacidad) {
        this.numero = numero;
        this.capacidad = capacidad;
        this.estado = LIBRE;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public int getNumero() { return numero; }
    public void setNumero(int numero) { this.numero = numero; }

    public int getCapacidad() { return capacidad; }
    public void setCapacidad(int capacidad) { this.capacidad = capacidad; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getPedidoActualId() { return pedidoActualId; }
    public void setPedidoActualId(String pedidoActualId) { this.pedidoActualId = pedidoActualId; }
}