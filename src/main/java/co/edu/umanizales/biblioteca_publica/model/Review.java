package co.edu.umanizales.biblioteca_publica.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Review {
    private String id;
    private String userId;
    private String bookId;
    private int rating; // 1-5
    private String comment;
    private LocalDateTime creationDate;
    private boolean approved;
    private User user;
    private Book book;

    public Review(String id, String userId, String bookId, int rating, String comment, LocalDateTime creationDate, boolean approved, User user, Book book) {
        this.id = id;
        this.userId = userId;
        this.bookId = bookId;
        this.rating = rating;
        this.comment = comment;
        this.creationDate = creationDate;
        this.approved = approved;
        this.user = user;
        this.book = book;
    }

    public void approve() {
        this.approved = true;
    }
}
