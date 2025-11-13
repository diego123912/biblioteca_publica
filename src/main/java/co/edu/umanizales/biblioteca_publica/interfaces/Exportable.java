package co.edu.umanizales.biblioteca_publica.interfaces;

import java.util.List;

public interface Exportable<T> {
    String toCSV();
    T fromCSV(String csvLine);
    List<String> getCSVHeaders();

}
