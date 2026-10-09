package com.jdc.fruverinari.repository;

import com.jdc.fruverinari.entities.MovimientoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MovimientoRepository extends JpaRepository<MovimientoEntity, Long> {

    // Todos los movimientos ordenados por fecha descendente
    List<MovimientoEntity> findAllByOrderByFechaMovimientoDesc();

    // Movimientos en un rango de fechas
    List<MovimientoEntity> findByFechaMovimientoBetween(LocalDateTime inicio, LocalDateTime fin);

    // Movimientos en un rango de fechas para un producto específico
    List<MovimientoEntity> findByFechaMovimientoBetweenAndProducto_Id(LocalDateTime inicio, LocalDateTime fin, Long productoId);
}
