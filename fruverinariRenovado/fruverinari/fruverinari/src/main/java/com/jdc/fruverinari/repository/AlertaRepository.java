package com.jdc.fruverinari.repository;


import com.jdc.fruverinari.entities.AlertaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlertaRepository extends JpaRepository<AlertaEntity, Long> {
    // Agregar consultas personalizadas si es necesario
}