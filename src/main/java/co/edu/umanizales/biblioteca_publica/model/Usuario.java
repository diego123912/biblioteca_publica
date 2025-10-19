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
public abstract class Usuario implements Notificable {
    private String id;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private UserType tipo;
    private List<String> notificaciones = new ArrayList<>();

    public Usuario(String id, String nombre, String apellido, String email, String telefono, UserType tipo) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.telefono = telefono;
        this.tipo = tipo;
        this.notificaciones = new ArrayList<>();
    }

    @Override
    public void enviarNotificacion(String mensaje) {
        notificaciones.add(mensaje);
        System.out.println("Notificación enviada a " + nombre + " " + apellido + ": " + mensaje);
    }

    @Override
    public String getContacto() {
        return email;
    }

    public abstract int getLimitePrestamos();
    public abstract int getDiasPrestamo();
}
