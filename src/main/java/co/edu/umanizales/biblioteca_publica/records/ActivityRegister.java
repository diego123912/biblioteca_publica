package co.edu.umanizales.biblioteca_publica.records;

import java.time.LocalDateTime;

public record ActivityRegister(
        String accion,
        String entidad,
        String entidadId,
        String usuario,
        LocalDateTime fechaHora,
        String detalles)
{
}

