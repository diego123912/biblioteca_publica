package co.edu.umanizales.biblioteca_publica.model;

import co.edu.umanizales.biblioteca_publica.enums.UserType;
import co.edu.umanizales.biblioteca_publica.interfaces.Notificable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import co.edu.umanizales.biblioteca_publica.enums.UserType;
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
