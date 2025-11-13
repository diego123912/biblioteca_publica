package co.edu.umanizales.biblioteca_publica.repository;

import co.edu.umanizales.biblioteca_publica.model.Publisher;
import co.edu.umanizales.biblioteca_publica.service.CSVService;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class EditorialCSVRepository implements EditorialRepository {
    
    private final CSVService csvService;
    private final Map<String, Publisher> publishers = new ConcurrentHashMap<>();
    private static final String FILE_NAME = "editorials.csv";
    
    public EditorialCSVRepository(CSVService csvService) {
        this.csvService = csvService;
        load();
    }
    
    @Override
    public void load() {
        try {
            List<List<String>> data = csvService.readCSV(FILE_NAME);
            for (List<String> row : data) {
                if (row.size() >= 5) {
                    Publisher publisher = new Publisher(
                        row.get(0), // id
                        row.get(1), // name
                        row.get(2), // country
                        row.get(3), // website
                        row.get(4)  // contact
                    );
                    publishers.put(publisher.getId(), publisher);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading publishers from CSV: " + e.getMessage());
        }
    }
    
    private void save() {
        try {
            List<String> headers = Arrays.asList("id", "name", "country", "website", "contact");
            
            List<List<String>> data = new ArrayList<>();
            for (Publisher publisher : publishers.values()) {
                data.add(Arrays.asList(
                    publisher.getId(),
                    publisher.getName(),
                    publisher.getCountry(),
                    publisher.getWebsite(),
                    publisher.getContact()
                ));
            }
            
            csvService.writeCSV(FILE_NAME, headers, data);
        } catch (IOException e) {
            System.err.println("Error saving publishers to CSV: " + e.getMessage());
        }
    }
    
    @Override
    public Publisher create(Publisher publisher) {
        if (publisher.getId() == null || publisher.getId().isEmpty()) {
            publisher.setId(UUID.randomUUID().toString());
        }
        publishers.put(publisher.getId(), publisher);
        save();
        return publisher;
    }
    
    @Override
    public List<Publisher> getAll() {
        return new ArrayList<>(publishers.values());
    }
    
    @Override
    public Publisher getById(String id) {
        return publishers.get(id);
    }
    
    @Override
    public Publisher update(String id, Publisher updatedPublisher) {
        if (publishers.containsKey(id)) {
            updatedPublisher.setId(id);
            publishers.put(id, updatedPublisher);
            save();
            return updatedPublisher;
        }
        return null;
    }
    
    @Override
    public boolean delete(String id) {
        if (publishers.remove(id) != null) {
            save();
            return true;
        }
        return false;
    }
    
    @Override
    public List<Publisher> searchByName(String name) {
        List<Publisher> result = new ArrayList<>();
        for (Publisher publisher : publishers.values()) {
            if (publisher.getName().toLowerCase().contains(name.toLowerCase())) {
                result.add(publisher);
            }
        }
        return result;
    }
}
