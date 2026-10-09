package com.jdc.fruverinari.repository;

import com.jdc.fruverinari.entities.HistorialEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistorialRepository extends JpaRepository<HistorialEntity, Long> {
    // Puedes agregar consultas personalizadas si quieres filtrar por entidad, usuario, fecha, etc.
}
