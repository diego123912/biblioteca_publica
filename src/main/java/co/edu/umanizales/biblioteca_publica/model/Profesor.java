package co.edu.umanizales.biblioteca_publica.model;

import co.edu.umanizales.biblioteca_publica.enums.UserType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Teacher extends User {
    private String department;
    private String specialization;

    public Teacher(String id, String firstName, String lastName, String email, String phone, String department, String specialization) {
        super(id, firstName, lastName, email, phone, UserType.TEACHER);
        this.department = department;
        this.specialization = specialization;
    }

    @Override
    public int getLoanLimit() {
        return 10;
    }

    @Override
    public int getLoanDays() {
        return 30;
    }
}
