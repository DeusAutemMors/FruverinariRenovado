package com.jdc.fruverinari.repository;

import com.jdc.fruverinari.entities.ProductoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<ProductoEntity, Long> {

    List<ProductoEntity> findByNombreContainingIgnoreCase(String nombre);

    ProductoEntity findByNombre(String nombre);
    List<ProductoEntity> findByNombreContainingIgnoreCaseOrDescripcionContainingIgnoreCase(String nombre, String descripcion);

    // Aquí puedes agregar consultas personalizadas si es necesario
}
