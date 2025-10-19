package co.edu.umanizales.biblioteca_publica.service;

import co.edu.umanizales.biblioteca_publica.model.Editorial;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class EditorialService {
    
    private final CSVService csvService;
    private final Map<String, Editorial> editoriales = new ConcurrentHashMap<>();
    private static final String FILE_NAME = "editoriales.csv";

    public EditorialService(CSVService csvService) {
        this.csvService = csvService;
        loadFromCSV();
    }

    private void loadFromCSV() {
        try {
            List<List<String>> data = csvService.readCSV(FILE_NAME);
            for (List<String> row : data) {
                if (row.size() >= 5) {
                    Editorial editorial = new Editorial(
                        row.get(0), // id
                        row.get(1), // nombre
                        row.get(2), // pais
                        row.get(3), // sitioWeb
                        row.get(4)  // contacto
                    );
                    editoriales.put(editorial.getId(), editorial);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al cargar editoriales desde CSV: " + e.getMessage());
        }
    }

    private void saveToCSV() {
        try {
            List<String> headers = Arrays.asList("id", "nombre", "pais", "sitioWeb", "contacto");
            
            List<List<String>> data = editoriales.values().stream()
                .map(editorial -> Arrays.asList(
                    editorial.getId(),
                    editorial.getNombre(),
                    editorial.getPais(),
                    editorial.getSitioWeb(),
                    editorial.getContacto()
                ))
                .collect(Collectors.toList());
            
            csvService.writeCSV(FILE_NAME, headers, data);
        } catch (IOException e) {
            System.err.println("Error al guardar editoriales en CSV: " + e.getMessage());
        }
    }

    public Editorial crear(Editorial editorial) {
        if (editorial.getId() == null || editorial.getId().isEmpty()) {
            editorial.setId(UUID.randomUUID().toString());
        }
        editoriales.put(editorial.getId(), editorial);
        saveToCSV();
        return editorial;
    }

    public List<Editorial> obtenerTodos() {
        return new ArrayList<>(editoriales.values());
    }

    public Optional<Editorial> obtenerPorId(String id) {
        return Optional.ofNullable(editoriales.get(id));
    }

    public Editorial actualizar(String id, Editorial editorialActualizada) {
        if (editoriales.containsKey(id)) {
            editorialActualizada.setId(id);
            editoriales.put(id, editorialActualizada);
            saveToCSV();
            return editorialActualizada;
        }
        return null;
    }

    public boolean eliminar(String id) {
        if (editoriales.remove(id) != null) {
            saveToCSV();
            return true;
        }
        return false;
    }

    public List<Editorial> buscarPorNombre(String nombre) {
        return editoriales.values().stream()
            .filter(editorial -> editorial.getNombre().toLowerCase().contains(nombre.toLowerCase()))
            .collect(Collectors.toList());
    }
}
