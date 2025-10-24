package co.edu.umanizales.biblioteca_publica.config;

import co.edu.umanizales.biblioteca_publica.enums.BookGenre;
import co.edu.umanizales.biblioteca_publica.model.*;
import co.edu.umanizales.biblioteca_publica.service.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Loads initial sample data to facilitate API testing.
 * Runs automatically when the application starts.
 */
@Component
public class DataLoader implements CommandLineRunner {

    private final BookService bookService;
    private final UserService userService;
    private final LibraryService libraryService;

    public DataLoader(BookService bookService, 
                     UserService userService,
                     LibraryService libraryService) {
        this.bookService = bookService;
        this.userService = userService;
        this.libraryService = libraryService;
    }

    @Override
    public void run(String... args) throws Exception {
        // Only load data if there are no existing records
        if (bookService.getAll().isEmpty()) {
            loadInitialData();
        }
    }

    private void loadInitialData() {
        System.out.println("Loading initial data...");

        // Create library
        Library library = new Library(
            "LIB001",
            "Central Library",
            "Street 50 # 23-45",
            "8781234",
            "Monday to Friday 8:00 AM - 6:00 PM"
        );
        libraryService.create(library);

        // Create sample students
        Student student1 = new Student(
            "STU001",
            "Maria",
            "Gonzalez",
            "maria.gonzalez@example.com",
            "3001234567",
            "Systems Engineering",
            "6"
        );
        userService.create(student1);

        Student student2 = new Student(
            "STU002",
            "Carlos",
            "Rodriguez",
            "carlos.rodriguez@example.com",
            "3009876543",
            "Medicine",
            "3"
        );
        userService.create(student2);

        // Create sample teacher
        Teacher teacher = new Teacher(
            "TCH001",
            "Ana",
            "Martinez",
            "ana.martinez@example.com",
            "3105551234",
            "Department of Sciences",
            "Quantum Physics"
        );
        userService.create(teacher);

        // Create administrator
        Administrator admin = new Administrator(
            "ADM001",
            "Luis",
            "Sanchez",
            "luis.sanchez@example.com",
            "3207778899",
            "General Administrator",
            true
        );
        userService.create(admin);

        // Create sample books
        Book book1 = new Book(
            "BK001",
            "978-3-16-148410-0",
            "One Hundred Years of Solitude",
            "Gabriel Garcia Marquez",
            "Sudamericana Publishing",
            1967,
            BookGenre.FICTION,
            3,
            3,
            "Shelf A1"
        );
        bookService.create(book1);

        Book book2 = new Book(
            "BK002",
            "978-0-7432-7356-5",
            "1984",
            "George Orwell",
            "Secker & Warburg",
            1949,
            BookGenre.FICTION,
            2,
            2,
            "Shelf A2"
        );
        bookService.create(book2);

        Book book3 = new Book(
            "BK003",
            "978-0-674-97755-4",
            "A Brief History of Time",
            "Stephen Hawking",
            "Bantam Books",
            1988,
            BookGenre.SCIENCE,
            4,
            4,
            "Shelf B1"
        );
        bookService.create(book3);

        Book book4 = new Book(
            "BK004",
            "978-0-13-110362-7",
            "Clean Code",
            "Robert C. Martin",
            "Prentice Hall",
            2008,
            BookGenre.TECHNOLOGY,
            5,
            5,
            "Shelf C3"
        );
        bookService.create(book4);

        Book book5 = new Book(
            "BK005",
            "978-0-201-63361-0",
            "Design Patterns",
            "Gang of Four",
            "Addison-Wesley",
            1994,
            BookGenre.TECHNOLOGY,
            3,
            3,
            "Shelf C3"
        );
        bookService.create(book5);

        System.out.println("✓ Initial data loaded successfully");
        System.out.println("  - " + userService.getAll().size() + " users");
        System.out.println("  - " + bookService.getAll().size() + " books");
        System.out.println("  - " + libraryService.getAll().size() + " library");
    }
}
