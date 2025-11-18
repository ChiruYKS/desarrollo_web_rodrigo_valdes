package com.tarea4.tarea4.controller;

import com.tarea4.tarea4.entity.AvisoAdopcion;
import com.tarea4.tarea4.repository.AvisoRepository;
import com.tarea4.tarea4.repository.RegionRepository;
import com.tarea4.tarea4.service.NotaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PageController {
    private final AvisoRepository avisoRepository;
    private final RegionRepository regionRepository;
    private final NotaService notaService;

    public PageController(AvisoRepository avisoRepository, RegionRepository regionRepository, NotaService notaService) {
        this.avisoRepository = avisoRepository;
        this.regionRepository = regionRepository;
        this.notaService = notaService;
    }

    @GetMapping({"/", "/portada/"})
    public String portada(Model model){
        // últimos 5
        Page<AvisoAdopcion> ultimos = avisoRepository.findAll(PageRequest.of(0,5));
        model.addAttribute("avisos", ultimos.getContent());
        model.addAttribute("regiones", regionRepository.findAll());
        return "index";
    }

    @GetMapping("/agregar_aviso/")
    public String agregarAviso(Model model){
        model.addAttribute("regiones", regionRepository.findAll());
        return "p1";
    }

    @GetMapping("/listado_avisos/")
    public String listadoAvisos(Model model, @RequestParam(defaultValue="1") int page){
        int perPage = 5;
        Page<AvisoAdopcion> p = avisoRepository.findAll(PageRequest.of(Math.max(0,page-1), perPage));
        // cargar promedio por aviso para mostrar en la vista
        p.getContent().forEach(a -> {
            Double avg = notaService.getAverage(a.getId());
            a.setPromedioNota(avg == null ? null : Math.round(avg * 100.0)/100.0);
        });
        model.addAttribute("avisos", p.getContent());
        model.addAttribute("page", page);
        model.addAttribute("totalPages", p.getTotalPages());
        return "p2";
    }
}

