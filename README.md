# Biblioteca Pública - Sistema de Gestión Digital

Sistema REST API para la gestión de una biblioteca digital comunitaria, desarrollado con Spring Boot, Lombok y persistencia en CSV.

## 📋 Características Principales

### Conceptos de POO Implementados

- ✅ **Encapsulamiento**: Todas las clases principales utilizan atributos privados con getters/setters via Lombok
- ✅ **Herencia**: Clase abstracta `Usuario` con subclases `Estudiante`, `Profesor` y `Administrador`
- ✅ **Polimorfismo**: Métodos `getLimitePrestamos()`, `getDiasPrestamo()` y `enviarNotificacion()` implementados polimórficamente
- ✅ **Interfaces**: `Notificable` (mensajes) y `Exportable` (persistencia CSV)
- ✅ **Composición**: `Resena` compuesta por `Usuario` y `Libro`
- ✅ **Agregación**: `Biblioteca` contiene colecciones de `Libros`, `Usuarios`, `Prestamos` y `Resenas`
- ✅ **Records**: `RegistroActividad` para datos inmutables
- ✅ **Enums**: `LoanStatus`, `UserType`, `BookGenre`
- ✅ **Clase Abstracta**: `Usuario` con métodos abstractos

## 🏗️ Arquitectura

```
biblioteca_publica/
├── model/              # Entidades del dominio
├── enums/              # Enumeraciones
├── records/            # Records Java
├── interfaces/         # Interfaces
├── service/            # Lógica de negocio
├── controller/         # Endpoints REST
└── data/csv/           # Archivos CSV (generados automáticamente)
```

## 📦 Entidades (10+ Clases)

1. **Usuario** (abstracta) → Estudiante, Profesor, Administrador
2. **Libro**
3. **Prestamo**
4. **Resena**
5. **Biblioteca**
6. **Autor**
7. **Editorial**
8. **Notificacion**
9. **Reserva**

## 🚀 Ejecución

### Requisitos
- Java 17+
- Maven 3.6+

### Iniciar el servidor

```bash
mvnw spring-boot:run
```

El servidor estará disponible en: `http://localhost:8080`

## 📡 Endpoints API

### Libros (`/api/libros`)

- `POST /api/libros` - Crear libro
- `GET /api/libros` - Listar todos
- `GET /api/libros/{id}` - Obtener por ID
- `PUT /api/libros/{id}` - Actualizar
- `DELETE /api/libros/{id}` - Eliminar
- `GET /api/libros/buscar/titulo?titulo={texto}` - Buscar por título
- `GET /api/libros/buscar/autor?autor={texto}` - Buscar por autor
- `GET /api/libros/buscar/genero/{genero}` - Buscar por género

### Usuarios (`/api/usuarios`)

- `POST /api/usuarios` - Crear usuario
- `GET /api/usuarios` - Listar todos
- `GET /api/usuarios/{id}` - Obtener por ID
- `PUT /api/usuarios/{id}` - Actualizar
- `DELETE /api/usuarios/{id}` - Eliminar
- `GET /api/usuarios/tipo/{tipo}` - Buscar por tipo (ESTUDIANTE, PROFESOR, ADMINISTRADOR)
- `GET /api/usuarios/buscar/email?email={email}` - Buscar por email

### Préstamos (`/api/prestamos`)

- `POST /api/prestamos/realizar` - Realizar préstamo (requiere: `usuarioId`, `libroId`)
- `POST /api/prestamos/{id}/devolver` - Devolver libro
- `GET /api/prestamos` - Listar todos
- `GET /api/prestamos/{id}` - Obtener por ID
- `GET /api/prestamos/usuario/{usuarioId}` - Préstamos de un usuario
- `GET /api/prestamos/estado/{estado}` - Filtrar por estado (ACTIVO, VENCIDO, FINALIZADO)
- `POST /api/prestamos/verificar-vencimientos` - Verificar y actualizar vencidos

### Reseñas (`/api/resenas`)

- `POST /api/resenas` - Crear reseña
- `GET /api/resenas` - Listar todas
- `GET /api/resenas/{id}` - Obtener por ID
- `PUT /api/resenas/{id}` - Actualizar
- `DELETE /api/resenas/{id}` - Eliminar
- `GET /api/resenas/libro/{libroId}` - Reseñas de un libro
- `GET /api/resenas/usuario/{usuarioId}` - Reseñas de un usuario
- `GET /api/resenas/aprobadas` - Solo reseñas aprobadas
- `POST /api/resenas/{id}/aprobar` - Aprobar reseña
- `GET /api/resenas/libro/{libroId}/calificacion-promedio` - Calificación promedio

### Autores (`/api/autores`)

- `POST /api/autores` - Crear autor
- `GET /api/autores` - Listar todos
- `GET /api/autores/{id}` - Obtener por ID
- `PUT /api/autores/{id}` - Actualizar
- `DELETE /api/autores/{id}` - Eliminar
- `GET /api/autores/buscar?nombre={texto}` - Buscar por nombre

### Editoriales (`/api/editoriales`)

- `POST /api/editoriales` - Crear editorial
- `GET /api/editoriales` - Listar todas
- `GET /api/editoriales/{id}` - Obtener por ID
- `PUT /api/editoriales/{id}` - Actualizar
- `DELETE /api/editoriales/{id}` - Eliminar
- `GET /api/editoriales/buscar?nombre={texto}` - Buscar por nombre

