# 📮 Guía Rápida para Postman - Biblioteca Pública API

## 🚀 Paso 1: Iniciar la Aplicación

Desde el directorio del proyecto, ejecuta:

```bash
mvnw.cmd spring-boot:run
```

O ejecuta desde tu IDE la clase `BibliotecaPublicaApplication.java`.

La API estará disponible en: **http://localhost:8070**

---

## 📊 Datos Precargados

La aplicación carga automáticamente datos de prueba:

### Usuarios
- **STU001** - Maria Gonzalez (Estudiante)
- **STU002** - Carlos Rodriguez (Estudiante)
- **TCH001** - Ana Martinez (Profesor)
- **ADM001** - Luis Sanchez (Administrador)

### Libros
- **BK001** - One Hundred Years of Solitude
- **BK002** - 1984
- **BK003** - A Brief History of Time
- **BK004** - Clean Code
- **BK005** - Design Patterns

---

## 🧪 Ejemplos de Peticiones en Postman

### 📚 LIBROS

#### 1. Listar todos los libros
```
GET http://localhost:8070/api/books
```

#### 2. Obtener libro por ID
```
GET http://localhost:8070/api/books/BK001
```

#### 3. Crear nuevo libro
```
POST http://localhost:8070/api/books
Content-Type: application/json

Body:
{
  "isbn": "978-0-307-47424-7",
  "title": "The Little Prince",
  "author": "Antoine de Saint-Exupéry",
  "publisher": "Gallimard",
  "publicationYear": 1943,
  "genre": "FICTION",
  "availableQuantity": 4,
  "totalQuantity": 4,
  "location": "Shelf A5"
}
```

#### 4. Buscar por título
```
GET http://localhost:8070/api/books/search/title?title=clean
```

#### 5. Buscar por autor
```
GET http://localhost:8070/api/books/search/author?author=Orwell
```

---

### 👥 USUARIOS

#### 6. Listar todos los usuarios
```
GET http://localhost:8070/api/users
```

#### 7. Crear estudiante
```
POST http://localhost:8070/api/users
Content-Type: application/json

Body:
{
  "firstName": "Pedro",
  "lastName": "Lopez",
  "email": "pedro.lopez@example.com",
  "phone": "3001112233",
  "type": "STUDENT",
  "career": "Law",
  "semester": "4"
}
```

#### 8. Crear profesor
```
POST http://localhost:8070/api/users
Content-Type: application/json

Body:
{
  "firstName": "Laura",
  "lastName": "Ramirez",
  "email": "laura.ramirez@example.com",
  "phone": "3002223344",
  "type": "TEACHER",
  "department": "Mathematics",
  "specialization": "Linear Algebra"
}
```

---

### 📖 PRÉSTAMOS (Polimorfismo en acción)

#### 9. Realizar préstamo
```
POST http://localhost:8070/api/loans/perform
Content-Type: application/json

Body:
{
  "userId": "STU001",
  "bookId": "BK001"
}
```

**Nota:** El sistema automáticamente calcula la fecha de devolución según el tipo de usuario:
- Estudiante: 15 días
- Profesor: 30 días
- Administrador: 60 días

#### 10. Listar préstamos de un usuario
```
GET http://localhost:8070/api/loans/user/STU001
```

#### 11. Devolver un libro
```
POST http://localhost:8070/api/loans/{loan-id}/return
```

#### 12. Listar préstamos por estado
```
GET http://localhost:8070/api/loans/status/ACTIVE
```

Estados disponibles: ACTIVE, OVERDUE, COMPLETED

---

### ⭐ RESEÑAS

#### 13. Crear reseña
```
POST http://localhost:8070/api/reviews
Content-Type: application/json

Body:
{
  "userId": "STU001",
  "bookId": "BK001",
  "rating": 5,
  "comment": "Una obra maestra de la literatura latinoamericana",
  "creationDate": "2025-11-06T20:00:00"
}
```

#### 14. Obtener reseñas de un libro
```
GET http://localhost:8070/api/reviews/book/BK001
```

#### 15. Aprobar una reseña
```
POST http://localhost:8070/api/reviews/{review-id}/approve
```

#### 16. Obtener calificación promedio de un libro
```
GET http://localhost:8070/api/reviews/book/BK001/average-rating
```

---

### 📅 RESERVAS

#### 17. Crear reserva
```
POST http://localhost:8070/api/reservations
Content-Type: application/json

Body:
{
  "userId": "STU002",
  "bookId": "BK003",
  "reservationDate": "2025-11-06T20:00:00",
  "expirationDate": "2025-11-08T20:00:00"
}
```

#### 18. Listar reservas activas
```
GET http://localhost:8070/api/reservations/active
```

#### 19. Cancelar reserva
```
POST http://localhost:8070/api/reservations/{reservation-id}/cancel
```

---

## 💡 Tips para Postman

1. **Headers necesarios:** Para peticiones POST/PUT, agrega el header:
   ```
   Content-Type: application/json
   ```

2. **Géneros de libros disponibles:**
   - FICTION
   - SCIENCE
   - TECHNOLOGY
   - HISTORY
   - BIOGRAPHY
   - SELF_HELP
   - CHILDRENS
   - POETRY

3. **Tipos de usuario:**
   - STUDENT
   - TEACHER
   - ADMINISTRATOR

4. **Estados de préstamo:**
   - ACTIVE
   - OVERDUE
   - COMPLETED

---

## 🎯 Orden Recomendado de Pruebas

1. Listar libros y usuarios (GET)
2. Crear un nuevo usuario
3. Crear un nuevo libro
4. Realizar un préstamo
5. Crear una reseña
6. Devolver el libro
7. Verificar el estado del préstamo

---

## 📦 Importar Colección de Postman

También puedes importar el archivo `Biblioteca_API.postman_collection.json` que incluye todas estas peticiones preconfiguradas.

En Postman:
1. Click en "Import"
2. Selecciona el archivo `Biblioteca_API.postman_collection.json`
3. ¡Listo! Todas las peticiones estarán disponibles
