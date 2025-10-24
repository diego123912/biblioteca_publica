package co.edu.umanizales.biblioteca_publica.repository;

import co.edu.umanizales.biblioteca_publica.model.Publisher;
import java.util.List;
import java.util.Optional;

public interface PublisherRepository {
    
    Publisher create(Publisher publisher);
    
    List<Publisher> getAll();
    
    Optional<Publisher> getById(String id);
    
    Publisher update(String id, Publisher publisher);
    
    boolean delete(String id);
    
    List<Publisher> searchByName(String name);
    
    void load();
}
