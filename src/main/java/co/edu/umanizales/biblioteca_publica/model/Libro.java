package co.edu.umanizales.biblioteca_publica.model;

import co.edu.umanizales.biblioteca_publica.enums.BookGenre;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Libro {
    private String id;
    private String isbn;
    private String titulo;
    private String autor;
    private String editorial;
    private int anioPublicacion;
    private BookGenre genero;
    private int cantidadDisponible;
    private int cantidadTotal;
    private String ubicacion;

    public boolean estaDisponible() {
        return cantidadDisponible > 0;
    }

    public void prestar() {
        if (cantidadDisponible > 0) {
            cantidadDisponible--;
        }
    }

    public void devolver() {
        if (cantidadDisponible < cantidadTotal) {
            cantidadDisponible++;
        }
    }
}
