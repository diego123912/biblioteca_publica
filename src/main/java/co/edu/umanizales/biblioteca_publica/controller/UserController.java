package co.edu.umanizales.biblioteca_publica.controller;

import co.edu.umanizales.biblioteca_publica.dto.UserDTO;
import co.edu.umanizales.biblioteca_publica.enums.UserType;
import co.edu.umanizales.biblioteca_publica.model.User;
import co.edu.umanizales.biblioteca_publica.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody UserDTO userDTO) {
        try {
            User user = userDTO.toUser();
            User newUser = userService.create(user);
            return new ResponseEntity<>(newUser, HttpStatus.CREATED);
        } catch (IllegalArgumentException error) {
            return new ResponseEntity<>(error.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception error) {
            return new ResponseEntity<>("Internal server error: " + error.getMessage(), 
                HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<User>> getAll() {
        return ResponseEntity.ok(userService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable String id) {
        Optional<User> user = userService.getById(id);
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable String id, @RequestBody UserDTO userDTO) {
        User user = userDTO.toUser();
        User updatedUser = userService.update(id, user);
        if (updatedUser != null) {
            return ResponseEntity.ok(updatedUser);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        if (userService.delete(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<User>> searchByType(@PathVariable UserType type) {
        return ResponseEntity.ok(userService.searchByType(type));
    }

    @GetMapping("/search/email")
    public ResponseEntity<User> searchByEmail(@RequestParam String email) {
        Optional<User> user = userService.searchByEmail(email);
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
