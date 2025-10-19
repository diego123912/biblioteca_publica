package co.edu.umanizales.biblioteca_publica.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Resena {
    private String id;
    private String usuarioId;
    private String libroId;
    private int calificacion; // 1-5
    private String comentario;
    private LocalDateTime fechaCreacion;
    private boolean aprobada;

    // Composición: Reseña compuesta por Usuario y Libro
    private Usuario usuario;
    private Libro libro;

    public Resena(String id, String usuarioId, String libroId, int calificacion, String comentario, LocalDateTime fechaCreacion) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.libroId = libroId;
        this.calificacion = Math.max(1, Math.min(5, calificacion)); // Validar entre 1 y 5
        this.comentario = comentario;
        this.fechaCreacion = fechaCreacion;
        this.aprobada = false;
    }

    public void aprobar() {
        this.aprobada = true;
    }
}
