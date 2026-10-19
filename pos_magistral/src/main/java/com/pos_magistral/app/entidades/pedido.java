package com.pos_magistral.app.entidades;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "pedidos")
public class pedido {

    public static final String TOMADO = "TOMADO";
    public static final String PAGADO = "PAGADO";
    public static final String CANCELADO = "CANCELADO";
    public static final String FINALIZADO = "FINALIZADO"; // solo por pedidos viejos

    // Tipo de pedido: en una mesa del restaurante o a domicilio por la página
    public static final String TIPO_MESA = "MESA";
    public static final String TIPO_DOMICILIO = "DOMICILIO";

    @Id
    private String id;
    private int numeroOrden;
    private String tipo = TIPO_MESA;

    // Pedidos de mesa (en domicilio quedan vacíos: mesaId null y numeroMesa 0)
    private String mesaId;
    private int numeroMesa;

    private String nombreCliente;

    // Pedidos a domicilio
    private String clienteId;
    private String nombreRecibe;
    private String telefono;
    private String direccion;
    private String metodoPagoElegido; // EFECTIVO, TARJETA o TRANSFERENCIA (lo escoge el cliente al pedir)

    private List<Item> items = new ArrayList<>();
    private Double total = 0.0;
    private String estado = TOMADO;
    private LocalDateTime fecha = LocalDateTime.now();

    public pedido() {
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public int getNumeroOrden() { return numeroOrden; }
    public void setNumeroOrden(int numeroOrden) { this.numeroOrden = numeroOrden; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getMesaId() { return mesaId; }
    public void setMesaId(String mesaId) { this.mesaId = mesaId; }

    public int getNumeroMesa() { return numeroMesa; }
    public void setNumeroMesa(int numeroMesa) { this.numeroMesa = numeroMesa; }

    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }

    public String getClienteId() { return clienteId; }
    public void setClienteId(String clienteId) { this.clienteId = clienteId; }

    public String getNombreRecibe() { return nombreRecibe; }
    public void setNombreRecibe(String nombreRecibe) { this.nombreRecibe = nombreRecibe; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getMetodoPagoElegido() { return metodoPagoElegido; }
    public void setMetodoPagoElegido(String metodoPagoElegido) { this.metodoPagoElegido = metodoPagoElegido; }

    public List<Item> getItems() { return items; }
    public void setItems(List<Item> items) { this.items = items; }

    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    // ¿Es un pedido a domicilio? (los pedidos viejos no tienen tipo y cuentan como de mesa)
    public boolean isDomicilio() {
        return TIPO_DOMICILIO.equals(tipo);
    }

    // Lo que todavía falta por cobrar (se calcula, no se guarda en la base)
    public double getTotalPendiente() {
        double pendiente = 0;
        for (Item linea : items) {
            pendiente += linea.getSubtotalPendiente();
        }
        return pendiente;
    }

    // Línea del pedido (va guardada dentro del documento del pedido)
    public static class Item {

        private String comidaId;
        private String nombre;
        private Double precioUnitario;
        private int cantidad;
        private int cantidadPagada = 0;
        private Double subtotal;

        public Item() {
        }

        public Item(String comidaId, String nombre, Double precioUnitario, int cantidad) {
            this.comidaId = comidaId;
            this.nombre = nombre;
            this.precioUnitario = precioUnitario;
            this.cantidad = cantidad;
            this.cantidadPagada = 0;
            this.subtotal = precioUnitario * cantidad;
        }

        public String getComidaId() { return comidaId; }
        public void setComidaId(String comidaId) { this.comidaId = comidaId; }

        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }

        public Double getPrecioUnitario() { return precioUnitario; }
        public void setPrecioUnitario(Double precioUnitario) { this.precioUnitario = precioUnitario; }

        public int getCantidad() { return cantidad; }
        public void setCantidad(int cantidad) { this.cantidad = cantidad; }

        public int getCantidadPagada() { return cantidadPagada; }
        public void setCantidadPagada(int cantidadPagada) { this.cantidadPagada = cantidadPagada; }

        public Double getSubtotal() { return subtotal; }
        public void setSubtotal(Double subtotal) { this.subtotal = subtotal; }

        // Unidades de esta línea que todavía no se han pagado
        public int getCantidadPendiente() {
            return cantidad - cantidadPagada;
        }

        // Lo que falta por cobrar de esta línea
        public double getSubtotalPendiente() {
            return precioUnitario * getCantidadPendiente();
        }
    }
}