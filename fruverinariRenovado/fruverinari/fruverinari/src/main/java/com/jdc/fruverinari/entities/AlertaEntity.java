package com.jdc.fruverinari.entities;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
public class AlertaEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAlerta;

    @ManyToOne
    @JoinColumn(name = "idProducto")
    private ProductoEntity producto;

    private String mensaje;
    private Date fechaAlerta;
    private String estado; // Estado de la alerta (pendiente, resuelta)

    // Getters y Setters
    public Long getIdAlerta() {
        return idAlerta;
    }

    public void setIdAlerta(Long idAlerta) {
        this.idAlerta = idAlerta;
    }

    public ProductoEntity getProducto() {
        return producto;
    }

    public void setProducto(ProductoEntity producto) {
        this.producto = producto;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Date getFechaAlerta() {
        return fechaAlerta;
    }

    public void setFechaAlerta(Date fechaAlerta) {
        this.fechaAlerta = fechaAlerta;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
