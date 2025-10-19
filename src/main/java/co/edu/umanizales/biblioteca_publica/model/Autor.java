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
public class Autor {
    private String id;
    private String nombre;
    private String apellido;
    private String nacionalidad;
    private LocalDate fechaNacimiento;
    private String biografia;
    private List<String> librosIds = new ArrayList<>();

    public Autor(String id, String nombre, String apellido, String nacionalidad, LocalDate fechaNacimiento, String biografia) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.nacionalidad = nacionalidad;
        this.fechaNacimiento = fechaNacimiento;
        this.biografia = biografia;
        this.librosIds = new ArrayList<>();
    }

    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }
}
