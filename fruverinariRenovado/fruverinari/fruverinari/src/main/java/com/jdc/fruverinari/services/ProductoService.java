package com.jdc.fruverinari.services;

import com.jdc.fruverinari.entities.ProductoEntity;
import java.util.List;

public interface ProductoService {

    List<ProductoEntity> obtenerTodos();

    ProductoEntity obtenerPorId(Long id);

    void guardar(ProductoEntity producto);

    void suspender(Long id); // Metodo para suspender

    /**
     * 🚀 NUEVA FUNCIÓN DE BÚSQUEDA ROBUSTA (Busca por Nombre O Descripción)
     * Usaremos este método para el filtro en la vista /productos.
     * @param query Termino a buscar.
     * @return Lista de ProductoEntity que coinciden.
     */
    List<ProductoEntity> buscarPorNombreODescripcion(String query);

}