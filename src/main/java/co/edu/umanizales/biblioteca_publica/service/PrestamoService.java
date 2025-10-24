package co.edu.umanizales.biblioteca_publica.service;

import co.edu.umanizales.biblioteca_publica.enums.LoanStatus;
import co.edu.umanizales.biblioteca_publica.model.Book;
import co.edu.umanizales.biblioteca_publica.model.Loan;
import co.edu.umanizales.biblioteca_publica.model.User;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class LoanService {
    
    private final CSVService csvService;
    private final BookService bookService;
    private final UserService userService;
    private final Map<String, Loan> loans = new ConcurrentHashMap<>();
    private static final String FILE_NAME = "loans.csv";

    public LoanService(CSVService csvService, BookService bookService, UserService userService) {
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
                    Loan loan = new Loan(
                        row.get(0), // id
                        row.get(1), // userId
                        row.get(2), // bookId
                        LocalDate.parse(row.get(3)), // loanDate
                        LocalDate.parse(row.get(4)), // estimatedReturnDate
                        row.get(5).isEmpty() ? null : LocalDate.parse(row.get(5)), // actualReturnDate
                        LoanStatus.valueOf(row.get(6)), // status
                        row.size() > 7 ? row.get(7) : "" // observations
                    );
                    loans.put(loan.getId(), loan);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading loans from CSV: " + e.getMessage());
        }
    }

    private void saveToCSV() {
        try {
            List<String> headers = Arrays.asList("id", "userId", "bookId", "loanDate", 
                "estimatedReturnDate", "actualReturnDate", "status", "observations");
            
            List<List<String>> data = loans.values().stream()
                .map(loan -> Arrays.asList(
                    loan.getId(),
                    loan.getUserId(),
                    loan.getBookId(),
                    loan.getLoanDate().toString(),
                    loan.getEstimatedReturnDate().toString(),
                    loan.getActualReturnDate() != null ? loan.getActualReturnDate().toString() : "",
                    loan.getStatus().toString(),
                    loan.getObservations() != null ? loan.getObservations() : ""
                ))
                .collect(Collectors.toList());
            
            csvService.writeCSV(FILE_NAME, headers, data);
        } catch (IOException e) {
            System.err.println("Error saving loans to CSV: " + e.getMessage());
        }
    }

    // Polymorphism: method that manages loans using the polymorphic getLoanDays() method
    public Loan performLoan(String userId, String bookId) {
        Optional<User> userOpt = userService.getById(userId);
        Optional<Book> bookOpt = bookService.getById(bookId);

        if (userOpt.isEmpty() || bookOpt.isEmpty()) {
            throw new RuntimeException("User or book not found");
        }

        User user = userOpt.get();
        Book book = bookOpt.get();

        if (!book.isAvailable()) {
            throw new RuntimeException("Book not available");
        }

        long activeLoans = loans.values().stream()
            .filter(p -> p.getUserId().equals(userId))
            .filter(p -> p.getStatus() == LoanStatus.ACTIVE || p.getStatus() == LoanStatus.OVERDUE)
            .count();

        if (activeLoans >= user.getLoanLimit()) {
            throw new RuntimeException("User has reached the loan limit");
        }

        String id = UUID.randomUUID().toString();
        LocalDate loanDate = LocalDate.now();
        LocalDate returnDate = loanDate.plusDays(user.getLoanDays()); // Polymorphism

        Loan loan = new Loan(id, userId, bookId, loanDate, returnDate);
        book.borrow();
        bookService.update(bookId, book);
        
        loans.put(id, loan);
        saveToCSV();

        // Send notification (Polymorphism)
        user.sendNotification("Loan performed: " + book.getTitle() + ". Return date: " + returnDate);

        return loan;
    }

    // Polymorphism: method that manages returns
    public Loan performReturn(String loanId) {
        Optional<Loan> loanOpt = getById(loanId);
        
        if (loanOpt.isEmpty()) {
            throw new RuntimeException("Loan not found");
        }

        Loan loan = loanOpt.get();
        Optional<Book> bookOpt = bookService.getById(loan.getBookId());
        Optional<User> userOpt = userService.getById(loan.getUserId());

        if (bookOpt.isEmpty() || userOpt.isEmpty()) {
            throw new RuntimeException("Book or user not found");
        }

        Book book = bookOpt.get();
        User user = userOpt.get();

        loan.returnBook();
        book.returnBook();
        bookService.update(book.getId(), book);
        
        loans.put(loanId, loan);
        saveToCSV();

        // Send notification (Polymorphism)
        String message = "Return performed: " + book.getTitle();
        if (loan.getDelayDays() > 0) {
            message += ". Delay days: " + loan.getDelayDays();
        }
        user.sendNotification(message);

        return loan;
    }

    public Loan create(Loan loan) {
        if (loan.getId() == null || loan.getId().isEmpty()) {
            loan.setId(UUID.randomUUID().toString());
        }
        loans.put(loan.getId(), loan);
        saveToCSV();
        return loan;
    }

    public List<Loan> getAll() {
        return new ArrayList<>(loans.values());
    }

    public Optional<Loan> getById(String id) {
        return Optional.ofNullable(loans.get(id));
    }

    public Loan update(String id, Loan updatedLoan) {
        if (loans.containsKey(id)) {
            updatedLoan.setId(id);
            loans.put(id, updatedLoan);
            saveToCSV();
            return updatedLoan;
        }
        return null;
    }

    public boolean delete(String id) {
        if (loans.remove(id) != null) {
            saveToCSV();
            return true;
        }
        return false;
    }

    public List<Loan> getByUser(String userId) {
        return loans.values().stream()
            .filter(p -> p.getUserId().equals(userId))
            .collect(Collectors.toList());
    }

    public List<Loan> getByStatus(LoanStatus status) {
        return loans.values().stream()
            .filter(p -> p.getStatus() == status)
            .collect(Collectors.toList());
    }

    public void checkOverdue() {
        loans.values().forEach(loan -> {
            if (loan.isOverdue()) {
                loan.markOverdue();
                
                Optional<User> userOpt = userService.getById(loan.getUserId());
                Optional<Book> bookOpt = bookService.getById(loan.getBookId());
                
                if (userOpt.isPresent() && bookOpt.isPresent()) {
                    User user = userOpt.get();
                    Book book = bookOpt.get();
                    user.sendNotification("Loan overdue: " + book.getTitle() + ". Delay days: " + loan.getDelayDays());
                }
            }
        });
        saveToCSV();


    }
}
