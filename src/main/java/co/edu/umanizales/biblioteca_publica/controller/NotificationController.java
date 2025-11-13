package co.edu.umanizales.biblioteca_publica.controller;

import co.edu.umanizales.biblioteca_publica.model.Notification;
import co.edu.umanizales.biblioteca_publica.service.NotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "*")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Notification notification) {
        try {
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
        Optional<Notification> notification = notificationService.getById(id);
        return notification.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
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
