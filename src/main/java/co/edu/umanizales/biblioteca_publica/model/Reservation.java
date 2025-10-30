package co.edu.umanizales.biblioteca_publica.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {
    private String id;
    private String userId;
    private String bookId;
    private LocalDateTime reservationDate;
    private LocalDateTime expirationDate;
    private boolean active;
    private boolean completed;

    public Reservation(String id, String userId, String bookId, LocalDateTime reservationDate, LocalDateTime expirationDate) {
        this.id = id;
        this.userId = userId;
        this.bookId = bookId;
        this.reservationDate = reservationDate;
        this.expirationDate = expirationDate;
        this.active = true;
        this.completed = false;
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
