package com.tarea4.tarea4.service;

import com.tarea4.tarea4.entity.AvisoAdopcion;
import com.tarea4.tarea4.entity.Nota;
import com.tarea4.tarea4.repository.NotaRepository;
import com.tarea4.tarea4.repository.AvisoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotaService {

    private final NotaRepository notaRepository;
    private final AvisoRepository avisoRepository;   

    public NotaService(NotaRepository notaRepository, AvisoRepository avisoRepository) {
        this.notaRepository = notaRepository;
        this.avisoRepository = avisoRepository;      
    }

    @Transactional
    public Nota addNota(Integer avisoId, Integer value) {

        if (value == null || value < 1 || value > 7) {
            throw new IllegalArgumentException("La nota debe ser un entero entre 1 y 7.");
        }

        // Buscar el aviso asociado
        AvisoAdopcion aviso = avisoRepository.findById(avisoId)
                .orElseThrow(() -> new IllegalArgumentException("Aviso no encontrado"));

        // Crear nueva nota
        Nota n = new Nota();
        n.setAviso(aviso);      
        n.setNota(value);

        return notaRepository.save(n);
    }

    public Double getAverage(Integer avisoId) {
        return notaRepository.findAverageByAvisoId(avisoId);
    }

    public Long getCount(Integer avisoId) {
        return notaRepository.countByAvisoId(avisoId);
    }
}
