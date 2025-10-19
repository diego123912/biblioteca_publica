package co.edu.umanizales.biblioteca_publica.service;

import co.edu.umanizales.biblioteca_publica.enums.BookGenre;
import co.edu.umanizales.biblioteca_publica.model.Libro;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class LibroService {
    
    private final CSVService csvService;
    private final Map<String, Libro> libros = new ConcurrentHashMap<>();
    private static final String FILE_NAME = "libros.csv";

    public LibroService(CSVService csvService) {
        this.csvService = csvService;
        loadFromCSV();
    }

    private void loadFromCSV() {
        try {
            List<List<String>> data = csvService.readCSV(FILE_NAME);
            for (List<String> row : data) {
                if (row.size() >= 10) {
                    Libro libro = new Libro(
                        row.get(0), // id
                        row.get(1), // isbn
                        row.get(2), // titulo
                        row.get(3), // autor
                        row.get(4), // editorial
                        Integer.parseInt(row.get(5)), // anioPublicacion
                        BookGenre.valueOf(row.get(6)), // genero
                        Integer.parseInt(row.get(7)), // cantidadDisponible
                        Integer.parseInt(row.get(8)), // cantidadTotal
                        row.get(9)  // ubicacion
                    );
                    libros.put(libro.getId(), libro);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al cargar libros desde CSV: " + e.getMessage());
        }
    }

    private void saveToCSV() {
        try {
            // Asegurarse de que el directorio existe
            Files.createDirectories(Paths.get("data/csv"));
            List<String> headers = Arrays.asList("id", "isbn", "titulo", "autor", "editorial", 
                "anioPublicacion", "genero", "cantidadDisponible", "cantidadTotal", "ubicacion");
            
            List<List<String>> data = libros.values().stream()
                .map(libro -> Arrays.asList(
                    libro.getId(),
                    libro.getIsbn(),
                    libro.getTitulo(),
                    libro.getAutor(),
                    libro.getEditorial(),
                    String.valueOf(libro.getAnioPublicacion()),
                    libro.getGenero().toString(),
                    String.valueOf(libro.getCantidadDisponible()),
                    String.valueOf(libro.getCantidadTotal()),
                    libro.getUbicacion()
                ))
                .collect(Collectors.toList());
            
            csvService.writeCSV(FILE_NAME, headers, data);
        } catch (IOException e) {
            System.err.println("Error al guardar libros en CSV: " + e.getMessage());
        }
    }

    public Libro crear(Libro libro) {
        try {
            if (libro.getId() == null || libro.getId().isEmpty()) {
                libro.setId(UUID.randomUUID().toString());
            }
            libros.put(libro.getId(), libro);
            saveToCSV();
            return libro;
        } catch (Exception e) {
            System.err.println("Error al crear el libro: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("No se pudo guardar el libro: " + e.getMessage(), e);
        }
    }

    public List<Libro> obtenerTodos() {
        return new ArrayList<>(libros.values());
    }

    public Optional<Libro> obtenerPorId(String id) {
        return Optional.ofNullable(libros.get(id));
    }

    public Libro actualizar(String id, Libro libroActualizado) {
        if (libros.containsKey(id)) {
            libroActualizado.setId(id);
            libros.put(id, libroActualizado);
            saveToCSV();
            return libroActualizado;
        }
        return null;
    }

    public boolean eliminar(String id) {
        if (libros.remove(id) != null) {
            saveToCSV();
            return true;
        }
        return false;
    }

    public List<Libro> buscarPorTitulo(String titulo) {
        return libros.values().stream()
            .filter(libro -> libro.getTitulo().toLowerCase().contains(titulo.toLowerCase()))
            .collect(Collectors.toList());
    }

    public List<Libro> buscarPorAutor(String autor) {
        return libros.values().stream()
            .filter(libro -> libro.getAutor().toLowerCase().contains(autor.toLowerCase()))
            .collect(Collectors.toList());
    }

    public List<Libro> buscarPorGenero(BookGenre genero) {
        return libros.values().stream()
            .filter(libro -> libro.getGenero() == genero)
            .collect(Collectors.toList());
    }
}