### Notificaciones (`/api/notificaciones`)

- `POST /api/notificaciones` - Crear notificación
- `GET /api/notificaciones` - Listar todas
- `GET /api/notificaciones/{id}` - Obtener por ID
- `GET /api/notificaciones/usuario/{usuarioId}` - Notificaciones de usuario
- `GET /api/notificaciones/usuario/{usuarioId}/no-leidas` - No leídas
- `POST /api/notificaciones/{id}/marcar-leida` - Marcar como leída

### Reservas (`/api/reservas`)

- `POST /api/reservas` - Crear reserva
- `GET /api/reservas` - Listar todas
- `GET /api/reservas/{id}` - Obtener por ID
- `GET /api/reservas/usuario/{usuarioId}` - Reservas de usuario
- `GET /api/reservas/activas` - Solo activas
- `POST /api/reservas/{id}/cancelar` - Cancelar reserva
- `POST /api/reservas/{id}/completar` - Completar reserva

### Bibliotecas (`/api/bibliotecas`)

- `POST /api/bibliotecas` - Crear biblioteca
- `GET /api/bibliotecas` - Listar todas
- `GET /api/bibliotecas/{id}` - Obtener por ID
- `PUT /api/bibliotecas/{id}` - Actualizar
- `DELETE /api/bibliotecas/{id}` - Eliminar

## 📝 Ejemplos de Uso

### Crear un Estudiante

```json
POST /api/usuarios
{
  "nombre": "Juan",
  "apellido": "Pérez",
  "email": "juan@example.com",
  "telefono": "3001234567",
  "tipo": "ESTUDIANTE",
  "carrera": "Ingeniería de Sistemas",
  "semestre": "5"
}
```

### Crear un Libro

```json
POST /api/libros
{
  "isbn": "978-3-16-148410-0",
  "titulo": "Cien Años de Soledad",
  "autor": "Gabriel García Márquez",
  "editorial": "Editorial Sudamericana",
  "anioPublicacion": 1967,
  "genero": "FICCION",
  "cantidadDisponible": 5,
  "cantidadTotal": 5,
  "ubicacion": "Estante A1"
}
```

### Realizar un Préstamo

```json
POST /api/prestamos/realizar
{
  "usuarioId": "uuid-del-usuario",
  "libroId": "uuid-del-libro"
}
```

El sistema automáticamente:
- Verifica disponibilidad del libro
- Calcula la fecha de devolución según el tipo de usuario (polimorfismo)
- Actualiza el inventario
- Envía notificación al usuario

### Crear una Reseña

```json
POST /api/resenas
{
  "usuarioId": "uuid-del-usuario",
  "libroId": "uuid-del-libro",
  "calificacion": 5,
  "comentario": "Excelente libro, muy recomendado",
  "fechaCreacion": "2025-10-18T19:00:00"
}
```

## 💾 Persistencia CSV

Los datos se almacenan automáticamente en archivos CSV en el directorio `data/csv/`:

- `libros.csv`
- `usuarios.csv`
- `prestamos.csv`
- `resenas.csv`
- `autores.csv`
- `editoriales.csv`
- `notificaciones.csv`
- `reservas.csv`
- `bibliotecas.csv`

Los archivos se crean automáticamente al iniciar la aplicación y se actualizan con cada operación CRUD.

## 🔧 Tecnologías

- **Spring Boot 3.5.6**
- **Java 17**
- **Lombok** - Reducción de código boilerplate
- **Maven** - Gestión de dependencias
- **Jackson** - Serialización JSON

## 📊 Reglas de Negocio

### Límites de Préstamos por Tipo de Usuario

- **Estudiante**: 3 libros por 15 días
- **Profesor**: 10 libros por 30 días
- **Administrador**: Sin límite por 60 días

### Estados de Préstamo

- **ACTIVO**: Préstamo en curso
- **VENCIDO**: Pasó la fecha de devolución
- **FINALIZADO**: Libro devuelto

### Notificaciones Automáticas

El sistema envía notificaciones cuando:
- Se realiza un préstamo
- Se devuelve un libro
- Un préstamo está vencido

## 🎯 Características Técnicas

### Polimorfismo en Acción

El método `realizarPrestamo()` utiliza polimorfismo para determinar el plazo de préstamo:

```java
// Cada tipo de usuario retorna valores diferentes
usuario.getDiasPrestamo(); // Estudiante: 15, Profesor: 30, Admin: 60
usuario.getLimitePrestamos(); // Estudiante: 3, Profesor: 10, Admin: ∞
```

### Composición

Las reseñas están compuestas por Usuario y Libro:

```java
Resena {
    private Usuario usuario;  // Composición
    private Libro libro;      // Composición
}
```

### Agregación

La Biblioteca agrega colecciones de entidades:

```java
Biblioteca {
    private List<Libro> libros;
    private List<Usuario> usuarios;
    private List<Prestamo> prestamos;
}
```

## 📄 Licencia

Proyecto educativo - Universidad de Manizales

---

**Desarrollado con ❤️ usando Spring Boot y conceptos avanzados de POO**
