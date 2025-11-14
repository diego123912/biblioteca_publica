package co.edu.umanizales.biblioteca_publica.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequestDTO {
    private String userId;
    private String bookId;
    private int rating;
    private String comment;
}
