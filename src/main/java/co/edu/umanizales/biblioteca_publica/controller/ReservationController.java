package co.edu.umanizales.biblioteca_publica.controller;

import co.edu.umanizales.biblioteca_publica.model.Reservation;
import co.edu.umanizales.biblioteca_publica.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@CrossOrigin(origins = "*")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<Reservation> create(@RequestBody Reservation reservation) {
        Reservation newReservation = reservationService.create(reservation);
        return new ResponseEntity<>(newReservation, HttpStatus.CREATED);
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
