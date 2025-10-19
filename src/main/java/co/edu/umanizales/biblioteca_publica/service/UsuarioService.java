package co.edu.umanizales.biblioteca_publica.service;

import co.edu.umanizales.biblioteca_publica.enums.UserType;
import co.edu.umanizales.biblioteca_publica.model.Administrador;
import co.edu.umanizales.biblioteca_publica.model.Estudiante;
import co.edu.umanizales.biblioteca_publica.model.Profesor;
import co.edu.umanizales.biblioteca_publica.model.Usuario;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class UsuarioService {
    
    private final CSVService csvService;
    private final Map<String, Usuario> usuarios = new ConcurrentHashMap<>();
    private static final String FILE_NAME = "usuarios.csv";

    public UsuarioService(CSVService csvService) {
        this.csvService = csvService;
        loadFromCSV();
    }

    private void loadFromCSV() {
        try {
            List<List<String>> data = csvService.readCSV(FILE_NAME);
            for (List<String> row : data) {
                if (row.size() >= 8) {
                    UserType tipo = UserType.valueOf(row.get(5));
                    Usuario usuario = null;
                    
                    switch (tipo) {
                        case ESTUDIANTE:
                            usuario = new Estudiante(
                                row.get(0), // id
                                row.get(1), // nombre
                                row.get(2), // apellido
                                row.get(3), // email
                                row.get(4), // telefono
                                row.get(6), // carrera
                                row.get(7)  // semestre
                            );
                            break;
                        case PROFESOR:
                            usuario = new Profesor(
                                row.get(0), // id
                                row.get(1), // nombre
                                row.get(2), // apellido
                                row.get(3), // email
                                row.get(4), // telefono
                                row.get(6), // departamento
                                row.get(7)  // especializacion
                            );
                            break;
                        case ADMINISTRADOR:
                            usuario = new Administrador(
                                row.get(0), // id
                                row.get(1), // nombre
                                row.get(2), // apellido
                                row.get(3), // email
                                row.get(4), // telefono
                                row.get(6), // rol
                                Boolean.parseBoolean(row.get(7))  // permisoTotal
                            );
                            break;
                    }
                    
                    if (usuario != null) {
                        usuarios.put(usuario.getId(), usuario);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error al cargar usuarios desde CSV: " + e.getMessage());
        }
    }

    private void saveToCSV() {
        try {
            List<String> headers = Arrays.asList("id", "nombre", "apellido", "email", "telefono", 
                "tipo", "campo1", "campo2");
            
            List<List<String>> data = usuarios.values().stream()
                .map(usuario -> {
                    String campo1 = "";
                    String campo2 = "";
                    
                    if (usuario instanceof Estudiante) {
                        Estudiante est = (Estudiante) usuario;
                        campo1 = est.getCarrera();
                        campo2 = est.getSemestre();
                    } else if (usuario instanceof Profesor) {
                        Profesor prof = (Profesor) usuario;
                        campo1 = prof.getDepartamento();
                        campo2 = prof.getEspecializacion();
                    } else if (usuario instanceof Administrador) {
                        Administrador admin = (Administrador) usuario;
                        campo1 = admin.getRol();
                        campo2 = String.valueOf(admin.isPermisoTotal());
                    }
                    
                    return Arrays.asList(
                        usuario.getId(),
                        usuario.getNombre(),
                        usuario.getApellido(),
                        usuario.getEmail(),
                        usuario.getTelefono(),
                        usuario.getTipo().toString(),
                        campo1,
                        campo2
                    );
                })
                .collect(Collectors.toList());
            
            csvService.writeCSV(FILE_NAME, headers, data);
        } catch (IOException e) {
            System.err.println("Error al guardar usuarios en CSV: " + e.getMessage());
        }
    }

    public Usuario crear(Usuario usuario) {
        if (usuario.getId() == null || usuario.getId().isEmpty()) {
            usuario.setId(UUID.randomUUID().toString());
        }
        usuarios.put(usuario.getId(), usuario);
        saveToCSV();
        return usuario;
    }

    public List<Usuario> obtenerTodos() {
        return new ArrayList<>(usuarios.values());
    }

    public Optional<Usuario> obtenerPorId(String id) {
        return Optional.ofNullable(usuarios.get(id));
    }

    public Usuario actualizar(String id, Usuario usuarioActualizado) {
        if (usuarios.containsKey(id)) {
            usuarioActualizado.setId(id);
            usuarios.put(id, usuarioActualizado);
            saveToCSV();
            return usuarioActualizado;
        }
        return null;
    }

    public boolean eliminar(String id) {
        if (usuarios.remove(id) != null) {
            saveToCSV();
            return true;
        }
        return false;
    }

    public List<Usuario> buscarPorTipo(UserType tipo) {
        return usuarios.values().stream()
            .filter(usuario -> usuario.getTipo() == tipo)
            .collect(Collectors.toList());
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarios.values().stream()
            .filter(usuario -> usuario.getEmail().equalsIgnoreCase(email))
            .findFirst();
    }
}
