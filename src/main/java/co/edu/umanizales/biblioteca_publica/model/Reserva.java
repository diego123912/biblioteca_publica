package co.edu.umanizales.biblioteca_publica.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reserva {
    private String id;
    private String usuarioId;
    private String libroId;
    private LocalDateTime fechaReserva;
    private LocalDateTime fechaExpiracion;
    private boolean activa;
    private boolean completada;

    public Reserva(String id, String usuarioId, String libroId, LocalDateTime fechaReserva, LocalDateTime fechaExpiracion) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.libroId = libroId;
        this.fechaReserva = fechaReserva;
        this.fechaExpiracion = fechaExpiracion;
        this.activa = true;
        this.completada = false;
    }

    public boolean estaExpirada() {
        return LocalDateTime.now().isAfter(fechaExpiracion);
    }

    public void cancelar() {
        this.activa = false;
    }

    public void completar() {
        this.activa = false;
        this.completada = true;
    }
}
