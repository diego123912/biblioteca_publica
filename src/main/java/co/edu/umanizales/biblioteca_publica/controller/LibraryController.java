package co.edu.umanizales.biblioteca_publica.controller;

import co.edu.umanizales.biblioteca_publica.model.Library;
import co.edu.umanizales.biblioteca_publica.service.LibraryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/libraries")
@CrossOrigin(origins = "*")
public class LibraryController {

    private final LibraryService libraryService;

    public LibraryController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @PostMapping
    public ResponseEntity<Library> create(@RequestBody Library library) {
        Library newLibrary = libraryService.create(library);
        return new ResponseEntity<>(newLibrary, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Library>> getAll() {
        return ResponseEntity.ok(libraryService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Library> getById(@PathVariable String id) {
        Library library = libraryService.getById(id);
        if (library != null) {
            return ResponseEntity.ok(library);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Library> update(@PathVariable String id, @RequestBody Library library) {
        Library updatedLibrary = libraryService.update(id, library);
        if (updatedLibrary != null) {
            return ResponseEntity.ok(updatedLibrary);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        if (libraryService.delete(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
