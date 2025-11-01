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

    public void approve() {
        this.approved = true;
    }
}
