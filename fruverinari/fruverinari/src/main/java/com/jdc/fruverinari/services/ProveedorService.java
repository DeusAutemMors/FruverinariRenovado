package com.jdc.fruverinari.services;

import com.jdc.fruverinari.entities.ProveedorEntity;
import java.util.List;

public interface ProveedorService {

    List<ProveedorEntity> obtenerTodos();

    ProveedorEntity obtenerPorId(Long id);

    void guardar(ProveedorEntity proveedor);

    void suspender(Long id); // Metodo para suspender un proveedor
}
