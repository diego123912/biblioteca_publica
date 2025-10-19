package co.edu.umanizales.biblioteca_publica.records;

import java.time.LocalDateTime;

public record RegistroActividad(
        String accion,
        String entidad,
        String entidadId,
        String usuario,
        LocalDateTime fechaHora,
        String detalles
) {
}
