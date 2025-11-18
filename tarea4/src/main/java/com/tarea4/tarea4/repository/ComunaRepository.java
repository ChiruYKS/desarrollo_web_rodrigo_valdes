package com.tarea4.tarea4.repository;

import com.tarea4.tarea4.entity.Comuna;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComunaRepository extends JpaRepository<Comuna, Integer> {

    // Comunas por región
    List<Comuna> findByRegionId(Integer regionId);
}
