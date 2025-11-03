package co.edu.umanizales.biblioteca_publica.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class Library {
    private String id;
    private String name;
    private String address;
    private String phone;
    private String schedule;
    
    // Aggregation: Library contains collections of Books and Users
    private List<Book> books = new ArrayList<>();
    private List<User> users = new ArrayList<>();
    private List<Loan> loans = new ArrayList<>();
    private List<Review> reviews = new ArrayList<>();

    public Library(String id, String name, String address, String phone, String schedule) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.schedule = schedule;
        this.books = new ArrayList<>();
        this.users = new ArrayList<>();
        this.loans = new ArrayList<>();
        this.reviews = new ArrayList<>();
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public void addUser(User user) {
        users.add(user);
    }

    public void addLoan(Loan loan) {
        loans.add(loan);
    }

    public void addReview(Review review) {
        reviews.add(review);
    }

    public int getTotalBooks() {
        return books.size();
    }

    public int getTotalUsers() {
        return users.size();
    }

    public int getActiveLoans() {
        return (int) loans.stream()
                .filter(loan -> loan.getStatus().toString().equals("ACTIVE"))
                .count();
    }
}
