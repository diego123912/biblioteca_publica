package co.edu.umanizales.biblioteca_publica.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class Publisher {
    private String id;
    private String name;
    private String country;
    private String website;
    private String contact;
    private List<String> bookIds = new ArrayList<>();

    public Publisher(String id, String name, String country, String website, String contact) {
        this.id = id;
        this.name = name;
        this.country = country;
        this.website = website;
        this.contact = contact;
        this.bookIds = new ArrayList<>();
    }
}
