package co.edu.umanizales.biblioteca_publica.controller;

import co.edu.umanizales.biblioteca_publica.model.Editorial;
import co.edu.umanizales.biblioteca_publica.service.EditorialService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/editoriales")
@CrossOrigin(origins = "*")
public class EditorialController {

    private final EditorialService editorialService;


    public EditorialController(EditorialService editorialService) {
        this.editorialService = editorialService;
    }

    @PostMapping
    public ResponseEntity<Editorial> crear(@RequestBody Editorial editorial) {
        Editorial nuevaEditorial = editorialService.crear(editorial);
        return new ResponseEntity<>(nuevaEditorial, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Editorial>> obtenerTodos() {
        return ResponseEntity.ok(editorialService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Editorial> obtenerPorId(@PathVariable String id) {
        Optional<Editorial> editorial = editorialService.obtenerPorId(id);
        return editorial.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Editorial> actualizar(@PathVariable String id, @RequestBody Editorial editorial) {
        Editorial editorialActualizada = editorialService.actualizar(id, editorial);
        if (editorialActualizada != null) {
            return ResponseEntity.ok(editorialActualizada);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        if (editorialService.eliminar(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Editorial>> buscarPorNombre(@RequestParam String nombre) {
        return ResponseEntity.ok(editorialService.buscarPorNombre(nombre));
    }
}
