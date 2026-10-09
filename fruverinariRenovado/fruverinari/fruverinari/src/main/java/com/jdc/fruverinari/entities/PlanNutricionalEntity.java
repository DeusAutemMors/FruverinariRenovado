package com.jdc.fruverinari.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "planes_nutricionales")
public class PlanNutricionalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombreCliente;

    @Column(nullable = false, length = 200)
    private String objetivo;

    @Column(length = 100)
    private String nivelActividad;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String contenidoPlan;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String productosSugeridos;

    private LocalDateTime fechaCreacion;

    @Column(length = 50)
    private String registradoPor;

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
    }

    public String getNivelActividad() {
        return nivelActividad;
    }

    public void setNivelActividad(String nivelActividad) {
        this.nivelActividad = nivelActividad;
    }

    public String getContenidoPlan() {
        return contenidoPlan;
    }

    public void setContenidoPlan(String contenidoPlan) {
        this.contenidoPlan = contenidoPlan;
    }

    public String getProductosSugeridos() {
        return productosSugeridos;
    }

    public void setProductosSugeridos(String productosSugeridos) {
        this.productosSugeridos = productosSugeridos;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getRegistradoPor() {
        return registradoPor;
    }

    public void setRegistradoPor(String registradoPor) {
        this.registradoPor = registradoPor;
    }
}
