# Records Service

Microservicio del proyecto Colegio Bernardo O'Higgins encargado de gestionar registros de asistencia y anotaciones de estudiantes.

## Contexto

Este microservicio forma parte de una arquitectura de microservicios para una plataforma de libro de clases digital.

## Responsabilidad del microservicio

Records Service se encarga de:

- Registrar asistencias de estudiantes.
- Consultar asistencias.
- Registrar anotaciones conductuales o académicas.
- Consultar anotaciones.

## Stack tecnológico

- Java 21
- Spring Boot 4.0.6
- Maven
- PostgreSQL
- Spring Data JPA
- Liquibase
- Lombok
- Docker

## Base de datos

Este microservicio utiliza una base de datos PostgreSQL propia llamada:

records_db

Las tablas son creadas mediante Liquibase.

## Tablas principales

### asistencia

Registra la asistencia de estudiantes por fecha y curso.

### anotacion

Registra anotaciones asociadas a estudiantes y docentes.

## Estructura del proyecto

```text
src/main/java/cl/colegio/records_service
├── controller
├── service
│   └── impl
├── repository
├── entity
├── dto
├── config
├── exception
└── mapper

Ejecutar PostgreSQL con Docker
docker run --name records-postgres ^
-e POSTGRES_USER=postgres ^
-e POSTGRES_PASSWORD=postgres ^
-e POSTGRES_DB=records_db ^
-p 5432:5432 ^
-d postgres

Endpoints esperados
Asistencias
GET /asistencias
GET /asistencias/{id}
POST /asistencias
PUT /asistencias/{id}
DELETE /asistencias/{id}
GET /asistencias/estado/{estado}
GET /asistencias/fecha?inicio=YYYY-MM-DD&fin=YYYY-MM-DD

Anotaciones
GET /anotaciones
GET /anotaciones/{id}
POST /anotaciones
PUT /anotaciones/{id}
DELETE /anotaciones/{id}
GET /anotaciones/tipo/{tipo}
GET /anotaciones/estudiante/{idEstudiante}