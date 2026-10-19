package com.pos_magistral.app.entidades;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "cajas")
public class caja {

    public static final String ABIERTA = "ABIERTA";
    public static final String CERRADA = "CERRADA";

    @Id
    private String id;
    private String estado = ABIERTA;

    // Apertura
    private String cajeroId;
    private String cajeroNombre;
    private Double montoInicial = 0.0;
    private LocalDateTime fechaApertura = LocalDateTime.now();

    // Cierre (se llenan cuando se cierra la caja)
    private LocalDateTime fechaCierre;
    private int cantidadPagos = 0;
    private Double totalEfectivo = 0.0;
    private Double totalTarjeta = 0.0;
    private Double totalTransferencia = 0.0;
    private Double totalVentas = 0.0;
    private Double efectivoEsperado = 0.0;
    private Double efectivoContado = 0.0;
    private Double diferencia = 0.0;

    public caja() {
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getCajeroId() { return cajeroId; }
    public void setCajeroId(String cajeroId) { this.cajeroId = cajeroId; }

    public String getCajeroNombre() { return cajeroNombre; }
    public void setCajeroNombre(String cajeroNombre) { this.cajeroNombre = cajeroNombre; }

    public Double getMontoInicial() { return montoInicial; }
    public void setMontoInicial(Double montoInicial) { this.montoInicial = montoInicial; }

    public LocalDateTime getFechaApertura() { return fechaApertura; }
    public void setFechaApertura(LocalDateTime fechaApertura) { this.fechaApertura = fechaApertura; }

    public LocalDateTime getFechaCierre() { return fechaCierre; }
    public void setFechaCierre(LocalDateTime fechaCierre) { this.fechaCierre = fechaCierre; }

    public int getCantidadPagos() { return cantidadPagos; }
    public void setCantidadPagos(int cantidadPagos) { this.cantidadPagos = cantidadPagos; }

    public Double getTotalEfectivo() { return totalEfectivo; }
    public void setTotalEfectivo(Double totalEfectivo) { this.totalEfectivo = totalEfectivo; }

    public Double getTotalTarjeta() { return totalTarjeta; }
    public void setTotalTarjeta(Double totalTarjeta) { this.totalTarjeta = totalTarjeta; }

    public Double getTotalTransferencia() { return totalTransferencia; }
    public void setTotalTransferencia(Double totalTransferencia) { this.totalTransferencia = totalTransferencia; }

    public Double getTotalVentas() { return totalVentas; }
    public void setTotalVentas(Double totalVentas) { this.totalVentas = totalVentas; }

    public Double getEfectivoEsperado() { return efectivoEsperado; }
    public void setEfectivoEsperado(Double efectivoEsperado) { this.efectivoEsperado = efectivoEsperado; }

    public Double getEfectivoContado() { return efectivoContado; }
    public void setEfectivoContado(Double efectivoContado) { this.efectivoContado = efectivoContado; }

    public Double getDiferencia() { return diferencia; }
    public void setDiferencia(Double diferencia) { this.diferencia = diferencia; }
}