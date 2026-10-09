package com.jdc.fruverinari.model;

public record ProductosReceta(
        String f1, String f2, String f3,
        String v1, String v2, String v3,
        String hierba, String citrico
) {
    public String productosTexto() {
        return String.join(", ", f1, f2, v1, v2, v3).replaceAll(", $", "");
    }
}
