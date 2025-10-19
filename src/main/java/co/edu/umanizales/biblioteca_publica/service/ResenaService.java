package co.edu.umanizales.biblioteca_publica.service;

import co.edu.umanizales.biblioteca_publica.model.Libro;
import co.edu.umanizales.biblioteca_publica.model.Resena;
import co.edu.umanizales.biblioteca_publica.model.Usuario;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class ResenaService {
    
    private final CSVService csvService;
    private final LibroService libroService;
    private final UsuarioService usuarioService;
    private final Map<String, Resena> resenas = new ConcurrentHashMap<>();
    private static final String FILE_NAME = "resenas.csv";

    public ResenaService(CSVService csvService, LibroService libroService, UsuarioService usuarioService) {
        this.csvService = csvService;
        this.libroService = libroService;
        this.usuarioService = usuarioService;
        loadFromCSV();
    }

    private void loadFromCSV() {
        try {
            List<List<String>> data = csvService.readCSV(FILE_NAME);
            for (List<String> row : data) {
                if (row.size() >= 6) {
                    Resena resena = new Resena(
                        row.get(0), // id
                        row.get(1), // usuarioId
                        row.get(2), // libroId
                        Integer.parseInt(row.get(3)), // calificacion
                        row.get(4), // comentario
                        LocalDateTime.parse(row.get(5)), // fechaCreacion
                        Boolean.parseBoolean(row.get(6)), // aprobada
                        null, // usuario (se carga bajo demanda)
                        null  // libro (se carga bajo demanda)
                    );
                    resenas.put(resena.getId(), resena);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al cargar reseñas desde CSV: " + e.getMessage());
        }
    }

    private void saveToCSV() {
        try {
            List<String> headers = Arrays.asList("id", "usuarioId", "libroId", "calificacion", 
                "comentario", "fechaCreacion", "aprobada");
            
            List<List<String>> data = resenas.values().stream()
                .map(resena -> Arrays.asList(
                    resena.getId(),
                    resena.getUsuarioId(),
                    resena.getLibroId(),
                    String.valueOf(resena.getCalificacion()),
                    csvService.escapeCSV(resena.getComentario()),
                    resena.getFechaCreacion().toString(),
                    String.valueOf(resena.isAprobada())
                ))
                .collect(Collectors.toList());
            
            csvService.writeCSV(FILE_NAME, headers, data);
        } catch (IOException e) {
            System.err.println("Error al guardar reseñas en CSV: " + e.getMessage());
        }
    }

    public Resena crear(Resena resena) {
        if (resena.getId() == null || resena.getId().isEmpty()) {
            resena.setId(UUID.randomUUID().toString());
        }
        
        // Composición: cargar usuario y libro
        Optional<Usuario> usuario = usuarioService.obtenerPorId(resena.getUsuarioId());
        Optional<Libro> libro = libroService.obtenerPorId(resena.getLibroId());
        
        if (usuario.isPresent() && libro.isPresent()) {
            resena.setUsuario(usuario.get());
            resena.setLibro(libro.get());
        }
        
        resenas.put(resena.getId(), resena);
        saveToCSV();
        return resena;
    }

    public List<Resena> obtenerTodos() {
        return new ArrayList<>(resenas.values());
    }

    public Optional<Resena> obtenerPorId(String id) {
        Optional<Resena> resenaOpt = Optional.ofNullable(resenas.get(id));
        
        // Cargar composición
        if (resenaOpt.isPresent()) {
            Resena resena = resenaOpt.get();
            cargarComposicion(resena);
        }
        
        return resenaOpt;
    }

    private void cargarComposicion(Resena resena) {
        if (resena.getUsuario() == null) {
            usuarioService.obtenerPorId(resena.getUsuarioId()).ifPresent(resena::setUsuario);
        }
        if (resena.getLibro() == null) {
            libroService.obtenerPorId(resena.getLibroId()).ifPresent(resena::setLibro);
        }
    }

    public Resena actualizar(String id, Resena resenaActualizada) {
        if (resenas.containsKey(id)) {
            resenaActualizada.setId(id);
            resenas.put(id, resenaActualizada);
            saveToCSV();
            return resenaActualizada;
        }
        return null;
    }

    public boolean eliminar(String id) {
        if (resenas.remove(id) != null) {
            saveToCSV();
            return true;
        }
        return false;
    }

    public List<Resena> obtenerPorLibro(String libroId) {
        return resenas.values().stream()
            .filter(r -> r.getLibroId().equals(libroId))
            .peek(this::cargarComposicion)
            .collect(Collectors.toList());
    }

    public List<Resena> obtenerPorUsuario(String usuarioId) {
        return resenas.values().stream()
            .filter(r -> r.getUsuarioId().equals(usuarioId))
            .peek(this::cargarComposicion)
            .collect(Collectors.toList());
    }

    public List<Resena> obtenerAprobadas() {
        return resenas.values().stream()
            .filter(Resena::isAprobada)
            .peek(this::cargarComposicion)
            .collect(Collectors.toList());
    }

    public Resena aprobarResena(String id) {
        Optional<Resena> resenaOpt = obtenerPorId(id);
        if (resenaOpt.isPresent()) {
            Resena resena = resenaOpt.get();
            resena.aprobar();
            resenas.put(id, resena);
            saveToCSV();
            return resena;
        }
        return null;
    }

    public double obtenerCalificacionPromedioLibro(String libroId) {
        return resenas.values().stream()
            .filter(r -> r.getLibroId().equals(libroId) && r.isAprobada())
            .mapToInt(Resena::getCalificacion)
            .average()
            .orElse(0.0);
    }
}
