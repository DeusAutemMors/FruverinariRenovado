package com.jdc.fruverinari.repository;

import com.jdc.fruverinari.entities.CanjeOfertaEntity;
import com.jdc.fruverinari.entities.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CanjeOfertaRepository extends JpaRepository<CanjeOfertaEntity, Long> {

    List<CanjeOfertaEntity> findByUsuarioOrderByFechaCanjeDesc(UsuarioEntity usuario);
}
