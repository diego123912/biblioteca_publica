package co.edu.umanizales.biblioteca_publica.service;

import co.edu.umanizales.biblioteca_publica.model.Reservation;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class ReservationService {
    
    private final CSVService csvService;
    private final Map<String, Reservation> reservations = new ConcurrentHashMap<>();
    private static final String FILE_NAME = "reservations.csv";

    public ReservationService(CSVService csvService) {
        this.csvService = csvService;
        loadFromCSV();
    }

    private void loadFromCSV() {
        try {
            List<List<String>> data = csvService.readCSV(FILE_NAME);
            for (List<String> row : data) {
                if (row.size() >= 6) {
                    Reservation reservation = new Reservation(
                        row.get(0), // id
                        row.get(1), // userId
                        row.get(2), // bookId
                        LocalDateTime.parse(row.get(3)), // reservationDate
                        LocalDateTime.parse(row.get(4)), // expirationDate
                        Boolean.parseBoolean(row.get(5)), // active
                        Boolean.parseBoolean(row.get(6))  // completed
                    );
                    reservations.put(reservation.getId(), reservation);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading reservations from CSV: " + e.getMessage());
        }
    }

    private void saveToCSV() {
        try {
            List<String> headers = Arrays.asList("id", "userId", "bookId", "reservationDate", 
                "expirationDate", "active", "completed");
            
            List<List<String>> data = reservations.values().stream()
                .map(reservation -> Arrays.asList(
                    reservation.getId(),
                    reservation.getUserId(),
                    reservation.getBookId(),
                    reservation.getReservationDate().toString(),
                    reservation.getExpirationDate().toString(),
                    String.valueOf(reservation.isActive()),
                    String.valueOf(reservation.isCompleted())
                ))
                .collect(Collectors.toList());
            
            csvService.writeCSV(FILE_NAME, headers, data);
        } catch (IOException e) {
            System.err.println("Error saving reservations to CSV: " + e.getMessage());
        }
    }

    public Reservation create(Reservation reservation) {
        if (reservation.getId() == null || reservation.getId().isEmpty()) {
            reservation.setId(UUID.randomUUID().toString());
        }
        reservations.put(reservation.getId(), reservation);
        saveToCSV();
        return reservation;
    }

    public List<Reservation> getAll() {
        return new ArrayList<>(reservations.values());
    }

    public Optional<Reservation> getById(String id) {
        return Optional.ofNullable(reservations.get(id));
    }

    public Reservation update(String id, Reservation updatedReservation) {
        if (reservations.containsKey(id)) {
            updatedReservation.setId(id);
            reservations.put(id, updatedReservation);
            saveToCSV();
            return updatedReservation;
        }
        return null;
    }

    public boolean delete(String id) {
        if (reservations.remove(id) != null) {
            saveToCSV();
            return true;
        }
        return false;
    }

    public List<Reservation> getByUser(String userId) {
        return reservations.values().stream()
            .filter(r -> r.getUserId().equals(userId))
            .collect(Collectors.toList());
    }

    public List<Reservation> getActive() {
        return reservations.values().stream()
            .filter(Reservation::isActive)
            .collect(Collectors.toList());
    }

    public Reservation cancel(String id) {
        Optional<Reservation> reservationOpt = getById(id);
        if (reservationOpt.isPresent()) {
            Reservation reservation = reservationOpt.get();
            reservation.cancel();
            reservations.put(id, reservation);
            saveToCSV();
            return reservation;
        }
        return null;
    }

    public Reservation complete(String id) {
        Optional<Reservation> reservationOpt = getById(id);
        if (reservationOpt.isPresent()) {
            Reservation reservation = reservationOpt.get();
            reservation.complete();
            reservations.put(id, reservation);
            saveToCSV();
            return reservation;
        }
        return null;
    }
}
