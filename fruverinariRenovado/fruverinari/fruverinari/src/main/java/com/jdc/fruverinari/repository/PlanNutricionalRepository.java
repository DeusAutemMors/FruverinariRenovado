package com.jdc.fruverinari.repository;

import com.jdc.fruverinari.entities.PlanNutricionalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanNutricionalRepository extends JpaRepository<PlanNutricionalEntity, Long> {

    List<PlanNutricionalEntity> findTop10ByOrderByFechaCreacionDesc();
}
