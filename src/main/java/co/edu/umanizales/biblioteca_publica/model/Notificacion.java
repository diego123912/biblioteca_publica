package co.edu.umanizales.biblioteca_publica.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notificacion {
    private String id;
    private String usuarioId;
    private String tipo; // PRESTAMO, DEVOLUCION, VENCIMIENTO, GENERAL
    private String mensaje;
    private LocalDateTime fechaEnvio;
    private boolean leida;

    public Notificacion(String id, String usuarioId, String tipo, String mensaje, LocalDateTime fechaEnvio) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.tipo = tipo;
        this.mensaje = mensaje;
        this.fechaEnvio = fechaEnvio;
        this.leida = false;
    }

    public void marcarComoLeida() {
        this.leida = true;
    }
}
