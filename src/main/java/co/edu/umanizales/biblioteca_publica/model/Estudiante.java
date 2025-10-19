package co.edu.umanizales.biblioteca_publica.model;

import co.edu.umanizales.biblioteca_publica.enums.UserType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Estudiante extends Usuario {
    private String carrera;
    private String semestre;

    public Estudiante(String id, String nombre, String apellido, String email, String telefono, String carrera, String semestre) {
        super(id, nombre, apellido, email, telefono, UserType.ESTUDIANTE);
        this.carrera = carrera;
        this.semestre = semestre;
    }

    @Override
    public int getLimitePrestamos() {
        return 3;
    }

    @Override
    public int getDiasPrestamo() {
        return 15;
    }
}
