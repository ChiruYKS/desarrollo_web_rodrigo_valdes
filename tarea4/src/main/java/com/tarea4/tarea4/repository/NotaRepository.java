package com.tarea4.tarea4.repository;

import com.tarea4.tarea4.entity.Nota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotaRepository extends JpaRepository<Nota, Integer> {

    // Para obtener el promedio de notas
    @Query("SELECT AVG(n.nota) FROM Nota n WHERE n.avisoId = :avisoId")
    Double findAverageByAvisoId(@Param("avisoId") Integer avisoId);

    // Para contar cuántas notas tiene el aviso
    @Query("SELECT COUNT(n) FROM Nota n WHERE n.avisoId = :avisoId")
    Long countByAvisoId(@Param("avisoId") Integer avisoId);

    // Para listar todas las notas (lo que necesita AvisoService)
    List<Nota> findByAvisoId(Integer avisoId);
}

