package com.pos_magistral.app.entidades;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "usuarios")
public class usuario {

    public static final String ROL_ADMIN = "ADMIN";
    public static final String ROL_MESERO = "MESERO";
    public static final String ROL_CAJERO = "CAJERO";
    public static final String ROL_CLIENTE = "CLIENTE";

    @Id
    private String id;

    private String nombre;
    private String correo;
    private String clave;
    private String rol;

    // Datos para la factura
    private String documento;
    private String telefono;
    private String direccion;

    public usuario() {
    }

    public usuario(String nombre, String correo, String clave, String rol) {
        this.nombre = nombre;
        this.correo = correo;
        this.clave = clave;
        this.rol = rol;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getClave() { return clave; }
    public void setClave(String clave) { this.clave = clave; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public String getDocumento() { return documento; }
    public void setDocumento(String documento) { this.documento = documento; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
}