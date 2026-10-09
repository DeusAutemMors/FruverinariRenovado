package com.jdc.fruverinari.repository;

import com.jdc.fruverinari.entities.OfertaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfertaRepository extends JpaRepository<OfertaEntity, Long> {

    List<OfertaEntity> findByActivaTrueOrderByPuntosRequeridosAsc();

    boolean existsByTitulo(String titulo);
}
