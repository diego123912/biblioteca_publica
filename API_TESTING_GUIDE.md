# Guía Rápida de Pruebas - API Biblioteca Pública

## 🚀 Iniciar la Aplicación

```bash
mvnw spring-boot:run
```

O desde tu IDE, ejecuta la clase `BibliotecaPublicaApplication.java`

La API estará disponible en: **http://localhost:8080**

## 📊 Datos de Prueba Precargados

Al iniciar, la aplicación carga automáticamente:

### Usuarios
- **Estudiante 1**: María González (ID: EST001)
- **Estudiante 2**: Carlos Rodríguez (ID: EST002)
- **Profesor**: Ana Martínez (ID: PROF001)
- **Administrador**: Luis Sánchez (ID: ADM001)

### Libros
- **LIB001**: Cien Años de Soledad
- **LIB002**: 1984
- **LIB003**: Una Breve Historia del Tiempo
- **LIB004**: Clean Code
- **LIB005**: Design Patterns

## 🧪 Casos de Prueba Recomendados

### 1. Listar todos los libros

```http
GET http://localhost:8080/api/libros
```

### 2. Obtener un libro específico

```http
GET http://localhost:8080/api/libros/LIB001
```

### 3. Buscar libros por título

```http
GET http://localhost:8080/api/libros/buscar/titulo?titulo=clean
```

### 4. Crear un nuevo libro

```http
POST http://localhost:8080/api/libros
Content-Type: application/json

{
  "isbn": "978-0-307-47424-7",
  "titulo": "El Principito",
  "autor": "Antoine de Saint-Exupéry",
  "editorial": "Gallimard",
  "anioPublicacion": 1943,
  "genero": "FICCION",
  "cantidadDisponible": 4,
  "cantidadTotal": 4,
  "ubicacion": "Estante A5"
}
```

### 5. Listar todos los usuarios

```http
GET http://localhost:8080/api/usuarios
```

### 6. Crear un nuevo estudiante

```http
POST http://localhost:8080/api/usuarios
Content-Type: application/json

{
  "nombre": "Pedro",
  "apellido": "López",
  "email": "pedro.lopez@example.com",
  "telefono": "3001112233",
  "tipo": "ESTUDIANTE",
  "carrera": "Derecho",
  "semestre": "4"
}
```

### 7. Crear un nuevo profesor

```http
POST http://localhost:8080/api/usuarios
Content-Type: application/json

{
  "nombre": "Laura",
  "apellido": "Ramírez",
  "email": "laura.ramirez@example.com",
  "telefono": "3002223344",
  "tipo": "PROFESOR",
  "departamento": "Matemáticas",
  "especializacion": "Álgebra Lineal"
}
```

### 8. Realizar un préstamo (Polimorfismo en acción)

```http
POST http://localhost:8080/api/prestamos/realizar
Content-Type: application/json

{
  "usuarioId": "EST001",
  "libroId": "LIB001"
}
```

**Nota**: Observa cómo el sistema automáticamente:
- Calcula la fecha de devolución según el tipo de usuario (15 días para estudiantes)
- Verifica disponibilidad
- Actualiza el inventario
- Envía notificación al usuario

### 9. Realizar préstamo con profesor (diferente plazo)

```http
POST http://localhost:8080/api/prestamos/realizar
Content-Type: application/json

{
  "usuarioId": "PROF001",
  "libroId": "LIB002"
}
```

**Polimorfismo**: El profesor tendrá 30 días de préstamo (vs 15 del estudiante)

### 10. Listar préstamos de un usuario

```http
GET http://localhost:8080/api/prestamos/usuario/EST001
```

### 11. Devolver un libro

```http
POST http://localhost:8080/api/prestamos/{id}/devolver
```

Reemplaza `{id}` con el ID del préstamo obtenido en el paso 8.

### 12. Crear una reseña (Composición)

