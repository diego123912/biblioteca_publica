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
