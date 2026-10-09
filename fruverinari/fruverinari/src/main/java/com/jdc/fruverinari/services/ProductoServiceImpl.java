package com.jdc.fruverinari.services;

import com.jdc.fruverinari.entities.ProductoEntity;
import com.jdc.fruverinari.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Override
    public List<ProductoEntity> obtenerTodos() {
        return productoRepository.findAll();
    }

    @Override
    public ProductoEntity obtenerPorId(Long id) {
        return productoRepository.findById(id).orElse(null);
    }

    @Override
    public void guardar(ProductoEntity producto) {
        // Si es nuevo (sin ID), le asignamos la fecha de ingreso
        if (producto.getId() == null) {
            producto.setFechaIngreso(LocalDateTime.now());
        }
        productoRepository.save(producto);
    }

    @Override
    public void suspender(Long id) {
        ProductoEntity producto = productoRepository.findById(id).orElse(null);
        if (producto != null) {
            // Cambia el estado (si estaba activo, lo suspende; si estaba suspendido, lo reactiva)
            // Se asume que ProductoEntity tiene un campo 'activo' (boolean)
            producto.setActivo(!producto.isActivo());
            productoRepository.save(producto);
        }
    }

    /**
     * 🚀 IMPLEMENTACIÓN DEL MÉTODO DE BÚSQUEDA ROBUSTA (Busca por Nombre O Descripción)
     * Utiliza el método del repositorio que busca por ambos campos.
     */
    @Override
    public List<ProductoEntity> buscarPorNombreODescripcion(String query) {
        if (query == null || query.trim().isEmpty()) {
            return productoRepository.findAll();
        }
        // Llama al método del repositorio, pasando el query dos veces
        // para que busque en Nombre Y Descripción.
        return productoRepository.findByNombreContainingIgnoreCaseOrDescripcionContainingIgnoreCase(query, query);
    }
}