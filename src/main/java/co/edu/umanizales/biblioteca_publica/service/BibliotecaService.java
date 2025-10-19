package co.edu.umanizales.biblioteca_publica.service;

import co.edu.umanizales.biblioteca_publica.model.Biblioteca;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class BibliotecaService {
    
    private final CSVService csvService;
    private final Map<String, Biblioteca> bibliotecas = new ConcurrentHashMap<>();
    private static final String FILE_NAME = "bibliotecas.csv";

    public BibliotecaService(CSVService csvService) {
        this.csvService = csvService;
        loadFromCSV();
    }

    private void loadFromCSV() {
        try {
            List<List<String>> data = csvService.readCSV(FILE_NAME);
            for (List<String> row : data) {
                if (row.size() >= 5) {
                    Biblioteca biblioteca = new Biblioteca(
                        row.get(0), // id
                        row.get(1), // nombre
                        row.get(2), // direccion
                        row.get(3), // telefono
                        row.get(4)  // horario
                    );
                    bibliotecas.put(biblioteca.getId(), biblioteca);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al cargar bibliotecas desde CSV: " + e.getMessage());
        }
    }

    private void saveToCSV() {
        try {
            List<String> headers = Arrays.asList("id", "nombre", "direccion", "telefono", "horario");
            
            List<List<String>> data = bibliotecas.values().stream()
                .map(biblioteca -> Arrays.asList(
                    biblioteca.getId(),
                    biblioteca.getNombre(),
                    biblioteca.getDireccion(),
                    biblioteca.getTelefono(),
                    biblioteca.getHorario()
                ))
                .collect(Collectors.toList());
            
            csvService.writeCSV(FILE_NAME, headers, data);
        } catch (IOException e) {
            System.err.println("Error al guardar bibliotecas en CSV: " + e.getMessage());
        }
    }

    public Biblioteca crear(Biblioteca biblioteca) {
        if (biblioteca.getId() == null || biblioteca.getId().isEmpty()) {
            biblioteca.setId(UUID.randomUUID().toString());
        }
        bibliotecas.put(biblioteca.getId(), biblioteca);
        saveToCSV();
        return biblioteca;
    }

    public List<Biblioteca> obtenerTodos() {
        return new ArrayList<>(bibliotecas.values());
    }

    public Optional<Biblioteca> obtenerPorId(String id) {
        return Optional.ofNullable(bibliotecas.get(id));
    }

    public Biblioteca actualizar(String id, Biblioteca bibliotecaActualizada) {
        if (bibliotecas.containsKey(id)) {
            bibliotecaActualizada.setId(id);
            bibliotecas.put(id, bibliotecaActualizada);
            saveToCSV();
            return bibliotecaActualizada;
        }
        return null;
    }

    public boolean eliminar(String id) {
        if (bibliotecas.remove(id) != null) {
            saveToCSV();
            return true;
        }
        return false;
    }
}
