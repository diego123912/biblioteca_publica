package co.edu.umanizales.biblioteca_publica.controller;

import co.edu.umanizales.biblioteca_publica.model.Biblioteca;
import co.edu.umanizales.biblioteca_publica.service.BibliotecaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/bibliotecas")
@CrossOrigin(origins = "*")
public class BibliotecaController {

    private final BibliotecaService bibliotecaService;

    public BibliotecaController(BibliotecaService bibliotecaService) {
        this.bibliotecaService = bibliotecaService;
    }

    @PostMapping
    public ResponseEntity<Biblioteca> crear(@RequestBody Biblioteca biblioteca) {
        Biblioteca nuevaBiblioteca = bibliotecaService.crear(biblioteca);
        return new ResponseEntity<>(nuevaBiblioteca, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Biblioteca>> obtenerTodos() {
        return ResponseEntity.ok(bibliotecaService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Biblioteca> obtenerPorId(@PathVariable String id) {
        Optional<Biblioteca> biblioteca = bibliotecaService.obtenerPorId(id);
        return biblioteca.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Biblioteca> actualizar(@PathVariable String id, @RequestBody Biblioteca biblioteca) {
        Biblioteca bibliotecaActualizada = bibliotecaService.actualizar(id, biblioteca);
        if (bibliotecaActualizada != null) {
            return ResponseEntity.ok(bibliotecaActualizada);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        if (bibliotecaService.eliminar(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
