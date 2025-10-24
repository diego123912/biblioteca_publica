package co.edu.umanizales.biblioteca_publica.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    private String id;
    private String userId;
    private String type; // LOAN, RETURN, OVERDUE, GENERAL
    private String message;
    private LocalDateTime sendDate;
    private boolean read;

    public Notification(String id, String userId, String type, String message, LocalDateTime sendDate) {
        this.id = id;
        this.userId = userId;
        this.type = type;
        this.message = message;
        this.sendDate = sendDate;
        this.read = false;
    }

    public void markAsRead() {
        this.read = true;
    }
}
