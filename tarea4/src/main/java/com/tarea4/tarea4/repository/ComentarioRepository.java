package com.tarea4.tarea4.repository;

import com.tarea4.tarea4.entity.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComentarioRepository extends JpaRepository<Comentario, Integer> {

    // Obtiene todos los comentarios de un aviso
    List<Comentario> findByAvisoId(Integer avisoId);

    //Igual pero ordenados de manera descendiente por fecha
    List<Comentario> findByAvisoIdOrderByFechaDesc(Integer avisoId);
}
