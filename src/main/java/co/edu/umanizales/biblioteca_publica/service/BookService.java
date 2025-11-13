package co.edu.umanizales.biblioteca_publica.service;

import co.edu.umanizales.biblioteca_publica.enums.BookGenre;
import co.edu.umanizales.biblioteca_publica.model.Book;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class BookService {
    
    private final CSVService csvService;
    private final Map<String, Book> books = new ConcurrentHashMap<>();
    private static final String FILE_NAME = "books.csv";

    public BookService(CSVService csvService) {
        this.csvService = csvService;
        loadFromCSV();
    }

    private void loadFromCSV() {
        try {
            List<List<String>> data = csvService.readCSV(FILE_NAME);
            for (List<String> row : data) {
                if (row.size() >= 10) {
                    Book book = new Book(
                        row.get(0), // id
                        row.get(1), // isbn
                        row.get(2), // title
                        row.get(3), // author
                        row.get(4), // publisher
                        Integer.parseInt(row.get(5)), // publicationYear
                        BookGenre.valueOf(row.get(6)), // genre
                        Integer.parseInt(row.get(7)), // availableQuantity
                        Integer.parseInt(row.get(8)), // totalQuantity
                        row.get(9)  // location
                    );
                    books.put(book.getId(), book);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading books from CSV: " + e.getMessage());
        }
    }

    private void saveToCSV() {
        try {
            // Ensure directory exists
            Files.createDirectories(Paths.get("data/csv"));
            List<String> headers = Arrays.asList("id", "isbn", "title", "author", "publisher", 
                "publicationYear", "genre", "availableQuantity", "totalQuantity", "location");
            
            List<List<String>> data = books.values().stream()
                .map(book -> Arrays.asList(
                    book.getId(),
                    book.getIsbn(),
                    book.getTitle(),
                    book.getAuthor(),
                    book.getPublisher(),
                    String.valueOf(book.getPublicationYear()),
                    book.getGenre().toString(),
                    String.valueOf(book.getAvailableQuantity()),
                    String.valueOf(book.getTotalQuantity()),
                    book.getLocation()
                ))
                .collect(Collectors.toList());
            
            csvService.writeCSV(FILE_NAME, headers, data);
        } catch (IOException e) {
            System.err.println("Error saving books to CSV: " + e.getMessage());
        }
    }

    public Book create(Book book) {
        try {
            if (book.getId() == null || book.getId().isEmpty()) {
                book.setId(UUID.randomUUID().toString());
            }
            
            // Validate book data
            validateBook(book);
            
            // Check if ISBN already exists
            if (isISBNDuplicate(book.getIsbn())) {
                throw new IllegalArgumentException("ISBN already exists: " + book.getIsbn());
            }
            
            books.put(book.getId(), book);
            saveToCSV();
            return book;
        } catch (Exception e) {
            System.err.println("Error creating book: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Could not save book: " + e.getMessage(), e);
        }
    }

    public List<Book> getAll() {
        return new ArrayList<>(books.values());
    }

    public Optional<Book> getById(String id) {
        return Optional.ofNullable(books.get(id));
    }

    public Book update(String id, Book updatedBook) {
        if (books.containsKey(id)) {
            updatedBook.setId(id);
            books.put(id, updatedBook);
            saveToCSV();
            return updatedBook;
        }
        return null;
    }

    public boolean delete(String id) {
        if (books.remove(id) != null) {
            saveToCSV();
            return true;
        }
        return false;
    }

    public List<Book> searchByTitle(String title) {
        return books.values().stream()
            .filter(book -> book.getTitle().toLowerCase().contains(title.toLowerCase()))
            .collect(Collectors.toList());
    }

    public List<Book> searchByAuthor(String author) {
        return books.values().stream()
            .filter(book -> book.getAuthor().toLowerCase().contains(author.toLowerCase()))
            .collect(Collectors.toList());
    }

    public List<Book> searchByGenre(BookGenre genre) {
        return books.values().stream()
            .filter(book -> book.getGenre() == genre)
            .collect(Collectors.toList());
    }
    
    // Helper method to check if ISBN is duplicate
    private boolean isISBNDuplicate(String isbn) {
        for (Book book : books.values()) {
            if (book.getIsbn().equalsIgnoreCase(isbn)) {
                return true;
            }
        }
        return false;
    }
    
    // Helper method to validate book data
    private void validateBook(Book book) {
        // Check required fields are not empty
        if (book.getIsbn() == null || book.getIsbn().trim().isEmpty()) {
            throw new IllegalArgumentException("ISBN is required");
        }
        if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Title is required");
        }
        if (book.getAuthor() == null || book.getAuthor().trim().isEmpty()) {
            throw new IllegalArgumentException("Author is required");
        }
        
        // Check ISBN format (basic: 10 or 13 digits)
        String isbnDigits = book.getIsbn().replaceAll("[^0-9]", "");
        if (isbnDigits.length() != 10 && isbnDigits.length() != 13) {
            throw new IllegalArgumentException("ISBN must be 10 or 13 digits");
        }
        
        // Check publication year is valid
        int currentYear = java.time.Year.now().getValue();
        if (book.getPublicationYear() < 1000 || book.getPublicationYear() > currentYear) {
            throw new IllegalArgumentException("Invalid publication year: " + book.getPublicationYear());
        }
        
        // Check quantities are valid
        if (book.getTotalQuantity() < 0) {
            throw new IllegalArgumentException("Total quantity cannot be negative");
        }
        if (book.getAvailableQuantity() < 0) {
            throw new IllegalArgumentException("Available quantity cannot be negative");
        }
        if (book.getAvailableQuantity() > book.getTotalQuantity()) {
            throw new IllegalArgumentException("Available quantity cannot exceed total quantity");
        }
    }
}
