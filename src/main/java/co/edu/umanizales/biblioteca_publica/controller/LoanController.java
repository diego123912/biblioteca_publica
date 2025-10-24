package co.edu.umanizales.biblioteca_publica.controller;

import co.edu.umanizales.biblioteca_publica.enums.LoanStatus;
import co.edu.umanizales.biblioteca_publica.model.Loan;
import co.edu.umanizales.biblioteca_publica.service.LoanService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/loans")
@CrossOrigin(origins = "*")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping
    public ResponseEntity<Loan> create(@RequestBody Loan loan) {
        Loan newLoan = loanService.create(loan);
        return new ResponseEntity<>(newLoan, HttpStatus.CREATED);
    }

    @PostMapping("/perform")
    public ResponseEntity<?> performLoan(@RequestBody Map<String, String> data) {
        try {
            String userId = data.get("userId");
            String bookId = data.get("bookId");
            Loan loan = loanService.performLoan(userId, bookId);
            return new ResponseEntity<>(loan, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{id}/return")
    public ResponseEntity<?> performReturn(@PathVariable String id) {
        try {
            Loan loan = loanService.performReturn(id);
            return ResponseEntity.ok(loan);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<Loan>> getAll() {
        return ResponseEntity.ok(loanService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Loan> getById(@PathVariable String id) {
        Optional<Loan> loan = loanService.getById(id);
        return loan.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Loan> update(@PathVariable String id, @RequestBody Loan loan) {
        Loan updatedLoan = loanService.update(id, loan);
        if (updatedLoan != null) {
            return ResponseEntity.ok(updatedLoan);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        if (loanService.delete(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Loan>> getByUser(@PathVariable String userId) {
        return ResponseEntity.ok(loanService.getByUser(userId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Loan>> getByStatus(@PathVariable LoanStatus status) {
        return ResponseEntity.ok(loanService.getByStatus(status));
    }

    @PostMapping("/check-overdue")
    public ResponseEntity<Void> checkOverdue() {
        loanService.checkOverdue();
        return ResponseEntity.ok().build();
    }
}
