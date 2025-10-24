package co.edu.umanizales.biblioteca_publica.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Author {
    private String id;
    private String firstName;
    private String lastName;
    private String nationality;
    private LocalDate birthDate;
    private String biography;
    private List<String> bookIds = new ArrayList<>();

    public Author(String id, String firstName, String lastName, String nationality, LocalDate birthDate, String biography) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.nationality = nationality;
        this.birthDate = birthDate;
        this.biography = biography;
        this.bookIds = new ArrayList<>();
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
