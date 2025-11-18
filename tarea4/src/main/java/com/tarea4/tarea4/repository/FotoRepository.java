package com.tarea4.tarea4.repository;

import com.tarea4.tarea4.entity.Foto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FotoRepository extends JpaRepository<Foto, Integer> {

    // Fotos asociadas a un aviso
    List<Foto> findByActividadId(Integer avisoId);
}

