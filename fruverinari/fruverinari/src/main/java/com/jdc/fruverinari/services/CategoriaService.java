package com.jdc.fruverinari.services;

import com.jdc.fruverinari.entities.CategoriaEntity;
import java.util.List;

public interface CategoriaService {

    List<CategoriaEntity> obtenerTodos();

    CategoriaEntity obtenerPorId(Long id);

    void guardar(CategoriaEntity categoria);

    void eliminar(Long id);
}
