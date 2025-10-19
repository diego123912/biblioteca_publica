package co.edu.umanizales.biblioteca_publica.service;

import co.edu.umanizales.biblioteca_publica.model.Reserva;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class ReservaService {
    
    private final CSVService csvService;
    private final Map<String, Reserva> reservas = new ConcurrentHashMap<>();
    private static final String FILE_NAME = "reservas.csv";

    public ReservaService(CSVService csvService) {
        this.csvService = csvService;
        loadFromCSV();
    }

    private void loadFromCSV() {
        try {
            List<List<String>> data = csvService.readCSV(FILE_NAME);
            for (List<String> row : data) {
                if (row.size() >= 6) {
                    Reserva reserva = new Reserva(
                        row.get(0), // id
                        row.get(1), // usuarioId
                        row.get(2), // libroId
                        LocalDateTime.parse(row.get(3)), // fechaReserva
                        LocalDateTime.parse(row.get(4)), // fechaExpiracion
                        Boolean.parseBoolean(row.get(5)), // activa
                        Boolean.parseBoolean(row.get(6))  // completada
                    );
                    reservas.put(reserva.getId(), reserva);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al cargar reservas desde CSV: " + e.getMessage());
        }
    }

    private void saveToCSV() {
        try {
            List<String> headers = Arrays.asList("id", "usuarioId", "libroId", "fechaReserva", 
                "fechaExpiracion", "activa", "completada");
            
            List<List<String>> data = reservas.values().stream()
                .map(reserva -> Arrays.asList(
                    reserva.getId(),
                    reserva.getUsuarioId(),
                    reserva.getLibroId(),
                    reserva.getFechaReserva().toString(),
                    reserva.getFechaExpiracion().toString(),
                    String.valueOf(reserva.isActiva()),
                    String.valueOf(reserva.isCompletada())
                ))
                .collect(Collectors.toList());
            
            csvService.writeCSV(FILE_NAME, headers, data);
        } catch (IOException e) {
            System.err.println("Error al guardar reservas en CSV: " + e.getMessage());
        }
    }

    public Reserva crear(Reserva reserva) {
        if (reserva.getId() == null || reserva.getId().isEmpty()) {
            reserva.setId(UUID.randomUUID().toString());
        }
        reservas.put(reserva.getId(), reserva);
        saveToCSV();
        return reserva;
    }

    public List<Reserva> obtenerTodos() {
        return new ArrayList<>(reservas.values());
    }

    public Optional<Reserva> obtenerPorId(String id) {
        return Optional.ofNullable(reservas.get(id));
    }

    public Reserva actualizar(String id, Reserva reservaActualizada) {
        if (reservas.containsKey(id)) {
            reservaActualizada.setId(id);
            reservas.put(id, reservaActualizada);
            saveToCSV();
            return reservaActualizada;
        }
        return null;
    }

    public boolean eliminar(String id) {
        if (reservas.remove(id) != null) {
            saveToCSV();
            return true;
        }
        return false;
    }

    public List<Reserva> obtenerPorUsuario(String usuarioId) {
        return reservas.values().stream()
            .filter(r -> r.getUsuarioId().equals(usuarioId))
            .collect(Collectors.toList());
    }

    public List<Reserva> obtenerActivas() {
        return reservas.values().stream()
            .filter(Reserva::isActiva)
            .collect(Collectors.toList());
    }

    public Reserva cancelar(String id) {
        Optional<Reserva> reservaOpt = obtenerPorId(id);
        if (reservaOpt.isPresent()) {
            Reserva reserva = reservaOpt.get();
            reserva.cancelar();
            reservas.put(id, reserva);
            saveToCSV();
            return reserva;
        }
        return null;
    }

    public Reserva completar(String id) {
        Optional<Reserva> reservaOpt = obtenerPorId(id);
        if (reservaOpt.isPresent()) {
            Reserva reserva = reservaOpt.get();
            reserva.completar();
            reservas.put(id, reserva);
            saveToCSV();
            return reserva;
        }
        return null;
    }
}
