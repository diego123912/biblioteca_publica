package co.edu.umanizales.biblioteca_publica.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Reservation {
    private String id;
    private User user;
    private Book book;
    private LocalDateTime reservationDate;
    private LocalDateTime expirationDate;
    private boolean active;
    private boolean completed;

    public Reservation(String id, User user, Book book, LocalDateTime reservationDate, LocalDateTime expirationDate, boolean active, boolean completed) {
        this.id = id;
        this.user = user;
        this.book = book;
        this.reservationDate = reservationDate;
        this.expirationDate = expirationDate;
        this.active = active;
        this.completed = completed;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expirationDate);
    }

    public void cancel() {
        this.active = false;
    }

    public void complete() {
        this.active = false;
        this.completed = true;
    }
}
