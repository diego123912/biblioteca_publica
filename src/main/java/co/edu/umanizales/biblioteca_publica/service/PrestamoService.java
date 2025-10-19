package co.edu.umanizales.biblioteca_publica.service;

import co.edu.umanizales.biblioteca_publica.enums.LoanStatus;
import co.edu.umanizales.biblioteca_publica.model.Libro;
import co.edu.umanizales.biblioteca_publica.model.Prestamo;
import co.edu.umanizales.biblioteca_publica.model.Usuario;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class PrestamoService {
    
    private final CSVService csvService;
    private final LibroService libroService;
    private final UsuarioService usuarioService;
    private final Map<String, Prestamo> prestamos = new ConcurrentHashMap<>();
    private static final String FILE_NAME = "prestamos.csv";

    public PrestamoService(CSVService csvService, LibroService libroService, UsuarioService usuarioService) {
        this.csvService = csvService;
        this.libroService = libroService;
        this.usuarioService = usuarioService;
        loadFromCSV();
    }

    private void loadFromCSV() {
        try {
            List<List<String>> data = csvService.readCSV(FILE_NAME);
            for (List<String> row : data) {
                if (row.size() >= 7) {
                    Prestamo prestamo = new Prestamo(
                        row.get(0), // id
                        row.get(1), // usuarioId
                        row.get(2), // libroId
                        LocalDate.parse(row.get(3)), // fechaPrestamo
                        LocalDate.parse(row.get(4)), // fechaDevolucionEstimada
                        row.get(5).isEmpty() ? null : LocalDate.parse(row.get(5)), // fechaDevolucionReal
                        LoanStatus.valueOf(row.get(6)), // estado
                        row.size() > 7 ? row.get(7) : "" // observaciones
                    );
                    prestamos.put(prestamo.getId(), prestamo);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al cargar préstamos desde CSV: " + e.getMessage());
        }
    }

    private void saveToCSV() {
        try {
            List<String> headers = Arrays.asList("id", "usuarioId", "libroId", "fechaPrestamo", 
                "fechaDevolucionEstimada", "fechaDevolucionReal", "estado", "observaciones");
            
            List<List<String>> data = prestamos.values().stream()
                .map(prestamo -> Arrays.asList(
                    prestamo.getId(),
                    prestamo.getUsuarioId(),
                    prestamo.getLibroId(),
                    prestamo.getFechaPrestamo().toString(),
                    prestamo.getFechaDevolucionEstimada().toString(),
                    prestamo.getFechaDevolucionReal() != null ? prestamo.getFechaDevolucionReal().toString() : "",
                    prestamo.getEstado().toString(),
                    prestamo.getObservaciones() != null ? prestamo.getObservaciones() : ""
                ))
                .collect(Collectors.toList());
            
            csvService.writeCSV(FILE_NAME, headers, data);
        } catch (IOException e) {
            System.err.println("Error al guardar préstamos en CSV: " + e.getMessage());
        }
    }

    // Polimorfismo: método que gestiona préstamos usando el método polimórfico getDiasPrestamo()
    public Prestamo realizarPrestamo(String usuarioId, String libroId) {
        Optional<Usuario> usuarioOpt = usuarioService.obtenerPorId(usuarioId);
        Optional<Libro> libroOpt = libroService.obtenerPorId(libroId);

        if (usuarioOpt.isEmpty() || libroOpt.isEmpty()) {
            throw new RuntimeException("Usuario o libro no encontrado");
        }

        Usuario usuario = usuarioOpt.get();
        Libro libro = libroOpt.get();

        if (!libro.estaDisponible()) {
            throw new RuntimeException("Libro no disponible");
        }

        long prestamosActivos = prestamos.values().stream()
            .filter(p -> p.getUsuarioId().equals(usuarioId))
            .filter(p -> p.getEstado() == LoanStatus.ACTIVO || p.getEstado() == LoanStatus.VENCIDO)
            .count();

        if (prestamosActivos >= usuario.getLimitePrestamos()) {
            throw new RuntimeException("Usuario ha alcanzado el límite de préstamos");
        }

        String id = UUID.randomUUID().toString();
        LocalDate fechaPrestamo = LocalDate.now();
        LocalDate fechaDevolucion = fechaPrestamo.plusDays(usuario.getDiasPrestamo()); // Polimorfismo

        Prestamo prestamo = new Prestamo(id, usuarioId, libroId, fechaPrestamo, fechaDevolucion);
        libro.prestar();
        libroService.actualizar(libroId, libro);
        
        prestamos.put(id, prestamo);
        saveToCSV();

        // Enviar notificación (Polimorfismo)
        usuario.enviarNotificacion("Préstamo realizado: " + libro.getTitulo() + ". Fecha devolución: " + fechaDevolucion);

        return prestamo;
    }

    // Polimorfismo: método que gestiona devoluciones
    public Prestamo realizarDevolucion(String prestamoId) {
        Optional<Prestamo> prestamoOpt = obtenerPorId(prestamoId);
        
        if (prestamoOpt.isEmpty()) {
            throw new RuntimeException("Préstamo no encontrado");
        }

        Prestamo prestamo = prestamoOpt.get();
        Optional<Libro> libroOpt = libroService.obtenerPorId(prestamo.getLibroId());
        Optional<Usuario> usuarioOpt = usuarioService.obtenerPorId(prestamo.getUsuarioId());

        if (libroOpt.isEmpty() || usuarioOpt.isEmpty()) {
            throw new RuntimeException("Libro o usuario no encontrado");
        }

        Libro libro = libroOpt.get();
        Usuario usuario = usuarioOpt.get();

        prestamo.devolver();
        libro.devolver();
        libroService.actualizar(libro.getId(), libro);
        
        prestamos.put(prestamoId, prestamo);
        saveToCSV();

        // Enviar notificación (Polimorfismo)
        String mensaje = "Devolución realizada: " + libro.getTitulo();
        if (prestamo.getDiasRetraso() > 0) {
            mensaje += ". Días de retraso: " + prestamo.getDiasRetraso();
        }
        usuario.enviarNotificacion(mensaje);

        return prestamo;
    }

    public Prestamo crear(Prestamo prestamo) {
        if (prestamo.getId() == null || prestamo.getId().isEmpty()) {
            prestamo.setId(UUID.randomUUID().toString());
        }
        prestamos.put(prestamo.getId(), prestamo);
        saveToCSV();
        return prestamo;
    }

    public List<Prestamo> obtenerTodos() {
        return new ArrayList<>(prestamos.values());
    }

    public Optional<Prestamo> obtenerPorId(String id) {
        return Optional.ofNullable(prestamos.get(id));
    }

    public Prestamo actualizar(String id, Prestamo prestamoActualizado) {
        if (prestamos.containsKey(id)) {
            prestamoActualizado.setId(id);
            prestamos.put(id, prestamoActualizado);
            saveToCSV();
            return prestamoActualizado;
        }
        return null;
    }

    public boolean eliminar(String id) {
        if (prestamos.remove(id) != null) {
            saveToCSV();
            return true;
        }
        return false;
    }

    public List<Prestamo> obtenerPorUsuario(String usuarioId) {
        return prestamos.values().stream()
            .filter(p -> p.getUsuarioId().equals(usuarioId))
            .collect(Collectors.toList());
    }

    public List<Prestamo> obtenerPorEstado(LoanStatus estado) {
        return prestamos.values().stream()
            .filter(p -> p.getEstado() == estado)
            .collect(Collectors.toList());
    }

    public void verificarVencimientos() {
        prestamos.values().forEach(prestamo -> {
            if (prestamo.estaVencido()) {
                prestamo.marcarVencido();
                
                Optional<Usuario> usuarioOpt = usuarioService.obtenerPorId(prestamo.getUsuarioId());
                Optional<Libro> libroOpt = libroService.obtenerPorId(prestamo.getLibroId());
                
                if (usuarioOpt.isPresent() && libroOpt.isPresent()) {
                    Usuario usuario = usuarioOpt.get();
                    Libro libro = libroOpt.get();
                    usuario.enviarNotificacion("Préstamo vencido: " + libro.getTitulo() + ". Días de retraso: " + prestamo.getDiasRetraso());
                }
            }
        });
        saveToCSV();
    }
}
