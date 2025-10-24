package co.edu.umanizales.biblioteca_publica.service;

import co.edu.umanizales.biblioteca_publica.enums.UserType;
import co.edu.umanizales.biblioteca_publica.model.Administrator;
import co.edu.umanizales.biblioteca_publica.model.Student;
import co.edu.umanizales.biblioteca_publica.model.Teacher;
import co.edu.umanizales.biblioteca_publica.model.User;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class UserService {
    
    private final CSVService csvService;
    private final Map<String, User> users = new ConcurrentHashMap<>();
    private static final String FILE_NAME = "users.csv";

    public UserService(CSVService csvService) {
        this.csvService = csvService;
        loadFromCSV();
    }

    private void loadFromCSV() {
        try {
            List<List<String>> data = csvService.readCSV(FILE_NAME);
            for (List<String> row : data) {
                if (row.size() >= 8) {
                    UserType type = UserType.valueOf(row.get(5));
                    User user = null;
                    
                    switch (type) {
                        case STUDENT:
                            user = new Student(
                                row.get(0), // id
                                row.get(1), // firstName
                                row.get(2), // lastName
                                row.get(3), // email
                                row.get(4), // phone
                                row.get(6), // major
                                row.get(7)  // semester
                            );
                            break;
                        case TEACHER:
                            user = new Teacher(
                                row.get(0), // id
                                row.get(1), // firstName
                                row.get(2), // lastName
                                row.get(3), // email
                                row.get(4), // phone
                                row.get(6), // department
                                row.get(7)  // specialization
                            );
                            break;
                        case ADMINISTRATOR:
                            user = new Administrator(
                                row.get(0), // id
                                row.get(1), // firstName
                                row.get(2), // lastName
                                row.get(3), // email
                                row.get(4), // phone
                                row.get(6), // role
                                Boolean.parseBoolean(row.get(7))  // fullPermission
                            );
                            break;
                    }
                    
                    if (user != null) {
                        users.put(user.getId(), user);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading users from CSV: " + e.getMessage());
        }
    }

    private void saveToCSV() {
        try {
            List<String> headers = Arrays.asList("id", "firstName", "lastName", "email", "phone", 
                "type", "field1", "field2");
            
            List<List<String>> data = users.values().stream()
                .map(user -> {
                    String field1 = "";
                    String field2 = "";
                    
                    if (user instanceof Student) {
                        Student student = (Student) user;
                        field1 = student.getMajor();
                        field2 = student.getSemester();
                    } else if (user instanceof Teacher) {
                        Teacher teacher = (Teacher) user;
                        field1 = teacher.getDepartment();
                        field2 = teacher.getSpecialization();
                    } else if (user instanceof Administrator) {
                        Administrator admin = (Administrator) user;
                        field1 = admin.getRole();
                        field2 = String.valueOf(admin.isFullPermission());
                    }
                    
                    return Arrays.asList(
                        user.getId(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getEmail(),
                        user.getPhone(),
                        user.getType().toString(),
                        field1,
                        field2
                    );
                })
                .collect(Collectors.toList());
            
            csvService.writeCSV(FILE_NAME, headers, data);
        } catch (IOException e) {
            System.err.println("Error saving users to CSV: " + e.getMessage());
        }
    }

    public User create(User user) {
        if (user.getId() == null || user.getId().isEmpty()) {
            user.setId(UUID.randomUUID().toString());
        }
        users.put(user.getId(), user);
        saveToCSV();
        return user;
    }

    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    public Optional<User> getById(String id) {
        return Optional.ofNullable(users.get(id));
    }

    public User update(String id, User updatedUser) {
        if (users.containsKey(id)) {
            updatedUser.setId(id);
            users.put(id, updatedUser);
            saveToCSV();
            return updatedUser;
        }
        return null;
    }

    public boolean delete(String id) {
        if (users.remove(id) != null) {
            saveToCSV();
            return true;
        }
        return false;
    }

    public List<User> searchByType(UserType type) {
        return users.values().stream()
            .filter(user -> user.getType() == type)
            .collect(Collectors.toList());
    }

    public Optional<User> searchByEmail(String email) {
        return users.values().stream()
            .filter(user -> user.getEmail().equalsIgnoreCase(email))
            .findFirst();
    }
}
