package com.jdc.fruverinari.model;

import java.util.Set;

public class RecetaPlantilla {

    private final String titulo;
    private final String descripcion;
    private final String momento;
    private final Set<String> objetivos;
    private final Set<String> estilos;
    private final Set<String> preferencias;
    private final boolean requiereCoccion;

    public RecetaPlantilla(String titulo, String descripcion, String momento,
                           Set<String> objetivos, Set<String> estilos,
                           Set<String> preferencias, boolean requiereCoccion) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.momento = momento;
        this.objetivos = objetivos;
        this.estilos = estilos;
        this.preferencias = preferencias;
        this.requiereCoccion = requiereCoccion;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getMomento() {
        return momento;
    }

    public Set<String> getObjetivos() {
        return objetivos;
    }

    public Set<String> getEstilos() {
        return estilos;
    }

    public Set<String> getPreferencias() {
        return preferencias;
    }

    public boolean isRequiereCoccion() {
        return requiereCoccion;
    }

    public String aplicar(ProductosReceta p) {
        return descripcion
                .replace("{f1}", p.f1()).replace("{f2}", p.f2()).replace("{f3}", p.f3())
                .replace("{v1}", p.v1()).replace("{v2}", p.v2()).replace("{v3}", p.v3())
                .replace("{h}", p.hierba()).replace("{c}", p.citrico());
    }
}
