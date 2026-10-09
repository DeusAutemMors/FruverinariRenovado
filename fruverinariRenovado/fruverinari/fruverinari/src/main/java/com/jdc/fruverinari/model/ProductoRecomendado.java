package com.jdc.fruverinari.model;

import com.jdc.fruverinari.entities.ProductoEntity;

public class ProductoRecomendado {

    private final ProductoEntity producto;
    private final int unidadesVendidas;
    private final String motivo;

    public ProductoRecomendado(ProductoEntity producto, int unidadesVendidas, String motivo) {
        this.producto = producto;
        this.unidadesVendidas = unidadesVendidas;
        this.motivo = motivo;
    }

    public ProductoEntity getProducto() {
        return producto;
    }

    public int getUnidadesVendidas() {
        return unidadesVendidas;
    }

    public String getMotivo() {
        return motivo;
    }
}
