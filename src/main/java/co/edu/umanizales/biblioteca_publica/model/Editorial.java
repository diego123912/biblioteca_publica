package co.edu.umanizales.biblioteca_publica.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Editorial {
    private String id;
    private String nombre;
    private String pais;
    private String sitioWeb;
    private String contacto;
    private List<String> librosIds = new ArrayList<>();

    public Editorial(String id, String nombre, String pais, String sitioWeb, String contacto) {
        this.id = id;
        this.nombre = nombre;
        this.pais = pais;
        this.sitioWeb = sitioWeb;
        this.contacto = contacto;
        this.librosIds = new ArrayList<>();
    }
}
