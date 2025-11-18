package com.tarea4.tarea4.repository;

import com.tarea4.tarea4.entity.AvisoAdopcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvisoRepository extends JpaRepository<AvisoAdopcion, Integer> {
    
    List<AvisoAdopcion> findByComunaId(Integer comunaId);
    Page<AvisoAdopcion> findAll(Pageable pageable);

    @Query(value = "SELECT * FROM aviso_adopcion ORDER BY fecha_ingreso DESC LIMIT :limit OFFSET :offset",
           nativeQuery = true)
    List<AvisoAdopcion> findPaginated(@Param("limit") int limit, @Param("offset") int offset);

    @Query("SELECT COUNT(a) FROM AvisoAdopcion a")
    long countAvisos();
}
