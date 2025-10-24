package co.edu.umanizales.biblioteca_publica.model;

import co.edu.umanizales.biblioteca_publica.enums.UserType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Administrator extends User {
    private String role;
    private boolean fullPermission;

    public Administrator(String id, String firstName, String lastName, String email, String phone, String role, boolean fullPermission) {
        super(id, firstName, lastName, email, phone, UserType.ADMINISTRATOR);
        this.role = role;
        this.fullPermission = fullPermission;
    }

    /**
     * Explicit getter to avoid issues if Lombok/annotation processing is not active in the IDE.
     */
    public boolean isFullPermission() {
        return fullPermission;
    }

    @Override
    public int getLoanLimit() {
        return Integer.MAX_VALUE;
    }

    @Override
    public int getLoanDays() {
        return 60;
    }
}
