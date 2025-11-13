package co.edu.umanizales.biblioteca_publica.model;

import co.edu.umanizales.biblioteca_publica.enums.BookGenre;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    private String id;
    private String isbn;
    private String title;
    private Author author;
    private Publisher publisher;
    private int publicationYear;
    private BookGenre genre;
    private int availableQuantity;
    private int totalQuantity;
    private String location;

    public boolean isAvailable() {
        return availableQuantity > 0;
    }

    public void borrow() {
        if (availableQuantity > 0) {
            availableQuantity--;
        }
    }

    public void returnBook() {
        if (availableQuantity < totalQuantity) {
            availableQuantity++;
        }
    }
}
