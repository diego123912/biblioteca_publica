package co.edu.umanizales.biblioteca_publica.service;

import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class CSVService {
    
    private static final String DATA_DIR = "data/csv/";

    public CSVService() {
        try {
            Files.createDirectories(Paths.get(DATA_DIR));
        } catch (IOException e) {
            System.err.println("Error creating data directory: " + e.getMessage());
        }
    }

    public void writeCSV(String fileName, List<String> headers, List<List<String>> data) throws IOException {
        Path filePath = Paths.get(DATA_DIR + fileName);
        
        try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8)) {
            // Write headers
            writer.write(String.join(",", headers));
            writer.newLine();
            
            // Write data
            for (List<String> row : data) {
                writer.write(String.join(",", row));
                writer.newLine();
            }
        }
    }

    public List<List<String>> readCSV(String fileName) throws IOException {
        Path filePath = Paths.get(DATA_DIR + fileName);
        List<List<String>> data = new ArrayList<>();
        
        if (!Files.exists(filePath)) {
            return data;
        }
        
        try (BufferedReader reader = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {
            String line;
            boolean firstLine = true;
            
            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false; // Skip headers
                    continue;
                }
                
                List<String> row = new ArrayList<>();
                String[] values = line.split(",", -1);
                for (String value : values) {
                    row.add(value);
                }
                data.add(row);
            }
        }
        
        return data;
    }

    public void appendCSV(String fileName, List<String> row) throws IOException {
        Path filePath = Paths.get(DATA_DIR + fileName);
        
        try (BufferedWriter writer = Files.newBufferedWriter(filePath, 
                StandardCharsets.UTF_8, 
                java.nio.file.StandardOpenOption.CREATE, 
                java.nio.file.StandardOpenOption.APPEND)) {
            writer.write(String.join(",", row));
            writer.newLine();
        }
    }

    public boolean fileExists(String fileName) {
        Path filePath = Paths.get(DATA_DIR + fileName);
        return Files.exists(filePath);
    }

    public void deleteFile(String fileName) throws IOException {
        Path filePath = Paths.get(DATA_DIR + fileName);
        if (Files.exists(filePath)) {
            Files.delete(filePath);
        }
    }

    public String escapeCSV(String value) {
        if (value == null) {
            return "";
        }
        
        // If contains comma, quote or newline, escape with quotes
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        
        return value;
    }
}
