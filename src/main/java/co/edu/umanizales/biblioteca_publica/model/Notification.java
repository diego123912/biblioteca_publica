package co.edu.umanizales.biblioteca_publica.model;

import co.edu.umanizales.biblioteca_publica.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    private String id;
    private User user;
    private NotificationType type;
    private String message;
    private LocalDateTime sendDate;
    private boolean read;

    public void markAsRead() {
        this.read = true;
    }
}
