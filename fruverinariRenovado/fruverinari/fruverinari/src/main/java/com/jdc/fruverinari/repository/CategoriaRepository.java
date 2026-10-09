package com.jdc.fruverinari.repository;

import com.jdc.fruverinari.entities.CategoriaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<CategoriaEntity, Long> {
    // Agregar consultas personalizadas si es necesario
}
