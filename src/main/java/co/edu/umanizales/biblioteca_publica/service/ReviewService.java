package co.edu.umanizales.biblioteca_publica.service;

import co.edu.umanizales.biblioteca_publica.model.Book;
import co.edu.umanizales.biblioteca_publica.model.Review;
import co.edu.umanizales.biblioteca_publica.model.User;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

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
                if (row.size() >= 7) {
                    String userId = row.get(1);
                    String bookId = row.get(2);
                    
                    User user = userService.getById(userId);
                    Book book = bookService.getById(bookId);
                    
                    if (user != null && book != null) {
                        Review review = new Review(
                            row.get(0), // id
                            user, // user
                            book, // book
                            Integer.parseInt(row.get(3)), // rating
                            row.get(4), // comment
                            LocalDateTime.parse(row.get(5)), // creationDate
                            Boolean.parseBoolean(row.get(6)) // approved
                        );
                        reviews.put(review.getId(), review);
                    }
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
            
            List<List<String>> data = new ArrayList<>();
            for (Review review : reviews.values()) {
                data.add(Arrays.asList(
                    review.getId(),
                    review.getUser() != null ? review.getUser().getId() : "",
                    review.getBook() != null ? review.getBook().getId() : "",
                    String.valueOf(review.getRating()),
                    csvService.escapeCSV(review.getComment()),
                    review.getCreationDate().toString(),
                    String.valueOf(review.isApproved())
                ));
            }
            
            csvService.writeCSV(FILE_NAME, headers, data);
        } catch (IOException e) {
            System.err.println("Error saving reviews to CSV: " + e.getMessage());
        }
    }

    public Review create(Review review) {
        if (review.getId() == null || review.getId().isEmpty()) {
            review.setId(UUID.randomUUID().toString());
        }
        
        // Validate review data
        validateReview(review);
        
        // Check if user already reviewed this book
        if (review.getUser() != null && review.getBook() != null && 
            hasUserReviewedBook(review.getUser().getId(), review.getBook().getId())) {
            throw new IllegalArgumentException("User already reviewed this book");
        }
        
        // Validate user and book exist
        if (review.getUser() == null) {
            throw new IllegalArgumentException("User is required");
        }
        if (review.getBook() == null) {
            throw new IllegalArgumentException("Book is required");
        }
        
        reviews.put(review.getId(), review);
        saveToCSV();
        return review;
    }

    public Review createReview(String userId, String bookId, int rating, String comment) {
        User user = userService.getById(userId);
        if (user == null) {
            throw new RuntimeException("User not found with id: " + userId);
        }

        Book book = bookService.getById(bookId);
        if (book == null) {
            throw new RuntimeException("Book not found with id: " + bookId);
        }

        // Check if user already reviewed this book
        if (hasUserReviewedBook(userId, bookId)) {
            throw new RuntimeException("User already reviewed this book");
        }

        // Validate rating
        if (rating < 1 || rating > 5) {
            throw new RuntimeException("Rating must be between 1 and 5");
        }

        // Validate comment
        if (comment == null || comment.trim().isEmpty()) {
            throw new RuntimeException("Comment is required");
        }

        String id = UUID.randomUUID().toString();
        LocalDateTime creationDate = LocalDateTime.now();

        Review review = new Review(
            id,
            user,
            book,
            rating,
            comment,
            creationDate,
            false  // Reviews start as not approved
        );

        reviews.put(id, review);
        saveToCSV();
        return review;
    }

    public List<Review> getAll() {
        return new ArrayList<>(reviews.values());
    }

    public Review getById(String id) {
        return reviews.get(id);
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
        List<Review> result = new ArrayList<>();
        for (Review r : reviews.values()) {
            if (r.getBook() != null && r.getBook().getId().equals(bookId)) {
                result.add(r);
            }
        }
        return result;
    }

    public List<Review> getByUser(String userId) {
        List<Review> result = new ArrayList<>();
        for (Review r : reviews.values()) {
            if (r.getUser() != null && r.getUser().getId().equals(userId)) {
                result.add(r);
            }
        }
        return result;
    }

    public List<Review> getApproved() {
        List<Review> result = new ArrayList<>();
        for (Review r : reviews.values()) {
            if (r.isApproved()) {
                result.add(r);
            }
        }
        return result;
    }

    public Review approveReview(String id) {
        Review review = getById(id);
        if (review != null) {
            review.approve();
            reviews.put(id, review);
            saveToCSV();
            return review;
        }
        return null;
    }

    public double getAverageRatingBook(String bookId) {
        int totalRating = 0;
        int count = 0;
        for (Review r : reviews.values()) {
            if (r.getBook() != null && r.getBook().getId().equals(bookId) && r.isApproved()) {
                totalRating += r.getRating();
                count++;
            }
        }
        return count > 0 ? (double) totalRating / count : 0.0;
    }
    
    // Helper method to check if user already reviewed a book
    private boolean hasUserReviewedBook(String userId, String bookId) {
        for (Review review : reviews.values()) {
            if (review.getUser() != null && review.getBook() != null &&
                review.getUser().getId().equals(userId) && review.getBook().getId().equals(bookId)) {
                return true;
            }
        }
        return false;
    }
    
    // Helper method to validate review data
    private void validateReview(Review review) {
        // Check required fields are not empty
        if (review.getUser() == null) {
            throw new IllegalArgumentException("User is required");
        }
        if (review.getBook() == null) {
            throw new IllegalArgumentException("Book is required");
        }
        if (review.getComment() == null || review.getComment().trim().isEmpty()) {
            throw new IllegalArgumentException("Comment is required");
        }
        
        // Check rating is in valid range (1-5)
        if (review.getRating() < 1 || review.getRating() > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
    }
}
