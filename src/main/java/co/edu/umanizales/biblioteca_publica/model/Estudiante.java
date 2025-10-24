package co.edu.umanizales.biblioteca_publica.model;

import co.edu.umanizales.biblioteca_publica.enums.UserType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Student extends User {
    private String major;
    private String semester;

    public Student(String id, String firstName, String lastName, String email, String phone, String major, String semester) {
        super(id, firstName, lastName, email, phone, UserType.STUDENT);
        this.major = major;
        this.semester = semester;
    }

    @Override
    public int getLoanLimit() {
        return 3;
    }

    @Override
    public int getLoanDays() {
        return 15;
    }
}
