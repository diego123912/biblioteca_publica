package co.edu.umanizales.biblioteca_publica.service;

import co.edu.umanizales.biblioteca_publica.enums.BookGenre;
import co.edu.umanizales.biblioteca_publica.model.Author;
import co.edu.umanizales.biblioteca_publica.model.Book;
import co.edu.umanizales.biblioteca_publica.model.Publisher;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class BookService {
    
    private final CSVService csvService;
    private final AuthorService authorService;
    private final PublisherService publisherService;
    private final Map<String, Book> books = new ConcurrentHashMap<>();
    private static final String FILE_NAME = "books.csv";

    public BookService(CSVService csvService, AuthorService authorService, PublisherService publisherService) {
        this.csvService = csvService;
        this.authorService = authorService;
        this.publisherService = publisherService;
        loadFromCSV();
    }

    private void loadFromCSV() {
        try {
            List<List<String>> data = csvService.readCSV(FILE_NAME);
            for (List<String> row : data) {
                if (row.size() >= 10) {
                    String authorId = row.get(3);
                    String publisherId = row.get(4);
                    
                    Author author = authorService.getById(authorId);
                    Publisher publisher = publisherService.getById(publisherId);
                    
                    if (author != null && publisher != null) {
                        Book book = new Book(
                            row.get(0), // id
                            row.get(1), // isbn
                            row.get(2), // title
                            author, // author
                            publisher, // publisher
                            Integer.parseInt(row.get(5)), // publicationYear
                            BookGenre.valueOf(row.get(6)), // genre
                            Integer.parseInt(row.get(7)), // availableQuantity
                            Integer.parseInt(row.get(8)), // totalQuantity
                            row.get(9)  // location
                        );
                        books.put(book.getId(), book);
                    }
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
            
            List<List<String>> data = new ArrayList<>();
            for (Book book : books.values()) {
                data.add(Arrays.asList(
                    book.getId(),
                    book.getIsbn(),
                    book.getTitle(),
                    book.getAuthor() != null ? book.getAuthor().getId() : "",
                    book.getPublisher() != null ? book.getPublisher().getId() : "",
                    String.valueOf(book.getPublicationYear()),
                    book.getGenre().toString(),
                    String.valueOf(book.getAvailableQuantity()),
                    String.valueOf(book.getTotalQuantity()),
                    book.getLocation()
                ));
            }
            
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

    public Book getById(String id) {
        return books.get(id);
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
        List<Book> result = new ArrayList<>();
        for (Book book : books.values()) {
            if (book.getTitle().toLowerCase().contains(title.toLowerCase())) {
                result.add(book);
            }
        }
        return result;
    }

    public List<Book> searchByAuthor(String author) {
        List<Book> result = new ArrayList<>();
        for (Book book : books.values()) {
            if (book.getAuthor() != null) {
                if (book.getAuthor().getFirstName().toLowerCase().contains(author.toLowerCase()) ||
                    book.getAuthor().getLastName().toLowerCase().contains(author.toLowerCase())) {
                    result.add(book);
                }
            }
        }
        return result;
    }

    public List<Book> searchByGenre(BookGenre genre) {
        List<Book> result = new ArrayList<>();
        for (Book book : books.values()) {
            if (book.getGenre() == genre) {
                result.add(book);
            }
        }
        return result;
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
        if (book.getAuthor() == null) {
            throw new IllegalArgumentException("Author is required");
        }
        if (book.getPublisher() == null) {
            throw new IllegalArgumentException("Publisher is required");
        }
        
        // Check ISBN format (basic: 10 or 13 digits)
        String isbnDigits = book.getIsbn().replaceAll("[^0-9]", "");
        if (isbnDigits.length() != 10 && isbnDigits.length() != 13) {
            throw new IllegalArgumentException("ISBN must be 10 or 13 digits (found: " + isbnDigits.length() + ")");
        }
        
        // Check publication year is valid
        int currentYear = java.time.Year.now().getValue();
        if (book.getPublicationYear() < 1000 || book.getPublicationYear() > currentYear) {
            throw new IllegalArgumentException("Invalid publication year: " + book.getPublicationYear() + " (must be between 1000 and " + currentYear + ")");
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
