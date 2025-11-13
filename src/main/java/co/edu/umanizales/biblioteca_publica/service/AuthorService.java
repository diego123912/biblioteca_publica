package co.edu.umanizales.biblioteca_publica.service;

import co.edu.umanizales.biblioteca_publica.model.Author;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AuthorService {
    
    private final CSVService csvService;
    private final Map<String, Author> authors = new ConcurrentHashMap<>();
    private static final String FILE_NAME = "authors.csv";

    public AuthorService(CSVService csvService) {
        this.csvService = csvService;
        loadFromCSV();
    }

    private void loadFromCSV() {
        try {
            List<List<String>> data = csvService.readCSV(FILE_NAME);
            for (List<String> row : data) {
                if (row.size() >= 6) {
                    Author author = new Author(
                        row.get(0), // id
                        row.get(1), // firstName
                        row.get(2), // lastName
                        row.get(3), // nationality
                        LocalDate.parse(row.get(4)), // birthDate
                        row.get(5)  // biography
                    );
                    authors.put(author.getId(), author);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading authors from CSV: " + e.getMessage());
        }
    }

    private void saveToCSV() {
        try {
            List<String> headers = Arrays.asList("id", "firstName", "lastName", "nationality", 
                "birthDate", "biography");
            
            List<List<String>> data = new ArrayList<>();
            for (Author author : authors.values()) {
                data.add(Arrays.asList(
                    author.getId(),
                    author.getFirstName(),
                    author.getLastName(),
                    author.getNationality(),
                    author.getBirthDate().toString(),
                    csvService.escapeCSV(author.getBiography())
                ));
            }
            
            csvService.writeCSV(FILE_NAME, headers, data);
        } catch (IOException e) {
            System.err.println("Error saving authors to CSV: " + e.getMessage());
        }
    }

    public Author create(Author author) {
        if (author.getId() == null || author.getId().isEmpty()) {
            author.setId(UUID.randomUUID().toString());
        }
        
        // Validate author data
        validateAuthor(author);
        
        // Check if author name already exists
        if (isAuthorNameDuplicate(author.getFirstName(), author.getLastName())) {
            throw new IllegalArgumentException("Author already exists: " + author.getFirstName() + " " + author.getLastName());
        }
        
        authors.put(author.getId(), author);
        saveToCSV();
        return author;
    }

    public List<Author> getAll() {
        return new ArrayList<>(authors.values());
    }

    public Author getById(String id) {
        return authors.get(id);
    }

    public Author update(String id, Author updatedAuthor) {
        if (authors.containsKey(id)) {
            updatedAuthor.setId(id);
            authors.put(id, updatedAuthor);
            saveToCSV();
            return updatedAuthor;
        }
        return null;
    }

    public boolean delete(String id) {
        if (authors.remove(id) != null) {
            saveToCSV();
            return true;
        }
        return false;
    }

    public List<Author> searchByName(String name) {
        List<Author> result = new ArrayList<>();
        for (Author author : authors.values()) {
            if (author.getFirstName().toLowerCase().contains(name.toLowerCase()) ||
                author.getLastName().toLowerCase().contains(name.toLowerCase())) {
                result.add(author);
            }
        }
        return result;
    }
    
    // Helper method to check if author name is duplicate
    private boolean isAuthorNameDuplicate(String firstName, String lastName) {
        for (Author author : authors.values()) {
            if (author.getFirstName().equalsIgnoreCase(firstName) && 
                author.getLastName().equalsIgnoreCase(lastName)) {
                return true;
            }
        }
        return false;
    }
    
    // Helper method to validate author data
    private void validateAuthor(Author author) {
        // Check required fields are not empty
        if (author.getFirstName() == null || author.getFirstName().trim().isEmpty()) {
            throw new IllegalArgumentException("First name is required");
        }
        if (author.getLastName() == null || author.getLastName().trim().isEmpty()) {
            throw new IllegalArgumentException("Last name is required");
        }
        if (author.getNationality() == null || author.getNationality().trim().isEmpty()) {
            throw new IllegalArgumentException("Nationality is required");
        }
        if (author.getBirthDate() == null) {
            throw new IllegalArgumentException("Birth date is required");
        }
        
        // Check birth date is not in the future
        if (author.getBirthDate().isAfter(java.time.LocalDate.now())) {
            throw new IllegalArgumentException("Birth date cannot be in the future");
        }
    }
}
