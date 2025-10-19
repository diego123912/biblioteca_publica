package co.edu.umanizales.biblioteca_publica.model;

import co.edu.umanizales.biblioteca_publica.enums.UserType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Profesor extends Usuario {
    private String departamento;
    private String especializacion;

    public Profesor(String id, String nombre, String apellido, String email, String telefono, String departamento, String especializacion) {
        super(id, nombre, apellido, email, telefono, UserType.PROFESOR);
        this.departamento = departamento;
        this.especializacion = especializacion;
    }

    @Override
    public int getLimitePrestamos() {
        return 10;
    }

    @Override
    public int getDiasPrestamo() {
        return 30;
    }
}
