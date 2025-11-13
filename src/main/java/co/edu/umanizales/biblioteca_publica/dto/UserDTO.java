package co.edu.umanizales.biblioteca_publica.dto;

import co.edu.umanizales.biblioteca_publica.enums.UserType;
import co.edu.umanizales.biblioteca_publica.model.Administrator;
import co.edu.umanizales.biblioteca_publica.model.Student;
import co.edu.umanizales.biblioteca_publica.model.Teacher;
import co.edu.umanizales.biblioteca_publica.model.User;
import lombok.Data;

/**
 * DTO to receive user data and create instances according to type.
 * Facilitates polymorphic deserialization of users.
 */
@Data
public class UserDTO {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private UserType type;
    
    // Student specific fields
    private String major;
    private String semester;
    
    // Teacher specific fields
    private String department;
    private String specialization;
    
    // Administrator specific fields
    private String role;
    private Boolean fullPermission;

    /**
     * Converts the DTO into the correct User instance according to type.
     */
    public User toUser() {
        if (type == null) {
            throw new IllegalArgumentException("User type is required. Please specify type: STUDENT, TEACHER, or ADMINISTRATOR");
        }
        
        switch (type) {
            case STUDENT:
                return new Student(id, firstName, lastName, email, phone, major, semester);
            case TEACHER:
                return new Teacher(id, firstName, lastName, email, phone, department, specialization);
            case ADMINISTRATOR:
                return new Administrator(id, firstName, lastName, email, phone, role, 
                    fullPermission != null ? fullPermission : false);
            default:
                throw new IllegalArgumentException("Invalid user type: " + type);
        }
    }

    /**
     * Creates a DTO from a User instance.
     */
    public static UserDTO fromUser(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setType(user.getType());
        
        if (user instanceof Student) {
            Student student = (Student) user;
            dto.setMajor(student.getMajor());
            dto.setSemester(student.getSemester());
        } else if (user instanceof Teacher) {
            Teacher teacher = (Teacher) user;
            dto.setDepartment(teacher.getDepartment());
            dto.setSpecialization(teacher.getSpecialization());
        } else if (user instanceof Administrator) {
            Administrator admin = (Administrator) user;
            dto.setRole(admin.getRole());
            dto.setFullPermission(admin.isFullPermission());
        }

        
        return dto;
    }
}
