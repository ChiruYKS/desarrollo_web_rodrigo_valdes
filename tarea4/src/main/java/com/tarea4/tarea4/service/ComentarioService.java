package com.tarea4.tarea4.service;

import com.tarea4.tarea4.entity.AvisoAdopcion;
import com.tarea4.tarea4.entity.Comentario;
import com.tarea4.tarea4.repository.AvisoRepository;
import com.tarea4.tarea4.repository.ComentarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ComentarioService {

    private final ComentarioRepository comentarioRepository;
    private final AvisoRepository avisoRepository;

    public ComentarioService(ComentarioRepository comentarioRepository, AvisoRepository avisoRepository) {
        this.comentarioRepository = comentarioRepository;
        this.avisoRepository = avisoRepository;
    }

    @Transactional
    public Comentario createComentario(Integer avisoId, String nombre, String texto) {

        // Existe el aviso?
        AvisoAdopcion aviso = avisoRepository.findById(avisoId)
                .orElseThrow(() -> new IllegalArgumentException("Aviso no encontrado"));

        // Crear nuevo comentario
        Comentario comentario = new Comentario();
        comentario.setAviso(aviso);
        comentario.setNombre(nombre);
        comentario.setTexto(texto);
        comentario.setFecha(LocalDateTime.now());

        return comentarioRepository.save(comentario);
    }

    public List<Comentario> getComentariosByAvisoId(Integer avisoId) {
        return comentarioRepository.findByAvisoIdOrderByFechaDesc(avisoId);
    }
}
