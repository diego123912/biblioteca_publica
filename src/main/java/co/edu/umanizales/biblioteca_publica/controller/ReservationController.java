package co.edu.umanizales.biblioteca_publica.controller;

import co.edu.umanizales.biblioteca_publica.dto.ReservationRequestDTO;
import co.edu.umanizales.biblioteca_publica.model.Reservation;
import co.edu.umanizales.biblioteca_publica.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controlador REST para gestionar reservaciones de libros
 * Endpoints disponibles:
 * - POST /api/reservations - Crear nueva reservación
 * - GET /api/reservations - Obtener todas las reservaciones
 * - GET /api/reservations/{id} - Obtener reservación por ID
 * - PUT /api/reservations/{id} - Actualizar reservación
 * - DELETE /api/reservations/{id} - Eliminar reservación
 * - GET /api/reservations/user/{userId} - Obtener reservaciones de un usuario
 * - GET /api/reservations/active - Obtener reservaciones activas
 * - POST /api/reservations/{id}/cancel - Cancelar reservación
 * - POST /api/reservations/{id}/complete - Completar reservación
 */
@RestController
@RequestMapping("/api/reservations")
@CrossOrigin(origins = "*")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody ReservationRequestDTO reservationRequest) {
        try {
            // Obtener IDs del request
            String userId = reservationRequest.getUserId();
            String bookId = reservationRequest.getBookId();
            
            // Crear la reservación usando el servicio
            Reservation newReservation = reservationService.createReservation(userId, bookId);
            
            // Retornar la reservación creada con código 201 (CREATED)
            return new ResponseEntity<>(newReservation, HttpStatus.CREATED);
            
        } catch (RuntimeException e) {
            // Si hay un error, retornar mensaje de error con código 400 (BAD_REQUEST)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<Reservation>> getAll() {
        return ResponseEntity.ok(reservationService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getById(@PathVariable String id) {
        Reservation reservation = reservationService.getById(id);
        if (reservation != null) {
            return ResponseEntity.ok(reservation);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reservation> update(@PathVariable String id, @RequestBody Reservation reservation) {
        Reservation updatedReservation = reservationService.update(id, reservation);
        if (updatedReservation != null) {
            return ResponseEntity.ok(updatedReservation);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        if (reservationService.delete(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Reservation>> getByUser(@PathVariable String userId) {
        return ResponseEntity.ok(reservationService.getByUser(userId));
    }

    @GetMapping("/active")
    public ResponseEntity<List<Reservation>> getActive() {
        return ResponseEntity.ok(reservationService.getActive());
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<Reservation> cancel(@PathVariable String id) {
        Reservation reservation = reservationService.cancel(id);
        if (reservation != null) {
            return ResponseEntity.ok(reservation);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<Reservation> complete(@PathVariable String id) {
        Reservation reservation = reservationService.complete(id);
        if (reservation != null) {
            return ResponseEntity.ok(reservation);
        }
        return ResponseEntity.notFound().build();
    }
}
