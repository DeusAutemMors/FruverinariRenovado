package com.jdc.fruverinari.model;

public class ComidaSugerida {

    private final String momento;
    private final String tituloReceta;
    private final String descripcion;
    private final String productos;

    public ComidaSugerida(String momento, String tituloReceta, String descripcion, String productos) {
        this.momento = momento;
        this.tituloReceta = tituloReceta;
        this.descripcion = descripcion;
        this.productos = productos;
    }

    public String getMomento() {
        return momento;
    }

    public String getTituloReceta() {
        return tituloReceta;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getProductos() {
        return productos;
    }
}
