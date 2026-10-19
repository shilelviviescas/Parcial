package com.pos_magistral.app.entidades;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "pagos")
public class pago {

    public static final String EFECTIVO = "EFECTIVO";
    public static final String TARJETA = "TARJETA";
    public static final String TRANSFERENCIA = "TRANSFERENCIA";

    // Propina voluntaria: 10% del subtotal
    public static final double PORCENTAJE_PROPINA = 0.10;

    @Id
    private String id;
    private String pedidoId;
    private int numeroOrden;
    private String mesaId;
    private int numeroMesa;
    private String nombreCliente;
    private String cajaId;
    private String cajeroNombre;
    private String metodo = EFECTIVO;
    private List<Linea> lineas = new ArrayList<>();
    private Double subtotal = 0.0;
    private boolean conPropina = false;
    private Double propina = 0.0;
    private Double total = 0.0;
    private Double recibido = 0.0;
    private Double devuelto = 0.0;
    private boolean liberoMesa = false;
    private LocalDateTime fecha = LocalDateTime.now();

    public pago() {
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getPedidoId() { return pedidoId; }
    public void setPedidoId(String pedidoId) { this.pedidoId = pedidoId; }

    public int getNumeroOrden() { return numeroOrden; }
    public void setNumeroOrden(int numeroOrden) { this.numeroOrden = numeroOrden; }

    public String getMesaId() { return mesaId; }
    public void setMesaId(String mesaId) { this.mesaId = mesaId; }

    public int getNumeroMesa() { return numeroMesa; }
    public void setNumeroMesa(int numeroMesa) { this.numeroMesa = numeroMesa; }

    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }

    public String getCajaId() { return cajaId; }
    public void setCajaId(String cajaId) { this.cajaId = cajaId; }

    public String getCajeroNombre() { return cajeroNombre; }
    public void setCajeroNombre(String cajeroNombre) { this.cajeroNombre = cajeroNombre; }

    public String getMetodo() { return metodo; }
    public void setMetodo(String metodo) { this.metodo = metodo; }

    public List<Linea> getLineas() { return lineas; }
    public void setLineas(List<Linea> lineas) { this.lineas = lineas; }

    public Double getSubtotal() { return subtotal; }
    public void setSubtotal(Double subtotal) { this.subtotal = subtotal; }

    public boolean isConPropina() { return conPropina; }
    public void setConPropina(boolean conPropina) { this.conPropina = conPropina; }

    public Double getPropina() { return propina; }
    public void setPropina(Double propina) { this.propina = propina; }

    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }

    public Double getRecibido() { return recibido; }
    public void setRecibido(Double recibido) { this.recibido = recibido; }

    public Double getDevuelto() { return devuelto; }
    public void setDevuelto(Double devuelto) { this.devuelto = devuelto; }

    public boolean isLiberoMesa() { return liberoMesa; }
    public void setLiberoMesa(boolean liberoMesa) { this.liberoMesa = liberoMesa; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    // Producto que se cobró en este pago (queda guardado dentro del documento del pago)
    public static class Linea {

        private String comidaId;
        private String nombre;
        private Double precioUnitario;
        private int cantidad;
        private Double subtotal;

        public Linea() {
        }

        public Linea(String comidaId, String nombre, Double precioUnitario, int cantidad) {
            this.comidaId = comidaId;
            this.nombre = nombre;
            this.precioUnitario = precioUnitario;
            this.cantidad = cantidad;
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

        public Double getSubtotal() { return subtotal; }
        public void setSubtotal(Double subtotal) { this.subtotal = subtotal; }
    }
}