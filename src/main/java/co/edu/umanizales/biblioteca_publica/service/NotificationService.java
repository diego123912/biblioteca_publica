package co.edu.umanizales.biblioteca_publica.service;

import co.edu.umanizales.biblioteca_publica.enums.NotificationType;
import co.edu.umanizales.biblioteca_publica.model.Notification;
import co.edu.umanizales.biblioteca_publica.model.User;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class NotificationService {
    
    private final CSVService csvService;
    private final UserService userService;
    private final Map<String, Notification> notifications = new ConcurrentHashMap<>();
    private static final String FILE_NAME = "notifications.csv";

    public NotificationService(CSVService csvService, UserService userService) {
        this.csvService = csvService;
        this.userService = userService;
        loadFromCSV();
    }

    private void loadFromCSV() {
        try {
            List<List<String>> data = csvService.readCSV(FILE_NAME);
            for (List<String> row : data) {
                if (row.size() >= 6) {
                    String userId = row.get(1);
                    
                    User user = userService.getById(userId);
                    
                    if (user != null) {
                        Notification notification = new Notification(
                            row.get(0), // id
                            user, // user
                            NotificationType.valueOf(row.get(2)), // type
                            row.get(3), // message
                            LocalDateTime.parse(row.get(4)), // sendDate
                            Boolean.parseBoolean(row.get(5)) // read
                        );
                        notifications.put(notification.getId(), notification);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading notifications from CSV: " + e.getMessage());
        }
    }

    private void saveToCSV() {
        try {
            List<String> headers = Arrays.asList("id", "userId", "type", "message", "sendDate", "read");
            
            List<List<String>> data = new ArrayList<>();
            for (Notification notification : notifications.values()) {
                data.add(Arrays.asList(
                    notification.getId(),
                    notification.getUser() != null ? notification.getUser().getId() : "",
                    notification.getType() != null ? notification.getType().toString() : "",
                    csvService.escapeCSV(notification.getMessage()),
                    notification.getSendDate().toString(),
                    String.valueOf(notification.isRead())
                ));
            }
            
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

    public Notification getById(String id) {
        return notifications.get(id);
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


    public Notification markAsRead(String id) {
        Notification notification = getById(id);
        if (notification != null) {
            notification.markAsRead();
            notifications.put(id, notification);
            saveToCSV();
            return notification;
        }
        return null;
    }
    
    public List<Notification> getByUser(String userId) {
        List<Notification> result = new ArrayList<>();
        for (Notification n : notifications.values()) {
            if (n.getUser() != null && n.getUser().getId().equals(userId)) {
                result.add(n);
            }
        }
        // Sort by sendDate descending
        for (int i = 0; i < result.size(); i++) {
            for (int j = i + 1; j < result.size(); j++) {
                if (result.get(i).getSendDate().isBefore(result.get(j).getSendDate())) {
                    Notification temp = result.get(i);
                    result.set(i, result.get(j));
                    result.set(j, temp);
                }
            }
        }
        return result;
    }

    public List<Notification> getUnread(String userId) {
        List<Notification> result = new ArrayList<>();
        for (Notification n : notifications.values()) {
            if (n.getUser() != null && n.getUser().getId().equals(userId) && !n.isRead()) {
                result.add(n);
            }
        }
        // Sort by sendDate descending
        for (int i = 0; i < result.size(); i++) {
            for (int j = i + 1; j < result.size(); j++) {
                if (result.get(i).getSendDate().isBefore(result.get(j).getSendDate())) {
                    Notification temp = result.get(i);
                    result.set(i, result.get(j));
                    result.set(j, temp);
                }
            }
        }
        return result;
    }
    
    // Helper method to validate notification data
    private void validateNotification(Notification notification) {
        // Check required fields are not empty
        if (notification.getUser() == null) {
            throw new IllegalArgumentException("User is required");
        }
        if (notification.getType() == null) {
            throw new IllegalArgumentException("Type is required");
        }
        if (notification.getMessage() == null || notification.getMessage().trim().isEmpty()) {
            throw new IllegalArgumentException("Message is required");
        }
    }
}
