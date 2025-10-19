package co.edu.umanizales.biblioteca_publica.model;

import co.edu.umanizales.biblioteca_publica.enums.UserType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Administrador extends Usuario {
    private String rol;
    private boolean permisoTotal;

    public Administrador(String id, String nombre, String apellido, String email, String telefono, String rol, boolean permisoTotal) {
        super(id, nombre, apellido, email, telefono, UserType.ADMINISTRADOR);
        this.rol = rol;
        this.permisoTotal = permisoTotal;
    }

    @Override
    public int getLimitePrestamos() {
        return Integer.MAX_VALUE;
    }

    @Override
    public int getDiasPrestamo() {
        return 60;
    }
}
