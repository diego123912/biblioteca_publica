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

    public void markAsRead() {
        this.read = true;
    }
}
