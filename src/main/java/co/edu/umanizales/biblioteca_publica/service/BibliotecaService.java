package co.edu.umanizales.biblioteca_publica.service;

import co.edu.umanizales.biblioteca_publica.model.Library;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class LibraryService {
    
    private final CSVService csvService;
    private final Map<String, Library> libraries = new ConcurrentHashMap<>();
    private static final String FILE_NAME = "libraries.csv";

    public LibraryService(CSVService csvService) {
        this.csvService = csvService;
        loadFromCSV();
    }

    private void loadFromCSV() {
        try {
            List<List<String>> data = csvService.readCSV(FILE_NAME);
            for (List<String> row : data) {
                if (row.size() >= 5) {
                    Library library = new Library(
                        row.get(0), // id
                        row.get(1), // name
                        row.get(2), // address
                        row.get(3), // phone
                        row.get(4)  // schedule
                    );
                    libraries.put(library.getId(), library);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading libraries from CSV: " + e.getMessage());
        }
    }

    private void saveToCSV() {
        try {
            List<String> headers = Arrays.asList("id", "name", "address", "phone", "schedule");
            
            List<List<String>> data = libraries.values().stream()
                .map(library -> Arrays.asList(
                    library.getId(),
                    library.getName(),
                    library.getAddress(),
                    library.getPhone(),
                    library.getSchedule()
                ))
                .collect(Collectors.toList());
            
            csvService.writeCSV(FILE_NAME, headers, data);
        } catch (IOException e) {
            System.err.println("Error saving libraries to CSV: " + e.getMessage());
        }
    }

    public Library create(Library library) {
        if (library.getId() == null || library.getId().isEmpty()) {
            library.setId(UUID.randomUUID().toString());
        }
        libraries.put(library.getId(), library);
        saveToCSV();
        return library;
    }

    public List<Library> getAll() {
        return new ArrayList<>(libraries.values());
    }

    public Optional<Library> getById(String id) {
        return Optional.ofNullable(libraries.get(id));
    }

    public Library update(String id, Library updatedLibrary) {
        if (libraries.containsKey(id)) {
            updatedLibrary.setId(id);
            libraries.put(id, updatedLibrary);
            saveToCSV();
            return updatedLibrary;
        }
        return null;
    }

    public boolean delete(String id) {
        if (libraries.remove(id) != null) {
            saveToCSV();
            return true;
        }
        return false;
    }
}
