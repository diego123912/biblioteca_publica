package co.edu.umanizales.biblioteca_publica.service;

import co.edu.umanizales.biblioteca_publica.model.Autor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class AutorService {
    
    private final CSVService csvService;
    private final Map<String, Autor> autores = new ConcurrentHashMap<>();
    private static final String FILE_NAME = "autores.csv";

    public AutorService(CSVService csvService) {
        this.csvService = csvService;
        loadFromCSV();
    }

    private void loadFromCSV() {
        try {
            List<List<String>> data = csvService.readCSV(FILE_NAME);
            for (List<String> row : data) {
                if (row.size() >= 6) {
                    Autor autor = new Autor(
                        row.get(0), // id
                        row.get(1), // nombre
                        row.get(2), // apellido
                        row.get(3), // nacionalidad
                        LocalDate.parse(row.get(4)), // fechaNacimiento
                        row.get(5)  // biografia
                    );
                    autores.put(autor.getId(), autor);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al cargar autores desde CSV: " + e.getMessage());
        }
    }

    private void saveToCSV() {
        try {
            List<String> headers = Arrays.asList("id", "nombre", "apellido", "nacionalidad", 
                "fechaNacimiento", "biografia");
            
            List<List<String>> data = autores.values().stream()
                .map(autor -> Arrays.asList(
                    autor.getId(),
                    autor.getNombre(),
                    autor.getApellido(),
                    autor.getNacionalidad(),
                    autor.getFechaNacimiento().toString(),
                    csvService.escapeCSV(autor.getBiografia())
                ))
                .collect(Collectors.toList());
            
            csvService.writeCSV(FILE_NAME, headers, data);
        } catch (IOException e) {
            System.err.println("Error al guardar autores en CSV: " + e.getMessage());
        }
    }

    public Autor crear(Autor autor) {
        if (autor.getId() == null || autor.getId().isEmpty()) {
            autor.setId(UUID.randomUUID().toString());
        }
        autores.put(autor.getId(), autor);
        saveToCSV();
        return autor;
    }

    public List<Autor> obtenerTodos() {
        return new ArrayList<>(autores.values());
    }

    public Optional<Autor> obtenerPorId(String id) {
        return Optional.ofNullable(autores.get(id));
    }

    public Autor actualizar(String id, Autor autorActualizado) {
        if (autores.containsKey(id)) {
            autorActualizado.setId(id);
            autores.put(id, autorActualizado);
            saveToCSV();
            return autorActualizado;
        }
        return null;
    }

    public boolean eliminar(String id) {
        if (autores.remove(id) != null) {
            saveToCSV();
            return true;
        }
        return false;
    }

    public List<Autor> buscarPorNombre(String nombre) {
        return autores.values().stream()
            .filter(autor -> autor.getNombre().toLowerCase().contains(nombre.toLowerCase()) ||
                           autor.getApellido().toLowerCase().contains(nombre.toLowerCase()))
            .collect(Collectors.toList());
    }
}
