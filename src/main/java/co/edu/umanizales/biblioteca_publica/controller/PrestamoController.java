package co.edu.umanizales.biblioteca_publica.controller;

import co.edu.umanizales.biblioteca_publica.enums.LoanStatus;
import co.edu.umanizales.biblioteca_publica.model.Prestamo;
import co.edu.umanizales.biblioteca_publica.service.PrestamoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/prestamos")
@CrossOrigin(origins = "*")
public class PrestamoController {

    private final PrestamoService prestamoService;

    public PrestamoController(PrestamoService prestamoService) {
        this.prestamoService = prestamoService;
    }

    @PostMapping
    public ResponseEntity<Prestamo> crear(@RequestBody Prestamo prestamo) {
        Prestamo nuevoPrestamo = prestamoService.crear(prestamo);
        return new ResponseEntity<>(nuevoPrestamo, HttpStatus.CREATED);
    }

    @PostMapping("/realizar")
    public ResponseEntity<?> realizarPrestamo(@RequestBody Map<String, String> datos) {
        try {
            String usuarioId = datos.get("usuarioId");
            String libroId = datos.get("libroId");
            Prestamo prestamo = prestamoService.realizarPrestamo(usuarioId, libroId);
            return new ResponseEntity<>(prestamo, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{id}/devolver")
    public ResponseEntity<?> realizarDevolucion(@PathVariable String id) {
        try {
            Prestamo prestamo = prestamoService.realizarDevolucion(id);
            return ResponseEntity.ok(prestamo);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<Prestamo>> obtenerTodos() {
        return ResponseEntity.ok(prestamoService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Prestamo> obtenerPorId(@PathVariable String id) {
        Optional<Prestamo> prestamo = prestamoService.obtenerPorId(id);
        return prestamo.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Prestamo> actualizar(@PathVariable String id, @RequestBody Prestamo prestamo) {
        Prestamo prestamoActualizado = prestamoService.actualizar(id, prestamo);
        if (prestamoActualizado != null) {
            return ResponseEntity.ok(prestamoActualizado);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        if (prestamoService.eliminar(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Prestamo>> obtenerPorUsuario(@PathVariable String usuarioId) {
        return ResponseEntity.ok(prestamoService.obtenerPorUsuario(usuarioId));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Prestamo>> obtenerPorEstado(@PathVariable LoanStatus estado) {
        return ResponseEntity.ok(prestamoService.obtenerPorEstado(estado));
    }

    @PostMapping("/verificar-vencimientos")
    public ResponseEntity<Void> verificarVencimientos() {
        prestamoService.verificarVencimientos();
        return ResponseEntity.ok().build();
    }
}
