package com.jdc.fruverinari.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class ProductoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String descripcion;
    private Double precio;
    private Integer cantidad;

    // 🔑 CAMPO FALTANTE: Agregado para mapear el SELECT del formulario HTML
    private String unidadMedida;

    // 🚀 STOCK MÍNIMO PARA ALERTAS
    @Column(name = "stock_minimo", nullable = false)
    private Integer stockMinimo = 0; // Valor por defecto

    private boolean activo = true; // Por defecto activo

    private LocalDateTime fechaIngreso;

    @PrePersist
    protected void onCreate() {
        fechaIngreso = LocalDateTime.now();
    }

    // ==========================================================
    //                   GETTERS Y SETTERS
    // ==========================================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    // 🔑 GETTER Y SETTER AGREGADOS PARA unidadMedida
    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    // 🚀 GETTER Y SETTER PARA stockMinimo
    public Integer getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(Integer stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public LocalDateTime getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDateTime fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    // Método correcto para tu ReporteService (Usando el campo id)
    public Long getIdProducto() {
        return id;
    }
}