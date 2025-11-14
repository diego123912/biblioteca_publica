package co.edu.umanizales.biblioteca_publica.service;

import co.edu.umanizales.biblioteca_publica.model.Book;
import co.edu.umanizales.biblioteca_publica.model.Reservation;
import co.edu.umanizales.biblioteca_publica.model.User;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ReservationService {
    
    private final CSVService csvService;
    private final UserService userService;
    private final BookService bookService;
    private final Map<String, Reservation> reservations = new ConcurrentHashMap<>();
    private static final String FILE_NAME = "reservations.csv";

    public ReservationService(CSVService csvService, UserService userService, BookService bookService) {
        this.csvService = csvService;
        this.userService = userService;
        this.bookService = bookService;
        loadFromCSV();
    }

    private void loadFromCSV() {
        try {
            List<List<String>> data = csvService.readCSV(FILE_NAME);
            for (List<String> row : data) {
                if (row.size() >= 7) {
                    String userId = row.get(1);
                    String bookId = row.get(2);
                    
                    User user = userService.getById(userId);
                    Book book = bookService.getById(bookId);
                    
                    if (user != null && book != null) {
                        Reservation reservation = new Reservation(
                            row.get(0), // id
                            user, // user
                            book, // book
                            LocalDateTime.parse(row.get(3)), // reservationDate
                            LocalDateTime.parse(row.get(4)), // expirationDate
                            Boolean.parseBoolean(row.get(5)), // active
                            Boolean.parseBoolean(row.get(6))  // completed
                        );
                        reservations.put(reservation.getId(), reservation);
                    }
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
            
            List<List<String>> data = new ArrayList<>();
            for (Reservation reservation : reservations.values()) {
                data.add(Arrays.asList(
                    reservation.getId(),
                    reservation.getUser() != null ? reservation.getUser().getId() : "",
                    reservation.getBook() != null ? reservation.getBook().getId() : "",
                    reservation.getReservationDate().toString(),
                    reservation.getExpirationDate().toString(),
                    String.valueOf(reservation.isActive()),
                    String.valueOf(reservation.isCompleted())
                ));
            }
            
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

    public Reservation createReservation(String userId, String bookId) {
        User user = userService.getById(userId);
        if (user == null) {
            throw new RuntimeException("User not found with id: " + userId);
        }

        Book book = bookService.getById(bookId);
        if (book == null) {
            throw new RuntimeException("Book not found with id: " + bookId);
        }

        if (!book.isAvailable()) {
            throw new RuntimeException("Book is not available for reservation");
        }

        String id = UUID.randomUUID().toString();
        LocalDateTime reservationDate = LocalDateTime.now();
        LocalDateTime expirationDate = reservationDate.plusDays(3); // Reservation expires in 3 days

        Reservation reservation = new Reservation(
            id,
            user,
            book,
            reservationDate,
            expirationDate,
            true,
            false
        );

        reservations.put(id, reservation);
        saveToCSV();
        return reservation;
    }

    public List<Reservation> getAll() {
        return new ArrayList<>(reservations.values());
    }

    public Reservation getById(String id) {
        return reservations.get(id);
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
        List<Reservation> result = new ArrayList<>();
        for (Reservation r : reservations.values()) {
            if (r.getUser() != null && r.getUser().getId().equals(userId)) {
                result.add(r);
            }
        }
        return result;
    }

    public List<Reservation> getActive() {
        List<Reservation> result = new ArrayList<>();
        for (Reservation r : reservations.values()) {
            if (r.isActive()) {
                result.add(r);
            }
        }
        return result;
    }

    public Reservation cancel(String id) {
        Reservation reservation = getById(id);
        if (reservation != null) {
            reservation.cancel();
            reservations.put(id, reservation);
            saveToCSV();
            return reservation;
        }
        return null;
    }

    public Reservation complete(String id) {
        Reservation reservation = getById(id);
        if (reservation != null) {
            reservation.complete();
            reservations.put(id, reservation);
            saveToCSV();
            return reservation;
        }
        return null;
    }
}
