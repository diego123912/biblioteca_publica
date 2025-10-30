package co.edu.umanizales.biblioteca_publica.service;

import co.edu.umanizales.biblioteca_publica.model.Book;
import co.edu.umanizales.biblioteca_publica.model.Review;
import co.edu.umanizales.biblioteca_publica.model.User;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class ReviewService {
    
    private final CSVService csvService;
    private final BookService bookService;
    private final UserService userService;
    private final Map<String, Review> reviews = new ConcurrentHashMap<>();
    private static final String FILE_NAME = "reviews.csv";

    public ReviewService(CSVService csvService, BookService bookService, UserService userService) {
        this.csvService = csvService;
        this.bookService = bookService;
        this.userService = userService;
        loadFromCSV();
    }

    private void loadFromCSV() {
        try {
            List<List<String>> data = csvService.readCSV(FILE_NAME);
            for (List<String> row : data) {
                if (row.size() >= 6) {
                    Review review = new Review(
                        row.get(0), // id
                        row.get(1), // userId
                        row.get(2), // bookId
                        Integer.parseInt(row.get(3)), // rating
                        row.get(4), // comment
                        LocalDateTime.parse(row.get(5)), // creationDate
                        Boolean.parseBoolean(row.get(6)), // approved
                        null, // user (loaded on demand)
                        null  // book (loaded on demand)
                    );
                    reviews.put(review.getId(), review);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading reviews from CSV: " + e.getMessage());
        }
    }

    private void saveToCSV() {
        try {
            List<String> headers = Arrays.asList("id", "userId", "bookId", "rating", 
                "comment", "creationDate", "approved");
            
            List<List<String>> data = reviews.values().stream()
                .map(review -> Arrays.asList(
                    review.getId(),
                    review.getUserId(),
                    review.getBookId(),
                    String.valueOf(review.getRating()),
                    csvService.escapeCSV(review.getComment()),
                    review.getCreationDate().toString(),
                    String.valueOf(review.isApproved())
                ))
                .collect(Collectors.toList());
            
            csvService.writeCSV(FILE_NAME, headers, data);
        } catch (IOException e) {
            System.err.println("Error saving reviews to CSV: " + e.getMessage());
        }
    }

    public Review create(Review review) {
        if (review.getId() == null || review.getId().isEmpty()) {
            review.setId(UUID.randomUUID().toString());
        }
        
        // Composition: load user and book
        Optional<User> user = userService.getById(review.getUserId());
        Optional<Book> book = bookService.getById(review.getBookId());
        
        if (user.isPresent() && book.isPresent()) {
            review.setUser(user.get());
            review.setBook(book.get());
        }
        
        reviews.put(review.getId(), review);
        saveToCSV();
        return review;
    }

    public List<Review> getAll() {
        return new ArrayList<>(reviews.values());
    }

    public Optional<Review> getById(String id) {
        Optional<Review> reviewOpt = Optional.ofNullable(reviews.get(id));
        
        // Load composition
        if (reviewOpt.isPresent()) {
            Review review = reviewOpt.get();
            loadComposition(review);
        }
        
        return reviewOpt;
    }

    private void loadComposition(Review review) {
        if (review.getUser() == null) {
            userService.getById(review.getUserId()).ifPresent(review::setUser);
        }
        if (review.getBook() == null) {
            bookService.getById(review.getBookId()).ifPresent(review::setBook);
        }
    }

    public Review update(String id, Review updatedReview) {
        if (reviews.containsKey(id)) {
            updatedReview.setId(id);
            reviews.put(id, updatedReview);
            saveToCSV();
            return updatedReview;
        }
        return null;
    }

    public boolean delete(String id) {
        if (reviews.remove(id) != null) {
            saveToCSV();
            return true;
        }
        return false;
    }

    public List<Review> getByBook(String bookId) {
        return reviews.values().stream()
            .filter(r -> r.getBookId().equals(bookId))
            .peek(this::loadComposition)
            .collect(Collectors.toList());
    }

    public List<Review> getByUser(String userId) {
        return reviews.values().stream()
            .filter(r -> r.getUserId().equals(userId))
            .peek(this::loadComposition)
            .collect(Collectors.toList());
    }

    public List<Review> getApproved() {
        return reviews.values().stream()
            .filter(Review::isApproved)
            .peek(this::loadComposition)
            .collect(Collectors.toList());
    }

    public Review approveReview(String id) {
        Optional<Review> reviewOpt = getById(id);
        if (reviewOpt.isPresent()) {
            Review review = reviewOpt.get();
            review.approve();
            reviews.put(id, review);
            saveToCSV();
            return review;
        }
        return null;
    }

    public double getAverageRatingBook(String bookId) {
        return reviews.values().stream()
            .filter(r -> r.getBookId().equals(bookId) && r.isApproved())
            .mapToInt(Review::getRating)
            .average()
            .orElse(0.0);
    }
}
