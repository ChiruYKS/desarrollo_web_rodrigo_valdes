package com.tarea4.tarea4.controller;

import com.tarea4.tarea4.entity.AvisoAdopcion;
import com.tarea4.tarea4.entity.Comentario;
import com.tarea4.tarea4.service.AvisoService;
import com.tarea4.tarea4.service.ComentarioService;
import com.tarea4.tarea4.service.NotaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class ApiController {
    private final AvisoService avisoService;
    private final ComentarioService comentarioService;
    private final NotaService notaService;

    public ApiController(AvisoService avisoService, ComentarioService comentarioService, NotaService notaService) {
        this.avisoService = avisoService;
        this.comentarioService = comentarioService;
        this.notaService = notaService;
    }

    @GetMapping("/detalle_aviso/{avisoId}/")
    public ResponseEntity<?> detalleAviso(@PathVariable Integer avisoId){
        AvisoAdopcion a = avisoService.findById(avisoId);
        if (a == null) return ResponseEntity.notFound().build();

        // Construir JSON similar al Flask
        Map<String, Object> resp = Map.of(
            "fecha_ingreso", a.getFechaIngreso().toString().replace('T',' '),
            "fecha_entrega", a.getFechaEntrega().toString().replace('T',' '),
            "comuna", a.getComuna() != null ? a.getComuna().getNombre() : null,
            "region", a.getComuna() != null && a.getComuna().getRegion() != null ? a.getComuna().getRegion().getNombre() : null,
            "sector", a.getSector(),
            "cantidad_tipo_edad", String.format("%d %s (%s)", a.getCantidad(), a.getTipo(), a.getEdad() + ( "a".equals(a.getUnidadMedida()) ? " años" : " meses" )),
            "contactos", a.getContactos().stream().map(c -> c.getNombre() + ": " + c.getIdentificador()).toArray(),
            "fotos", a.getFotos().stream().map(f -> f.getRutaArchivo()).toArray()
        );
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/add_comentario/{avisoId}/")
    public ResponseEntity<?> addComentario(@PathVariable Integer avisoId, @RequestBody Map<String,String> body){
        String nombre = body.getOrDefault("nombre","");
        String texto = body.getOrDefault("texto","");
        // Validaciones como en Flask
        if (nombre.length() < 3 || nombre.length() > 80) {
            return ResponseEntity.badRequest().body(Map.of("success",false,"errors", new String[]{"El nombre debe tener entre 3 y 80 caracteres."}));
        }
        if (texto.length() < 5) {
            return ResponseEntity.badRequest().body(Map.of("success",false,"errors", new String[]{"El comentario debe tener al menos 5 caracteres."}));
        }

        Comentario c = comentarioService.createComentario(avisoId, nombre, texto);
        return ResponseEntity.ok(Map.of("success", true, "comentario", Map.of(
            "id", c.getId(),
            "nombre", c.getNombre(),
            "texto", c.getTexto(),
            "fecha", c.getFecha().toString().replace('T',' ')
        )));
    }

    @GetMapping("/get_comentarios/{avisoId}/")
    public ResponseEntity<?> getComentarios(@PathVariable Integer avisoId){
        var list = comentarioService.getComentariosByAvisoId(avisoId);
        return ResponseEntity.ok(list.stream().map(c -> Map.of(
            "id", c.getId(),
            "nombre", c.getNombre(),
            "texto", c.getTexto(),
            "fecha", c.getFecha().toString().replace('T',' ')
        )));
    }

    @PostMapping("/add_nota/{avisoId}/")
    public ResponseEntity<?> addNota(@PathVariable Integer avisoId, @RequestBody Map<String,Object> body){
        Object nObj = body.get("nota");
        if (nObj == null) return ResponseEntity.badRequest().body(Map.of("success", false, "message", "nota requerida"));
        Integer notaVal;
        if (nObj instanceof Number) notaVal = ((Number)nObj).intValue();
        else {
            try { notaVal = Integer.parseInt(nObj.toString()); } catch(Exception e){ return ResponseEntity.badRequest().body(Map.of("success",false,"message","nota inválida")); }
        }
        try {
            notaService.addNota(avisoId, notaVal);
            Double avg = notaService.getAverage(avisoId);
            Long count = notaService.getCount(avisoId);
            Double rounded = avg == null ? null : Math.round(avg * 100.0)/100.0;
            return ResponseEntity.ok(Map.of("success",true,"promedio", rounded, "contador", count));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", ex.getMessage()));
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("success", false, "message", "Error interno"));
        }
    }

    // api_estadisticas similar a tu Flask: puedes añadir consultas con repository.query(...)
}

