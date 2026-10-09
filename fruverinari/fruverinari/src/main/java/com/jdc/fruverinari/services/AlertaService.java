package com.jdc.fruverinari.services;

import com.jdc.fruverinari.entities.AlertaEntity;
import com.jdc.fruverinari.entities.ProductoEntity;

import java.util.List;

public interface AlertaService {

    List<AlertaEntity> obtenerTodos();

    AlertaEntity obtenerPorId(Long id);

    void guardar(AlertaEntity alerta);

    void eliminar(Long id);

    List<ProductoEntity> obtenerProductosConBajoStock();



}
