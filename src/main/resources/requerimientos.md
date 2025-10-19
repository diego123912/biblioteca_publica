Proyecto biblioteca publica

# requerimientos

quiero crear un api rest con springboot y Lombok
para la gestión de un sistema de biblioteca digital comunitaria,
donde se administren libros, usuarios, préstamos y reseñas.
La aplicación debe almacenar la información en un archivo separado por comas (CSV) y
permitir operaciones CRUD sobre al menos diez clases distintas.

El sistema debe contemplar:

Encapsulamiento en las clases principales (ej. Libro, Usuario, Prestamo, Reseña).

Herencia para modelar diferentes tipos de usuarios (ej. Estudiante, Profesor, Administrador).

Polimorfismo en los métodos que gestionen préstamos, devoluciones y notificaciones.

Interfaces para definir contratos comunes, como Notificable (para enviar mensajes a usuarios) o Exportable (para manejar persistencia en CSV).

Composición al incluir objetos dentro de otros, como una Reseña compuesta por un Usuario y un Libro.

Agregación al asociar varias entidades, como una Biblioteca que contiene una colección de Libros y Usuarios.

Inclusión de records para modelar datos inmutables, como RegistroActividad.

Uso de enumeradores para estados (ej. EstadoPrestamo con valores: ACTIVO, VENCIDO, FINALIZADO).

Implementación de al menos una clase abstracta, como Usuario, que defina atributos y métodos comunes a Estudiante y Profesor.

La API debe exponer endpoints que permitan realizar las operaciones CRUD de todas las entidades, garantizando la persistencia de los datos en el archivo CSV.
