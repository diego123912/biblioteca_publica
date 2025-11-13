package co.edu.umanizales.biblioteca_publica.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Review {
    private String id;
    private User user;
    private Book book;
    private int rating; // 1-5
    private String comment;
    private LocalDateTime creationDate;
    private boolean approved;

    public Review(String id, User user, Book book, int rating, String comment, LocalDateTime creationDate, boolean approved) {
        this.id = id;
        this.user = user;
        this.book = book;
        this.rating = rating;
        this.comment = comment;
        this.creationDate = creationDate;
        this.approved = approved;
    }

    public void approve() {
        this.approved = true;
    }
}
