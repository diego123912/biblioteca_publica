package co.edu.umanizales.biblioteca_publica.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    private String id;
    private String userId;
    private String bookId;
    private int rating; // 1-5
    private String comment;
    private LocalDateTime creationDate;
    private boolean approved;

    // Composition: Review composed by User and Book
    private User user;
    private Book book;

    public Review(String id, String userId, String bookId, int rating, String comment, LocalDateTime creationDate) {
        this.id = id;
        this.userId = userId;
        this.bookId = bookId;
        this.rating = Math.max(1, Math.min(5, rating)); // Validate between 1 and 5
        this.comment = comment;
        this.creationDate = creationDate;
        this.approved = false;
    }

    public void approve() {
        this.approved = true;
    }
}
