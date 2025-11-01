package co.edu.umanizales.biblioteca_publica.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Publisher {
    private String id;
    private String name;
    private String country;
    private String website;
    private String contact;
    private List<String> bookIds = new ArrayList<>();

}
