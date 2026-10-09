package com.jdc.fruverinari.repository;

import com.jdc.fruverinari.entities.ProveedorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProveedorRepository extends JpaRepository<ProveedorEntity, Long> {
    // Puedes agregar consultas personalizadas si es necesario
}
