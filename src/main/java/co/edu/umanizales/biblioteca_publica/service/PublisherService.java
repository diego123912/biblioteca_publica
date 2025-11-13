package co.edu.umanizales.biblioteca_publica.service;

import co.edu.umanizales.biblioteca_publica.model.Publisher;
import co.edu.umanizales.biblioteca_publica.repository.PublisherRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PublisherService {
    
    private final PublisherRepository publisherRepository;

    public PublisherService(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    public Publisher create(Publisher publisher) {
        return publisherRepository.create(publisher);
    }

    public List<Publisher> getAll() {
        return publisherRepository.getAll();
    }

    public Publisher getById(String id) {
        return publisherRepository.getById(id);
    }

    public Publisher update(String id, Publisher updatedPublisher) {
        return publisherRepository.update(id, updatedPublisher);
    }

    public boolean delete(String id) {
        return publisherRepository.delete(id);
    }

    public List<Publisher> searchByName(String name) {
        return publisherRepository.searchByName(name);
    }
}
