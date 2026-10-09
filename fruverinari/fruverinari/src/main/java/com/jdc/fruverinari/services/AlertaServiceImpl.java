package com.jdc.fruverinari.services;

import com.jdc.fruverinari.entities.AlertaEntity;
import com.jdc.fruverinari.entities.ProductoEntity; // Importamos ProductoEntity
import com.jdc.fruverinari.repository.AlertaRepository;
import com.jdc.fruverinari.repository.ProductoRepository; // 🚀 Importamos ProductoRepository
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors; // Necesario para .toList() o .collect(Collectors.toList())

@Service
public class AlertaServiceImpl implements AlertaService {

    @Autowired
    private AlertaRepository alertaRepository;

    // 🚀 INYECTAR ProductoRepository para acceder a la lista de productos
    @Autowired
    private ProductoRepository productoRepository;

    // ------------------------------------------------------------------
    // 🚀 IMPLEMENTACIÓN RF05: Obtener productos con stock bajo
    // ------------------------------------------------------------------
    @Override
    @Transactional(readOnly = true)
    public List<ProductoEntity> obtenerProductosConBajoStock() {

        // Obtenemos todos los productos y los filtramos en memoria.
        // Filtra los productos donde la 'cantidad' actual sea menor o igual al 'stockMinimo'.
        return productoRepository.findAll().stream()
                .filter(p -> p.getCantidad() != null
                        && p.getStockMinimo() != null
                        && p.getCantidad() <= p.getStockMinimo())
                .collect(Collectors.toList());

        // Nota: En Java 16+, puedes usar .toList() en lugar de .collect(Collectors.toList())
    }

    // ------------------------------------------------------------------
    // IMPLEMENTACIONES CRUD DE AlertaEntity EXISTENTES
    // ------------------------------------------------------------------

    @Override
    @Transactional(readOnly = true)
    public List<AlertaEntity> obtenerTodos() {
        return alertaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public AlertaEntity obtenerPorId(Long id) {
        return alertaRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void guardar(AlertaEntity alerta) {
        alertaRepository.save(alerta);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        alertaRepository.deleteById(id);
    }
}