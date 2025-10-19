package co.edu.umanizales.biblioteca_publica.controller;

import co.edu.umanizales.biblioteca_publica.model.Resena;
import co.edu.umanizales.biblioteca_publica.service.ResenaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/resenas")
@CrossOrigin(origins = "*")
public class ResenaController {

    private final ResenaService resenaService;

    public ResenaController(ResenaService resenaService) {
        this.resenaService = resenaService;
    }

    @PostMapping
    public ResponseEntity<Resena> crear(@RequestBody Resena resena) {
        Resena nuevaResena = resenaService.crear(resena);
        return new ResponseEntity<>(nuevaResena, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Resena>> obtenerTodos() {
        return ResponseEntity.ok(resenaService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resena> obtenerPorId(@PathVariable String id) {
        Optional<Resena> resena = resenaService.obtenerPorId(id);
        return resena.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Resena> actualizar(@PathVariable String id, @RequestBody Resena resena) {
        Resena resenaActualizada = resenaService.actualizar(id, resena);
        if (resenaActualizada != null) {
            return ResponseEntity.ok(resenaActualizada);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        if (resenaService.eliminar(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/libro/{libroId}")
    public ResponseEntity<List<Resena>> obtenerPorLibro(@PathVariable String libroId) {
        return ResponseEntity.ok(resenaService.obtenerPorLibro(libroId));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Resena>> obtenerPorUsuario(@PathVariable String usuarioId) {
        return ResponseEntity.ok(resenaService.obtenerPorUsuario(usuarioId));
    }

    @GetMapping("/aprobadas")
    public ResponseEntity<List<Resena>> obtenerAprobadas() {
        return ResponseEntity.ok(resenaService.obtenerAprobadas());
    }

    @PostMapping("/{id}/aprobar")
    public ResponseEntity<Resena> aprobar(@PathVariable String id) {
        Resena resena = resenaService.aprobarResena(id);
        if (resena != null) {
            return ResponseEntity.ok(resena);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/libro/{libroId}/calificacion-promedio")
    public ResponseEntity<Map<String, Double>> obtenerCalificacionPromedio(@PathVariable String libroId) {
        double promedio = resenaService.obtenerCalificacionPromedioLibro(libroId);
        return ResponseEntity.ok(Map.of("calificacionPromedio", promedio));
    }
}
