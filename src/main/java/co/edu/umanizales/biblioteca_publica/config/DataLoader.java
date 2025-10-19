package co.edu.umanizales.biblioteca_publica.config;

import co.edu.umanizales.biblioteca_publica.enums.BookGenre;
import co.edu.umanizales.biblioteca_publica.model.*;
import co.edu.umanizales.biblioteca_publica.service.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Carga datos iniciales de ejemplo para facilitar las pruebas de la API.
 * Se ejecuta automáticamente al iniciar la aplicación.
 */
@Component
public class DataLoader implements CommandLineRunner {

    private final LibroService libroService;
    private final UsuarioService usuarioService;
    private final BibliotecaService bibliotecaService;

    public DataLoader(LibroService libroService, 
                     UsuarioService usuarioService,
                     BibliotecaService bibliotecaService) {
        this.libroService = libroService;
        this.usuarioService = usuarioService;
        this.bibliotecaService = bibliotecaService;
    }

    @Override
    public void run(String... args) throws Exception {
        // Solo cargar datos si no hay registros existentes
        if (libroService.obtenerTodos().isEmpty()) {
            cargarDatosIniciales();
        }
    }

    private void cargarDatosIniciales() {
        System.out.println("Cargando datos iniciales...");

        // Crear biblioteca
        Biblioteca biblioteca = new Biblioteca(
            "BIB001",
            "Biblioteca Central",
            "Calle 50 # 23-45",
            "8781234",
            "Lunes a Viernes 8:00 AM - 6:00 PM"
        );
        bibliotecaService.crear(biblioteca);

        // Crear estudiantes de ejemplo
        Estudiante estudiante1 = new Estudiante(
            "EST001",
            "María",
            "González",
            "maria.gonzalez@example.com",
            "3001234567",
            "Ingeniería de Sistemas",
            "6"
        );
        usuarioService.crear(estudiante1);

        Estudiante estudiante2 = new Estudiante(
            "EST002",
            "Carlos",
            "Rodríguez",
            "carlos.rodriguez@example.com",
            "3009876543",
            "Medicina",
            "3"
        );
        usuarioService.crear(estudiante2);

        // Crear profesor de ejemplo
        Profesor profesor = new Profesor(
            "PROF001",
            "Ana",
            "Martínez",
            "ana.martinez@example.com",
            "3105551234",
            "Departamento de Ciencias",
            "Física Cuántica"
        );
        usuarioService.crear(profesor);

        // Crear administrador
        Administrador admin = new Administrador(
            "ADM001",
            "Luis",
            "Sánchez",
            "luis.sanchez@example.com",
            "3207778899",
            "Administrador General",
            true
        );
        usuarioService.crear(admin);

        // Crear libros de ejemplo
        Libro libro1 = new Libro(
            "LIB001",
            "978-3-16-148410-0",
            "Cien Años de Soledad",
            "Gabriel García Márquez",
            "Editorial Sudamericana",
            1967,
            BookGenre.FICCION,
            3,
            3,
            "Estante A1"
        );
        libroService.crear(libro1);

        Libro libro2 = new Libro(
            "LIB002",
            "978-0-7432-7356-5",
            "1984",
            "George Orwell",
            "Secker & Warburg",
            1949,
            BookGenre.FICCION,
            2,
            2,
            "Estante A2"
        );
        libroService.crear(libro2);

        Libro libro3 = new Libro(
            "LIB003",
            "978-0-674-97755-4",
            "Una Breve Historia del Tiempo",
            "Stephen Hawking",
            "Bantam Books",
            1988,
            BookGenre.CIENCIA,
            4,
            4,
            "Estante B1"
        );
        libroService.crear(libro3);

        Libro libro4 = new Libro(
            "LIB004",
            "978-0-13-110362-7",
            "Clean Code",
            "Robert C. Martin",
            "Prentice Hall",
            2008,
            BookGenre.TECNOLOGIA,
            5,
            5,
            "Estante C3"
        );
        libroService.crear(libro4);

        Libro libro5 = new Libro(
            "LIB005",
            "978-0-201-63361-0",
            "Design Patterns",
            "Gang of Four",
            "Addison-Wesley",
            1994,
            BookGenre.TECNOLOGIA,
            3,
            3,
            "Estante C3"
        );
        libroService.crear(libro5);

        System.out.println("✓ Datos iniciales cargados exitosamente");
        System.out.println("  - " + usuarioService.obtenerTodos().size() + " usuarios");
        System.out.println("  - " + libroService.obtenerTodos().size() + " libros");
        System.out.println("  - " + bibliotecaService.obtenerTodos().size() + " biblioteca");
    }
}
