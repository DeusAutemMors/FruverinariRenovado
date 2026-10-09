package com.jdc.fruverinari.model;

public class RecetaVista {

    private final String titulo;
    private final String momento;
    private final String descripcion;
    private final String estilos;
    private final boolean requiereCoccion;

    public RecetaVista(String titulo, String momento, String descripcion, String estilos, boolean requiereCoccion) {
        this.titulo = titulo;
        this.momento = momento;
        this.descripcion = descripcion;
        this.estilos = estilos;
        this.requiereCoccion = requiereCoccion;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getMomento() {
        return momento;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getEstilos() {
        return estilos;
    }

    public boolean isRequiereCoccion() {
        return requiereCoccion;
    }
}
