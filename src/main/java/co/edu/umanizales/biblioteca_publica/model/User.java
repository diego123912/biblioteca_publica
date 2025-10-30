package co.edu.umanizales.biblioteca_publica.model;

import co.edu.umanizales.biblioteca_publica.enums.UserType;
import co.edu.umanizales.biblioteca_publica.interfaces.Notificable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class User implements Notificable {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private UserType type;
    private List<String> notifications = new ArrayList<>();

    public User(String id, String firstName, String lastName, String email, String phone, UserType type) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.type = type;
        this.notifications = new ArrayList<>();
    }

    @Override
    public void sendNotification(String message) {
        notifications.add(message);
        System.out.println("Notification sent to " + firstName + " " + lastName + ": " + message);
    }

    @Override
    public String getContact() {
        return email;
    }

    public abstract int getLoanLimit();
    public abstract int getLoanDays();
}
