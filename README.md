
# Multi-store Management System
## Descripción del Proyecto
Este proyecto es un sistema de gestión de tiendas múltiples (Multi-store management system) construido con Spring Boot. La aplicación proporciona una API RESTful para la gestión de usuarios, roles y tiendas, con un enfoque en la seguridad a través de la autenticación por token JWT (JSON Web Token). El sistema está diseñado para inicializar una base de datos con un usuario administrador y una tienda por defecto al arrancar.

### Tecnologías Utilizadas  
Backend: Java 17, Spring Boot 3.5.4  
Base de datos: PostgreSQL
Persistencia: Spring Data JPA, Hibernate  
Seguridad: Spring Security, JWT (JSON Web Token), PasswordEncoder (BCrypt)  
Herramientas de desarrollo: Maven, Insomnia e Intelijj  

## Características Principales
- Autenticación segura: Implementación de JWT para proteger los endpoints.

- Control de acceso: Gestión de roles y permisos para usuarios.

- Inicialización de datos: La clase InitializerRunner crea automáticamente un usuario administrador (ADMIN) y una tienda por defecto si la base de datos está vacía.

- Validación de datos: Uso de anotaciones de validación (@NotBlank, etc.) en los DTOs para garantizar la integridad de la información.

- API RESTful: Endpoints bien definidos para la gestión de entidades.

## Cómo Empezar
Prerrequisitos
Java 17 o superior instalado.  
Maven instalado.  
Una instancia de PostgreSQL corriendo localmente o en un servidor.  
Insomnia o Postman para probar los endpoints.  
Configuración del Proyecto
Clona el repositorio:

#### Bash ####

git clone https://github.com/DarienTejedor/ClothingStoreInventorySystem.git
cd multi-store-management-system
Configura la base de datos:  
Recuerda crear la base de datos Postgres y adjuntar en el propierties los valores.  
Abre el archivo src/main/resources/application.properties y configura las credenciales de tu base de datos PostgreSQL:

#### Properties ####

spring.datasource.url=jdbc:postgresql://localhost:5432/(tu_base_de_datos)  
spring.datasource.username=(tu_usuario)  
spring.datasource.password=(tu_contraseña)  
spring.jpa.hibernate.ddl-auto=update  
spring.jpa.show-sql=true  

#### Ejecuta la aplicación: ####
Puedes ejecutar la aplicación directamente desde tu IDE (como IntelliJ o VS Code) o usando Maven:

La aplicación, al arrancar por primera vez, creará un usuario administrador con las siguientes credenciales:

loginUser: ADMIN  
password: admin123

Autenticación y obtención de token
URL: POST http://localhost:8080/api/auth/login

Cuerpo de la solicitud (JSON):

JSON

{  
"loginUser": "ADMIN",  
"password": "admin123"  
}  

Respuesta: Un token JWT. Debes usar este token en el encabezado Authorization (con el prefijo Bearer) para acceder a otros endpoints protegidos.

#### Obtener la lista de usuarios ####
URL: GET http://localhost:8080/api/users

Headers: Authorization: Bearer (TU_TOKEN_JWT)

Descripción: Devuelve la lista completa de usuarios paginada.

## Estado del Proyecto ##
El proyecto se encuentra en una fase funcional y lista para ser utilizada como base. La inicialización de datos y la seguridad están completamente implementadas y operativas, y la API ya cuenta con la estructura necesaria para gestionar entidades clave como usuarios, tiendas, productos, y más. El siguiente paso es seguir desarrollando las lógicas de negocio específicas y los endpoints adicionales necesarios para un sistema de gestión completo añ igual que otro tipo de funcionalidades.