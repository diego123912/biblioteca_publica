package co.edu.umanizales.biblioteca_publica.controller;

import co.edu.umanizales.biblioteca_publica.enums.BookGenre;
import co.edu.umanizales.biblioteca_publica.model.Libro;
import co.edu.umanizales.biblioteca_publica.service.LibroService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/libros")
@CrossOrigin(origins = "*")
public class LibroController {

    private final LibroService libroService;

    public LibroController(LibroService libroService) {
        this.libroService = libroService;
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Libro libro) {
        try {
            Libro nuevoLibro = libroService.crear(libro);
            return new ResponseEntity<>(nuevoLibro, HttpStatus.CREATED);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error al crear el libro: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<?> obtenerTodos() {
        try {
            List<Libro> libros = libroService.obtenerTodos();
            return ResponseEntity.ok(libros);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error al obtener los libros: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Libro> obtenerPorId(@PathVariable String id) {
        Optional<Libro> libro = libroService.obtenerPorId(id);
        return libro.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Libro> actualizar(@PathVariable String id, @RequestBody Libro libro) {
        Libro libroActualizado = libroService.actualizar(id, libro);
        if (libroActualizado != null) {
            return ResponseEntity.ok(libroActualizado);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        if (libroService.eliminar(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/buscar/titulo")
    public ResponseEntity<List<Libro>> buscarPorTitulo(@RequestParam String titulo) {
        return ResponseEntity.ok(libroService.buscarPorTitulo(titulo));
    }

    @GetMapping("/buscar/autor")
    public ResponseEntity<List<Libro>> buscarPorAutor(@RequestParam String autor) {
        return ResponseEntity.ok(libroService.buscarPorAutor(autor));
    }

    @GetMapping("/buscar/genero/{genero}")
    public ResponseEntity<List<Libro>> buscarPorGenero(@PathVariable BookGenre genero) {
        return ResponseEntity.ok(libroService.buscarPorGenero(genero));
    }
}
