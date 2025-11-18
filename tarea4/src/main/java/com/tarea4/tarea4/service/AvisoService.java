package com.tarea4.tarea4.service;

import com.tarea4.tarea4.entity.AvisoAdopcion;
import com.tarea4.tarea4.entity.Nota;
import com.tarea4.tarea4.repository.AvisoRepository;
import com.tarea4.tarea4.repository.NotaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AvisoService {

    private final AvisoRepository avisoRepository;
    private final NotaRepository notaRepository;

    public AvisoService(AvisoRepository avisoRepository, NotaRepository notaRepository) {
        this.avisoRepository = avisoRepository;
        this.notaRepository = notaRepository;
    }

    public List<AvisoAdopcion> getPaginatedAvisos(int page, int perPage) {
        int offset = (page - 1) * perPage;
        return avisoRepository.findPaginated(perPage, offset);
    }

    public long getTotalAvisos() {
        return avisoRepository.countAvisos();
    }

    public Optional<AvisoAdopcion> getAvisoById(int id) {
        return avisoRepository.findById(id);
    }

    public double getPromedioNotas(int avisoId) {
        List<Nota> notas = notaRepository.findByAvisoId(avisoId);
        if (notas.isEmpty()) return -1;

        double sum = notas.stream()
                .mapToInt(Nota::getNota)
                .sum();

        return sum / notas.size();
    }

    public Nota agregarNota(int avisoId, int valorNota) {

        AvisoAdopcion aviso = avisoRepository.findById(avisoId)
                .orElseThrow(() -> new RuntimeException("Aviso no encontrado"));

        Nota n = new Nota();
        n.setAviso(aviso);     
        n.setNota(valorNota);

        return notaRepository.save(n);
    }
}

