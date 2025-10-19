package co.edu.umanizales.biblioteca_publica.controller;

import co.edu.umanizales.biblioteca_publica.model.Autor;
import co.edu.umanizales.biblioteca_publica.service.AutorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/autores")
@CrossOrigin(origins = "*")
public class AutorController {

    private final AutorService autorService;

    public AutorController(AutorService autorService) {
        this.autorService = autorService;
    }

    @PostMapping
    public ResponseEntity<Autor> crear(@RequestBody Autor autor) {
        Autor nuevoAutor = autorService.crear(autor);
        return new ResponseEntity<>(nuevoAutor, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Autor>> obtenerTodos() {
        return ResponseEntity.ok(autorService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Autor> obtenerPorId(@PathVariable String id) {
        Optional<Autor> autor = autorService.obtenerPorId(id);
        return autor.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Autor> actualizar(@PathVariable String id, @RequestBody Autor autor) {
        Autor autorActualizado = autorService.actualizar(id, autor);
        if (autorActualizado != null) {
            return ResponseEntity.ok(autorActualizado);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        if (autorService.eliminar(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Autor>> buscarPorNombre(@RequestParam String nombre) {
        return ResponseEntity.ok(autorService.buscarPorNombre(nombre));
    }
}
