# Diagrama de Clases - Sistema de Biblioteca Pública

## Visualización del Diagrama

Para visualizar el diagrama UML contenido en `class-diagram.puml`, puedes usar:

### Opción 1: PlantUML Plugin (Recomendado)
1. **IntelliJ IDEA**: Instala el plugin "PlantUML integration"
2. Abre el archivo `class-diagram.puml`
3. El diagrama se renderizará automáticamente

### Opción 2: Online
1. Visita: https://www.plantuml.com/plantuml/uml/
2. Copia el contenido de `class-diagram.puml`
3. Pégalo en el editor online

### Opción 3: VS Code
1. Instala la extensión "PlantUML"
2. Abre `class-diagram.puml`
3. Presiona `Alt + D` para ver el preview

---

## Estructura del Diagrama

### 📦 Packages

#### **enums**
- `UserType`: STUDENT, TEACHER, ADMINISTRATOR
- `BookGenre`: FICTION, NON_FICTION, SCIENCE, HISTORY, TECHNOLOGY, ART, PHILOSOPHY, BIOGRAPHY, CHILDREN, ACADEMIC
- `LoanStatus`: ACTIVE, OVERDUE, COMPLETED

#### **interfaces**
- `Notificable`: Define el contrato para envío de notificaciones

#### **model**
Contiene todas las clases del dominio del sistema

---

## 🔗 Tipos de Relaciones

### 1️⃣ **Herencia (Generalization)**
```
User (abstract)
  ├── Student
  ├── Teacher
  └── Administrator
```

**Características:**
- `User` es clase abstracta
- Define métodos abstractos: `getLoanLimit()`, `getLoanDays()`
- Implementa interfaz `Notificable`
- Cada subclase implementa sus propios límites y días de préstamo

### 2️⃣ **Implementación de Interfaces**
```
User implements Notificable
  ├── sendNotification(String message)
  └── getContact()
```

**Polimorfismo:**
- Permite tratar a todos los usuarios de forma uniforme
- Cada tipo de usuario puede tener comportamiento específico

### 3️⃣ **Composición (Composition)**
```
Review
  ├── *---> User (composed by)
  └── *---> Book (composed by)
```

**Características:**
- Review NO puede existir sin User y Book
- Relación fuerte y dependiente
- Ciclo de vida vinculado

### 4️⃣ **Agregación (Aggregation)**
```
Library
  ├── o---> List<Book>
  ├── o---> List<User>
  ├── o---> List<Loan>
  └── o---> List<Review>
```

**Características:**
- Library contiene colecciones pero no es dueña exclusiva
- Los objetos pueden existir independientemente
- Relación "tiene-un" (has-a)

### 5️⃣ **Asociación (Association)**
```
Loan ------> User (borrows)
Loan ------> Book (borrowed)
Reservation ------> User (reserves)
Reservation ------> Book (reserved)
Notification ------> User (notifies)
Author ------> Book (writes)
Publisher ------> Book (publishes)
```

**Características:**
- Relaciones de uso o referencia
- No implica propiedad
- Navegabilidad definida

---

## 📊 Resumen de Clases

### Clase Abstracta: `User`
- **Atributos**: id, firstName, lastName, email, phone, type, notifications
- **Métodos abstractos**: getLoanLimit(), getLoanDays()
- **Métodos concretos**: sendNotification(), getContact()
- **Propósito**: Demostrar polimorfismo y herencia

### Clases Concretas de Usuario:

#### `Student`
- Límite de préstamos: **3 libros**
- Días de préstamo: **15 días**
- Atributos específicos: major, semester

#### `Teacher`
- Límite de préstamos: **10 libros**
- Días de préstamo: **30 días**
- Atributos específicos: department, specialization

#### `Administrator`
- Límite de préstamos: **Ilimitado**
- Días de préstamo: **60 días**
- Atributos específicos: role, fullPermission

### Entidades Principales:

#### `Book`
- Representa los libros de la biblioteca
- Control de disponibilidad y cantidades
- Métodos: isAvailable(), borrow(), returnBook()

#### `Loan`
- Gestiona los préstamos de libros
- Control de fechas y estados
- Métodos: isOverdue(), returnBook(), markOverdue(), getDelayDays()

#### `Review`
- Reseñas y calificaciones de libros
- Composición con User y Book
- Sistema de aprobación

#### `Reservation`
- Reservas de libros no disponibles
- Control de expiración
- Métodos: isExpired(), cancel(), complete()

#### `Notification`
- Sistema de notificaciones a usuarios
- Tipos: LOAN, RETURN, OVERDUE, GENERAL

#### `Library`
- Clase principal que agrega todas las entidades
- Gestión centralizada del sistema

#### `Author` y `Publisher`
- Información sobre autores y editoriales
- Relación con múltiples libros

---

## 🎯 Conceptos de POO Demostrados

### ✅ **Encapsulación**
- Todos los atributos son privados
- Acceso mediante getters/setters (Lombok @Data)

### ✅ **Herencia**
- `Student`, `Teacher`, `Administrator` heredan de `User`
- Reutilización de código y estructura común

### ✅ **Polimorfismo**
- Métodos abstractos en `User`: getLoanLimit(), getLoanDays()
- Cada subclase implementa su propio comportamiento
- Uso en `LoanService.performLoan()`:
  ```java
  User user = userService.getById(userId);
  int days = user.getLoanDays(); // Polimorfismo en acción
  ```

### ✅ **Abstracción**
- Interfaz `Notificable`
- Clase abstracta `User`
- Ocultan detalles de implementación

### ✅ **Composición**
- `Review` compuesta por `User` y `Book`
- Dependencia fuerte

### ✅ **Agregación**
- `Library` agrega colecciones de entidades
- Dependencia débil

### ✅ **Asociación**
- Relaciones entre entidades (Loan-User, Loan-Book, etc.)

---

## 📝 Notas de Diseño

### Patrones Aplicados:
1. **Repository Pattern**: Separación de lógica de persistencia
2. **Service Layer**: Lógica de negocio centralizada
3. **DTO Pattern**: Transferencia de datos entre capas
4. **Dependency Injection**: Inyección de dependencias con Spring

### Características del Sistema:
- ✅ Sistema completo de gestión de biblioteca
- ✅ Diferentes tipos de usuarios con privilegios distintos
- ✅ Sistema de préstamos con validaciones
- ✅ Sistema de reservas
- ✅ Sistema de notificaciones
- ✅ Reseñas y calificaciones
- ✅ Persistencia en CSV
- ✅ API REST completa

---

## 🔄 Flujo de Operaciones Principales

### Préstamo de Libro (Loan):
```
1. User solicita préstamo de Book
2. Sistema verifica:
   - Disponibilidad del libro
   - Límite de préstamos del usuario (polimorfismo)
3. Si válido:
   - Crea Loan
   - Reduce availableQuantity del Book
   - Calcula fecha de devolución usando user.getLoanDays() (polimorfismo)
   - Envía Notification al User (polimorfismo)
```

### Reserva de Libro:
```
1. User reserva Book no disponible
2. Sistema crea Reservation con fecha de expiración
3. Cuando Book esté disponible:
   - Notifica al User
   - Permite conversión a Loan
```

### Sistema de Reseñas:
```
1. User crea Review de Book leído
2. Composición: Review requiere User y Book
3. Administrator aprueba Review
4. Review visible para todos los usuarios
```

---

*Última actualización: 2025-11-02*
