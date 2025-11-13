package co.edu.umanizales.biblioteca_publica.controller;

import co.edu.umanizales.biblioteca_publica.model.Publisher;
import co.edu.umanizales.biblioteca_publica.service.PublisherService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/publishers")
@CrossOrigin(origins = "*")
public class PublisherController {

    private final PublisherService publisherService;


    public PublisherController(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @PostMapping
    public ResponseEntity<Publisher> create(@RequestBody Publisher publisher) {
        Publisher newPublisher = publisherService.create(publisher);
        return new ResponseEntity<>(newPublisher, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Publisher>> getAll() {
        return ResponseEntity.ok(publisherService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Publisher> getById(@PathVariable String id) {
        Publisher publisher = publisherService.getById(id);
        if (publisher != null) {
            return ResponseEntity.ok(publisher);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Publisher> update(@PathVariable String id, @RequestBody Publisher publisher) {
        Publisher updatedPublisher = publisherService.update(id, publisher);
        if (updatedPublisher != null) {
            return ResponseEntity.ok(updatedPublisher);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        if (publisherService.delete(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<Publisher>> searchByName(@RequestParam String name) {
        return ResponseEntity.ok(publisherService.searchByName(name));
    }


}
