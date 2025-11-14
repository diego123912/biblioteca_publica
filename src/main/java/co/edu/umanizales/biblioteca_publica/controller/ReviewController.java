package co.edu.umanizales.biblioteca_publica.controller;

import co.edu.umanizales.biblioteca_publica.dto.ReviewRequestDTO;
import co.edu.umanizales.biblioteca_publica.model.Review;
import co.edu.umanizales.biblioteca_publica.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "*")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody ReviewRequestDTO reviewRequest) {
        try {
            Review review = reviewService.createReview(
                reviewRequest.getUserId(),
                reviewRequest.getBookId(),
                reviewRequest.getRating(),
                reviewRequest.getComment()
            );
            return new ResponseEntity<>(review, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<Review>> getAll() {
        return ResponseEntity.ok(reviewService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Review> getById(@PathVariable String id) {
        Review review = reviewService.getById(id);
        if (review != null) {
            return ResponseEntity.ok(review);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Review> update(@PathVariable String id, @RequestBody Review review) {
        Review updatedReview = reviewService.update(id, review);
        if (updatedReview != null) {
            return ResponseEntity.ok(updatedReview);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        if (reviewService.delete(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<Review>> getByBook(@PathVariable String bookId) {
        return ResponseEntity.ok(reviewService.getByBook(bookId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Review>> getByUser(@PathVariable String userId) {
        return ResponseEntity.ok(reviewService.getByUser(userId));
    }

    @GetMapping("/approved")
    public ResponseEntity<List<Review>> getApproved() {
        return ResponseEntity.ok(reviewService.getApproved());
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<Review> approve(@PathVariable String id) {
        Review review = reviewService.approveReview(id);
        if (review != null) {
            return ResponseEntity.ok(review);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/book/{bookId}/average-rating")
    public ResponseEntity<Map<String, Double>> getAverageRating(@PathVariable String bookId) {
        double average = reviewService.getAverageRatingBook(bookId);
        return ResponseEntity.ok(Map.of("averageRating", average));
    }
}
