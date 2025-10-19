package co.edu.umanizales.biblioteca_publica.service;

import co.edu.umanizales.biblioteca_publica.model.Notificacion;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class NotificacionService {
    
    private final CSVService csvService;
    private final Map<String, Notificacion> notificaciones = new ConcurrentHashMap<>();
    private static final String FILE_NAME = "notificaciones.csv";

    public NotificacionService(CSVService csvService) {
        this.csvService = csvService;
        loadFromCSV();
    }

    private void loadFromCSV() {
        try {
            List<List<String>> data = csvService.readCSV(FILE_NAME);
            for (List<String> row : data) {
                if (row.size() >= 6) {
                    Notificacion notificacion = new Notificacion(
                        row.get(0), // id
                        row.get(1), // usuarioId
                        row.get(2), // tipo
                        row.get(3), // mensaje
                        LocalDateTime.parse(row.get(4)), // fechaEnvio
                        Boolean.parseBoolean(row.get(5)) // leida
                    );
                    notificaciones.put(notificacion.getId(), notificacion);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al cargar notificaciones desde CSV: " + e.getMessage());
        }
    }

    private void saveToCSV() {
        try {
            List<String> headers = Arrays.asList("id", "usuarioId", "tipo", "mensaje", "fechaEnvio", "leida");
            
            List<List<String>> data = notificaciones.values().stream()
                .map(notificacion -> Arrays.asList(
                    notificacion.getId(),
                    notificacion.getUsuarioId(),
                    notificacion.getTipo(),
                    csvService.escapeCSV(notificacion.getMensaje()),
                    notificacion.getFechaEnvio().toString(),
                    String.valueOf(notificacion.isLeida())
                ))
                .collect(Collectors.toList());
            
            csvService.writeCSV(FILE_NAME, headers, data);
        } catch (IOException e) {
            System.err.println("Error al guardar notificaciones en CSV: " + e.getMessage());
        }
    }

    public Notificacion crear(Notificacion notificacion) {
        if (notificacion.getId() == null || notificacion.getId().isEmpty()) {
            notificacion.setId(UUID.randomUUID().toString());
        }
        notificaciones.put(notificacion.getId(), notificacion);
        saveToCSV();
        return notificacion;
    }

    public List<Notificacion> obtenerTodos() {
        return new ArrayList<>(notificaciones.values());
    }

    public Optional<Notificacion> obtenerPorId(String id) {
        return Optional.ofNullable(notificaciones.get(id));
    }

    public Notificacion actualizar(String id, Notificacion notificacionActualizada) {
        if (notificaciones.containsKey(id)) {
            notificacionActualizada.setId(id);
            notificaciones.put(id, notificacionActualizada);
            saveToCSV();
            return notificacionActualizada;
        }
        return null;
    }

    public boolean eliminar(String id) {
        if (notificaciones.remove(id) != null) {
            saveToCSV();
            return true;
        }
        return false;
    }

    public List<Notificacion> obtenerPorUsuario(String usuarioId) {
        return notificaciones.values().stream()
            .filter(n -> n.getUsuarioId().equals(usuarioId))
            .sorted(Comparator.comparing(Notificacion::getFechaEnvio).reversed())
            .collect(Collectors.toList());
    }

    public List<Notificacion> obtenerNoLeidas(String usuarioId) {
        return notificaciones.values().stream()
            .filter(n -> n.getUsuarioId().equals(usuarioId) && !n.isLeida())
            .sorted(Comparator.comparing(Notificacion::getFechaEnvio).reversed())
            .collect(Collectors.toList());
    }

    public Notificacion marcarComoLeida(String id) {
        Optional<Notificacion> notificacionOpt = obtenerPorId(id);
        if (notificacionOpt.isPresent()) {
            Notificacion notificacion = notificacionOpt.get();
            notificacion.marcarComoLeida();
            notificaciones.put(id, notificacion);
            saveToCSV();
            return notificacion;
        }
        return null;
    }
}
