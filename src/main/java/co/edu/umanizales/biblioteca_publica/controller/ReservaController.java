package co.edu.umanizales.biblioteca_publica.controller;

import co.edu.umanizales.biblioteca_publica.model.Reserva;
import co.edu.umanizales.biblioteca_publica.service.ReservaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reservas")
@CrossOrigin(origins = "*")
public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @PostMapping
    public ResponseEntity<Reserva> crear(@RequestBody Reserva reserva) {
        Reserva nuevaReserva = reservaService.crear(reserva);
        return new ResponseEntity<>(nuevaReserva, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Reserva>> obtenerTodos() {
        return ResponseEntity.ok(reservaService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reserva> obtenerPorId(@PathVariable String id) {
        Optional<Reserva> reserva = reservaService.obtenerPorId(id);
        return reserva.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reserva> actualizar(@PathVariable String id, @RequestBody Reserva reserva) {
        Reserva reservaActualizada = reservaService.actualizar(id, reserva);
        if (reservaActualizada != null) {
            return ResponseEntity.ok(reservaActualizada);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        if (reservaService.eliminar(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Reserva>> obtenerPorUsuario(@PathVariable String usuarioId) {
        return ResponseEntity.ok(reservaService.obtenerPorUsuario(usuarioId));
    }

    @GetMapping("/activas")
    public ResponseEntity<List<Reserva>> obtenerActivas() {
        return ResponseEntity.ok(reservaService.obtenerActivas());
    }

    @PostMapping("/{id}/cancelar")
    public ResponseEntity<Reserva> cancelar(@PathVariable String id) {
        Reserva reserva = reservaService.cancelar(id);
        if (reserva != null) {
            return ResponseEntity.ok(reserva);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/completar")
    public ResponseEntity<Reserva> completar(@PathVariable String id) {
        Reserva reserva = reservaService.completar(id);
        if (reserva != null) {
            return ResponseEntity.ok(reserva);
        }
        return ResponseEntity.notFound().build();
    }
}
