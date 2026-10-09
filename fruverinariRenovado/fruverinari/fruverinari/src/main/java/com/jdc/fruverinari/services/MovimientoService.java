package com.jdc.fruverinari.services;

import com.jdc.fruverinari.entities.MovimientoEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface MovimientoService {

    // Obtener todos los movimientos
    List<MovimientoEntity> obtenerTodos();

    // Obtener un movimiento por su ID
    MovimientoEntity obtenerPorId(Long id);

    // Guardar o actualizar un movimiento
    void guardar(MovimientoEntity movimiento);

    // Eliminar un movimiento por su ID
    void eliminar(Long id);

    // Obtener el historial completo de movimientos ordenado por fecha descendente
    List<MovimientoEntity> obtenerHistorial();

    // Obtener movimientos filtrando por rango de fechas
    List<MovimientoEntity> obtenerMovimientosPorRangoDeFecha(LocalDateTime inicio,LocalDateTime  fin);

    // Obtener movimientos filtrando por rango de fechas y por producto
    List<MovimientoEntity> obtenerMovimientosPorRangoDeFechaYProducto(LocalDateTime inicio, LocalDateTime fin, Long idProducto);
}
