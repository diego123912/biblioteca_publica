package co.edu.umanizales.biblioteca_publica.service;

import co.edu.umanizales.biblioteca_publica.model.Notification;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class NotificationService {
    
    private final CSVService csvService;
    private final Map<String, Notification> notifications = new ConcurrentHashMap<>();
    private static final String FILE_NAME = "notifications.csv";

    public NotificationService(CSVService csvService) {
        this.csvService = csvService;
        loadFromCSV();
    }

    private void loadFromCSV() {
        try {
            List<List<String>> data = csvService.readCSV(FILE_NAME);
            for (List<String> row : data) {
                if (row.size() >= 6) {
                    Notification notification = new Notification(
                        row.get(0), // id
                        row.get(1), // userId
                        row.get(2), // type
                        row.get(3), // message
                        LocalDateTime.parse(row.get(4)), // sendDate
                        Boolean.parseBoolean(row.get(5)) // read
                    );
                    notifications.put(notification.getId(), notification);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading notifications from CSV: " + e.getMessage());
        }
    }

    private void saveToCSV() {
        try {
            List<String> headers = Arrays.asList("id", "userId", "type", "message", "sendDate", "read");
            
            List<List<String>> data = notifications.values().stream()
                .map(notification -> Arrays.asList(
                    notification.getId(),
                    notification.getUserId(),
                    notification.getType(),
                    csvService.escapeCSV(notification.getMessage()),
                    notification.getSendDate().toString(),
                    String.valueOf(notification.isRead())
                ))
                .collect(Collectors.toList());
            
            csvService.writeCSV(FILE_NAME, headers, data);
        } catch (IOException e) {
            System.err.println("Error saving notifications to CSV: " + e.getMessage());
        }
    }

    public Notification create(Notification notification) {
        if (notification.getId() == null || notification.getId().isEmpty()) {
            notification.setId(UUID.randomUUID().toString());
        }
        
        // Validate notification data
        validateNotification(notification);
        
        notifications.put(notification.getId(), notification);
        saveToCSV();
        return notification;
    }

    public List<Notification> getAll() {
        return new ArrayList<>(notifications.values());
    }

    public Optional<Notification> getById(String id) {
        return Optional.ofNullable(notifications.get(id));
    }

    public Notification update(String id, Notification updatedNotification) {
        if (notifications.containsKey(id)) {
            updatedNotification.setId(id);
            notifications.put(id, updatedNotification);
            saveToCSV();
            return updatedNotification;
        }
        return null;
    }

    public boolean delete(String id) {
        if (notifications.remove(id) != null) {
            saveToCSV();
            return true;
        }
        return false;
    }

    public List<Notification> getByUser(String userId) {
        return notifications.values().stream()
            .filter(n -> n.getUserId().equals(userId))
            .sorted(Comparator.comparing(Notification::getSendDate).reversed())
            .collect(Collectors.toList());
    }

    public List<Notification> getUnread(String userId) {
        return notifications.values().stream()
            .filter(n -> n.getUserId().equals(userId) && !n.isRead())
            .sorted(Comparator.comparing(Notification::getSendDate).reversed())
            .collect(Collectors.toList());
    }

    public Notification markAsRead(String id) {
        Optional<Notification> notificationOpt = getById(id);
        if (notificationOpt.isPresent()) {
            Notification notification = notificationOpt.get();
            notification.markAsRead();
            notifications.put(id, notification);
            saveToCSV();
            return notification;
        }
        return null;
    }
    
    // Helper method to validate notification data
    private void validateNotification(Notification notification) {
        // Check required fields are not empty
        if (notification.getUserId() == null || notification.getUserId().trim().isEmpty()) {
            throw new IllegalArgumentException("User ID is required");
        }
        if (notification.getType() == null || notification.getType().trim().isEmpty()) {
            throw new IllegalArgumentException("Type is required");
        }
        if (notification.getMessage() == null || notification.getMessage().trim().isEmpty()) {
            throw new IllegalArgumentException("Message is required");
        }
    }
}