```http
POST http://localhost:8080/api/resenas
Content-Type: application/json

{
  "usuarioId": "EST001",
  "libroId": "LIB001",
  "calificacion": 5,
  "comentario": "Una obra maestra de la literatura latinoamericana. Realismo mágico en su máxima expresión.",
  "fechaCreacion": "2025-10-18T19:00:00"
}
```

### 13. Obtener reseñas de un libro

```http
GET http://localhost:8080/api/resenas/libro/LIB001
```

### 14. Aprobar una reseña

```http
POST http://localhost:8080/api/resenas/{id}/aprobar
```

### 15. Obtener calificación promedio de un libro

```http
GET http://localhost:8080/api/resenas/libro/LIB001/calificacion-promedio
```

### 16. Crear un autor

```http
POST http://localhost:8080/api/autores
Content-Type: application/json

{
  "nombre": "Isabel",
  "apellido": "Allende",
  "nacionalidad": "Chilena",
  "fechaNacimiento": "1942-08-02",
  "biografia": "Escritora chilena, considerada una de las más importantes de la literatura latinoamericana."
}
```

### 17. Crear una reserva

```http
POST http://localhost:8080/api/reservas
Content-Type: application/json

{
  "usuarioId": "EST002",
  "libroId": "LIB003",
  "fechaReserva": "2025-10-18T20:00:00",
  "fechaExpiracion": "2025-10-20T20:00:00"
}
```

### 18. Verificar préstamos vencidos

```http
POST http://localhost:8080/api/prestamos/verificar-vencimientos
```

### 19. Buscar usuarios por tipo

```http
GET http://localhost:8080/api/usuarios/tipo/ESTUDIANTE
```

### 20. Filtrar préstamos por estado

```http
GET http://localhost:8080/api/prestamos/estado/ACTIVO
```

## 🎯 Conceptos POO Demostrados

### Herencia
- Clase abstracta `Usuario` → `Estudiante`, `Profesor`, `Administrador`

### Polimorfismo
```java
// Cada tipo de usuario retorna valores diferentes
usuario.getDiasPrestamo()      // EST: 15, PROF: 30, ADM: 60
usuario.getLimitePrestamos()   // EST: 3,  PROF: 10, ADM: ∞
```

### Encapsulamiento
- Todos los atributos son privados con acceso via Lombok (`@Data`)

### Composición
- `Resena` contiene instancias de `Usuario` y `Libro`

### Agregación
- `Biblioteca` contiene listas de `Libros`, `Usuarios`, `Prestamos`

### Interfaces
- `Notificable`: Implementada por `Usuario`
- `Exportable`: Para persistencia CSV

### Enums
- `LoanStatus`: ACTIVO, VENCIDO, FINALIZADO
- `UserType`: ESTUDIANTE, PROFESOR, ADMINISTRADOR
- `BookGenre`: FICCION, CIENCIA, TECNOLOGIA, etc.

### Records
- `RegistroActividad`: Datos inmutables de auditoría

## 📁 Archivos CSV Generados

Los datos se persisten automáticamente en:

```
data/csv/
├── libros.csv
├── usuarios.csv
├── prestamos.csv
├── resenas.csv
├── autores.csv
├── editoriales.csv
├── notificaciones.csv
├── reservas.csv
└── bibliotecas.csv
```

## 🔧 Herramientas Recomendadas

- **Postman**: Para pruebas de API
- **Thunder Client** (VS Code): Extensión ligera
- **curl**: Desde línea de comandos
- **Swagger/OpenAPI**: (Opcional, puede agregarse después)

## ⚠️ Notas Importantes

1. Los IDs se generan automáticamente como UUIDs (excepto datos precargados)
2. Las notificaciones se almacenan pero no se envían por email (solo se registran)
3. La verificación de vencimientos debe ejecutarse manualmente o programarse
4. El sistema valida límites de préstamos según tipo de usuario

## 📞 Soporte

Para más información, consulta el archivo `README.md` principal.
