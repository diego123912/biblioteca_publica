package co.edu.umanizales.biblioteca_publica.repository;

import co.edu.umanizales.biblioteca_publica.model.Publisher;
import java.util.List;

public interface EditorialRepository {
    
    Publisher create(Publisher publisher);
    
    List<Publisher> getAll();
    
    Publisher getById(String id);
    
    Publisher update(String id, Publisher publisher);
    
    boolean delete(String id);
    
    List<Publisher> searchByName(String name);
    
    void load();
}
