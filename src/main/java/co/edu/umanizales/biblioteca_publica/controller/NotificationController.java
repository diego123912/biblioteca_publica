package co.edu.umanizales.biblioteca_publica.controller;

import co.edu.umanizales.biblioteca_publica.enums.NotificationType;
import co.edu.umanizales.biblioteca_publica.model.Notification;
import co.edu.umanizales.biblioteca_publica.model.User;
import co.edu.umanizales.biblioteca_publica.service.NotificationService;
import co.edu.umanizales.biblioteca_publica.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "*")
public class NotificationController {

    private final NotificationService notificationService;
    private final UserService userService;

    public NotificationController(NotificationService notificationService, UserService userService) {
        this.notificationService = notificationService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Map<String, Object> notificationData) {
        try {
            // Extract usuarioId and fetch the User
            String usuarioId = (String) notificationData.get("usuarioId");
            if (usuarioId == null || usuarioId.trim().isEmpty()) {
                return new ResponseEntity<>("usuarioId is required", HttpStatus.BAD_REQUEST);
            }
            
            User user = userService.getById(usuarioId);
            if (user == null) {
                return new ResponseEntity<>("User not found with id: " + usuarioId, HttpStatus.BAD_REQUEST);
            }
            
            // Extract other fields
            String tipoStr = (String) notificationData.get("tipo");
            NotificationType tipo = NotificationType.valueOf(tipoStr);
            
            String mensaje = (String) notificationData.get("mensaje");
            String fechaEnvioStr = (String) notificationData.get("fechaEnvio");
            LocalDateTime fechaEnvio = LocalDateTime.parse(fechaEnvioStr);
            
            Boolean leida = (Boolean) notificationData.get("leida");
            if (leida == null) {
                leida = false;
            }
            
            // Create Notification object
            Notification notification = new Notification();
            notification.setUser(user);
            notification.setType(tipo);
            notification.setMessage(mensaje);
            notification.setSendDate(fechaEnvio);
            notification.setRead(leida);
            
            Notification newNotification = notificationService.create(notification);
            return new ResponseEntity<>(newNotification, HttpStatus.CREATED);
        } catch (IllegalArgumentException error) {
            return new ResponseEntity<>(error.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception error) {
            return new ResponseEntity<>("Internal server error: " + error.getMessage(), 
                HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<Notification>> getAll() {
        return ResponseEntity.ok(notificationService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Notification> getById(@PathVariable String id) {
        Notification notification = notificationService.getById(id);
        if (notification != null) {
            return ResponseEntity.ok(notification);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Notification> update(@PathVariable String id, @RequestBody Notification notification) {
        Notification updatedNotification = notificationService.update(id, notification);
        if (updatedNotification != null) {
            return ResponseEntity.ok(updatedNotification);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        if (notificationService.delete(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Notification>> getByUser(@PathVariable String userId) {
        return ResponseEntity.ok(notificationService.getByUser(userId));
    }

    @GetMapping("/user/{userId}/unread")
    public ResponseEntity<List<Notification>> getUnread(@PathVariable String userId) {
        return ResponseEntity.ok(notificationService.getUnread(userId));
    }

    @PostMapping("/{id}/mark-read")
    public ResponseEntity<Notification> markAsRead(@PathVariable String id) {
        Notification notification = notificationService.markAsRead(id);
        if (notification != null) {
            return ResponseEntity.ok(notification);
        }
        return ResponseEntity.notFound().build();
    }
}
