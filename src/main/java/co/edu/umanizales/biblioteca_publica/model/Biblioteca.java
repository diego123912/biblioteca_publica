package co.edu.umanizales.biblioteca_publica.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Biblioteca {
    private String id;
    private String nombre;
    private String direccion;
    private String telefono;
    private String horario;
    
    // Agregación: Biblioteca contiene colecciones de Libros y Usuarios
    private List<Libro> libros = new ArrayList<>();
    private List<Usuario> usuarios = new ArrayList<>();
    private List<Prestamo> prestamos = new ArrayList<>();
    private List<Resena> resenas = new ArrayList<>();

    public Biblioteca(String id, String nombre, String direccion, String telefono, String horario) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.horario = horario;
        this.libros = new ArrayList<>();
        this.usuarios = new ArrayList<>();
        this.prestamos = new ArrayList<>();
        this.resenas = new ArrayList<>();
    }

    public void agregarLibro(Libro libro) {
        libros.add(libro);
    }

    public void agregarUsuario(Usuario usuario) {
        usuarios.add(usuario);
    }

    public void agregarPrestamo(Prestamo prestamo) {
        prestamos.add(prestamo);
    }

    public void agregarResena(Resena resena) {
        resenas.add(resena);
    }

    public int getTotalLibros() {
        return libros.size();
    }

    public int getTotalUsuarios() {
        return usuarios.size();
    }

    public int getPrestamosActivos() {
        return (int) prestamos.stream()
                .filter(p -> p.getEstado().toString().equals("ACTIVO"))
                .count();
    }
}
