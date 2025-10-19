package co.edu.umanizales.biblioteca_publica.controller;

import co.edu.umanizales.biblioteca_publica.model.Notificacion;
import co.edu.umanizales.biblioteca_publica.service.NotificacionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/notificaciones")
@CrossOrigin(origins = "*")
public class NotificacionController {

    private final NotificacionService notificacionService;

    public NotificacionController(NotificacionService notificacionService) {
        this.notificacionService = notificacionService;
    }

    @PostMapping
    public ResponseEntity<Notificacion> crear(@RequestBody Notificacion notificacion) {
        Notificacion nuevaNotificacion = notificacionService.crear(notificacion);
        return new ResponseEntity<>(nuevaNotificacion, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Notificacion>> obtenerTodos() {
        return ResponseEntity.ok(notificacionService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Notificacion> obtenerPorId(@PathVariable String id) {
        Optional<Notificacion> notificacion = notificacionService.obtenerPorId(id);
        return notificacion.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Notificacion> actualizar(@PathVariable String id, @RequestBody Notificacion notificacion) {
        Notificacion notificacionActualizada = notificacionService.actualizar(id, notificacion);
        if (notificacionActualizada != null) {
            return ResponseEntity.ok(notificacionActualizada);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        if (notificacionService.eliminar(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Notificacion>> obtenerPorUsuario(@PathVariable String usuarioId) {
        return ResponseEntity.ok(notificacionService.obtenerPorUsuario(usuarioId));
    }

    @GetMapping("/usuario/{usuarioId}/no-leidas")
    public ResponseEntity<List<Notificacion>> obtenerNoLeidas(@PathVariable String usuarioId) {
        return ResponseEntity.ok(notificacionService.obtenerNoLeidas(usuarioId));
    }

    @PostMapping("/{id}/marcar-leida")
    public ResponseEntity<Notificacion> marcarComoLeida(@PathVariable String id) {
        Notificacion notificacion = notificacionService.marcarComoLeida(id);
        if (notificacion != null) {
            return ResponseEntity.ok(notificacion);
        }
        return ResponseEntity.notFound().build();
    }
}
