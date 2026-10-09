package com.jdc.fruverinari.model;

import com.jdc.fruverinari.entities.ProductoEntity;

import java.util.ArrayList;
import java.util.List;

public class PlanNutricionalGenerado {

    private String nombreCliente;
    private String objetivo;
    private String objetivoEtiqueta;
    private String nivelActividad;
    private String estiloAlimentacion;
    private String preferenciaRecetas;
    private String intensidad;
    private String restriccionesTexto;
    private String resumen;
    private String consejos;
    private final List<ComidaSugerida> comidas = new ArrayList<>();
    private final List<DiaPlanSemanal> semana = new ArrayList<>();
    private final List<ProductoEntity> productosRecomendados = new ArrayList<>();

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

    public String getObjetivoEtiqueta() {
        return objetivoEtiqueta;
    }

    public void setObjetivoEtiqueta(String objetivoEtiqueta) {
        this.objetivoEtiqueta = objetivoEtiqueta;
    }

    public String getNivelActividad() {
        return nivelActividad;
    }

    public void setNivelActividad(String nivelActividad) {
        this.nivelActividad = nivelActividad;
    }

    public String getEstiloAlimentacion() {
        return estiloAlimentacion;
    }

    public void setEstiloAlimentacion(String estiloAlimentacion) {
        this.estiloAlimentacion = estiloAlimentacion;
    }

    public String getPreferenciaRecetas() {
        return preferenciaRecetas;
    }

    public void setPreferenciaRecetas(String preferenciaRecetas) {
        this.preferenciaRecetas = preferenciaRecetas;
    }

    public String getIntensidad() {
        return intensidad;
    }

    public void setIntensidad(String intensidad) {
        this.intensidad = intensidad;
    }

    public String getRestriccionesTexto() {
        return restriccionesTexto;
    }

    public void setRestriccionesTexto(String restriccionesTexto) {
        this.restriccionesTexto = restriccionesTexto;
    }

    public String getResumen() {
        return resumen;
    }

    public void setResumen(String resumen) {
        this.resumen = resumen;
    }

    public String getConsejos() {
        return consejos;
    }

    public void setConsejos(String consejos) {
        this.consejos = consejos;
    }

    public List<ComidaSugerida> getComidas() {
        return comidas;
    }

    public List<DiaPlanSemanal> getSemana() {
        return semana;
    }

    public List<ProductoEntity> getProductosRecomendados() {
        return productosRecomendados;
    }

    public String getContenidoParaGuardar() {
        StringBuilder sb = new StringBuilder();
        sb.append(resumen).append("\n\n");
        for (ComidaSugerida c : comidas) {
            sb.append(c.getMomento()).append(" - ").append(c.getTituloReceta()).append(": ")
                    .append(c.getDescripcion()).append(" (").append(c.getProductos()).append(")\n");
        }
        sb.append("\nConsejos: ").append(consejos);
        if (!semana.isEmpty()) {
            sb.append("\n\n--- Plan semanal ---\n");
            for (DiaPlanSemanal d : semana) {
                sb.append(d.getDia()).append(" | D: ").append(d.getDesayunoTitulo()).append(" - ")
                        .append(d.getDesayuno()).append(" | A: ").append(d.getAlmuerzoTitulo())
                        .append(" - ").append(d.getAlmuerzo()).append(" | C: ").append(d.getCenaTitulo())
                        .append(" - ").append(d.getCena()).append("\n");
            }
        }
        return sb.toString();
    }

    public String getProductosComoTexto() {
        return productosRecomendados.stream()
                .map(ProductoEntity::getNombre)
                .reduce((a, b) -> a + ", " + b)
                .orElse("Sin productos en inventario");
    }
}
