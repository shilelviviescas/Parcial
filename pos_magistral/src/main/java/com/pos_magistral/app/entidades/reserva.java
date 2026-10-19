package com.pos_magistral.app.entidades;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Document(collection = "reservas")
public class reserva {

    public static final String PENDIENTE = "PENDIENTE";
    public static final String CONFIRMADA = "CONFIRMADA";
    public static final String CANCELADA = "CANCELADA";

    // Ocasiones que el cliente puede elegir cuando pide decoración
    public static final List<String> OCASIONES = List.of("Cumpleaños", "Aniversario", "Reunión", "Otra");

    @Id
    private String id;

    // Quién reserva
    private String clienteId;
    private String nombreCliente;

    // Cuándo y para cuántas personas
    private LocalDate fecha;
    private LocalTime hora;
    private int personas = 1;

    // Decoración (la ocasión solo se guarda si pidió decoración)
    private boolean decoracion = false;
    private String ocasion;

    private String estado = PENDIENTE;
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    public reserva() {
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getClienteId() { return clienteId; }
    public void setClienteId(String clienteId) { this.clienteId = clienteId; }

    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public LocalTime getHora() { return hora; }
    public void setHora(LocalTime hora) { this.hora = hora; }

    public int getPersonas() { return personas; }
    public void setPersonas(int personas) { this.personas = personas; }

    public boolean isDecoracion() { return decoracion; }
    public void setDecoracion(boolean decoracion) { this.decoracion = decoracion; }

    public String getOcasion() { return ocasion; }
    public void setOcasion(String ocasion) { this.ocasion = ocasion; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}