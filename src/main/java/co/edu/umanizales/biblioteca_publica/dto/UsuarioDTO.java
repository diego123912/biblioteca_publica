package co.edu.umanizales.biblioteca_publica.dto;

import co.edu.umanizales.biblioteca_publica.enums.UserType;
import co.edu.umanizales.biblioteca_publica.model.Administrador;
import co.edu.umanizales.biblioteca_publica.model.Estudiante;
import co.edu.umanizales.biblioteca_publica.model.Profesor;
import co.edu.umanizales.biblioteca_publica.model.Usuario;
import lombok.Data;

/**
 * DTO para recibir datos de usuarios y crear instancias según el tipo.
 * Facilita la deserialización polimórfica de usuarios.
 */
@Data
public class UsuarioDTO {
    private String id;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private UserType tipo;
    
    // Campos específicos de Estudiante
    private String carrera;
    private String semestre;
    
    // Campos específicos de Profesor
    private String departamento;
    private String especializacion;
    
    // Campos específicos de Administrador
    private String rol;
    private Boolean permisoTotal;

    /**
     * Convierte el DTO en la instancia correcta de Usuario según el tipo.
     */
    public Usuario toUsuario() {
        switch (tipo) {
            case ESTUDIANTE:
                return new Estudiante(id, nombre, apellido, email, telefono, carrera, semestre);
            case PROFESOR:
                return new Profesor(id, nombre, apellido, email, telefono, departamento, especializacion);
            case ADMINISTRADOR:
                return new Administrador(id, nombre, apellido, email, telefono, rol, 
                    permisoTotal != null ? permisoTotal : false);
            default:
                throw new IllegalArgumentException("Tipo de usuario no válido: " + tipo);
        }
    }

    /**
     * Crea un DTO desde una instancia de Usuario.
     */
    public static UsuarioDTO fromUsuario(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setNombre(usuario.getNombre());
        dto.setApellido(usuario.getApellido());
        dto.setEmail(usuario.getEmail());
        dto.setTelefono(usuario.getTelefono());
        dto.setTipo(usuario.getTipo());
        
        if (usuario instanceof Estudiante) {
            Estudiante est = (Estudiante) usuario;
            dto.setCarrera(est.getCarrera());
            dto.setSemestre(est.getSemestre());
        } else if (usuario instanceof Profesor) {
            Profesor prof = (Profesor) usuario;
            dto.setDepartamento(prof.getDepartamento());
            dto.setEspecializacion(prof.getEspecializacion());
        } else if (usuario instanceof Administrador) {
            Administrador admin = (Administrador) usuario;
            dto.setRol(admin.getRol());
            dto.setPermisoTotal(admin.isPermisoTotal());
        }
        
        return dto;
    }
}
