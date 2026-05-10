
````md
# AGENTS.md

## Contexto general del proyecto

Este repositorio contiene el microservicio `records-service`, perteneciente al proyecto **Colegio Bernardo O'Higgins**.

El sistema general corresponde a una plataforma de libro de clases digital basada en arquitectura de microservicios.

Actualmente el sistema contempla los siguientes componentes:

- `users-service`
- `academy-service`
- `records-service`
- `api-gateway`
- `frontend`

El microservicio de comunicaciones será implementado más adelante, por lo tanto no debe crearse ni modificarse en este repositorio.

---

## Responsabilidad del microservicio

El microservicio `records-service` es responsable de gestionar:

- Asistencias de estudiantes.
- Anotaciones de estudiantes.
- Consultas por estado de asistencia.
- Consultas por tipo de anotación.
- Consultas por estudiante.
- Consultas por rangos de fechas.

Este microservicio debe mantener su propia base de datos independiente, siguiendo el principio de independencia de datos en una arquitectura de microservicios.

---

## Stack tecnológico obligatorio

Utilizar las siguientes tecnologías:

- Java 21
- Spring Boot 4.0.6
- Maven
- PostgreSQL
- Spring Data JPA
- Liquibase
- Lombok
- application.properties

No cambiar versiones, gestor de dependencias ni estructura base del proyecto sin autorización explícita.

---

## Patrones arquitectónicos y de diseño

Este microservicio debe aplicar los siguientes patrones:

### Arquitectura por capas

El proyecto debe seguir una arquitectura por capas:

```text
Controller -> Service -> Repository -> Database
````

La responsabilidad de cada capa es la siguiente:

* `controller`: expone endpoints REST.
* `service`: define la lógica de negocio.
* `service.impl`: implementa la lógica de negocio.
* `repository`: accede a la base de datos usando Spring Data JPA.
* `entity`: representa las tablas de la base de datos.
* `dto`: define objetos de entrada y salida para las APIs.
* `mapper`: transforma entidades a DTOs y DTOs a entidades cuando corresponda.
* `factory`: centraliza la creación de objetos de dominio o entidades.
* `exception`: contiene excepciones personalizadas y manejo global de errores.
* `config`: contiene configuraciones generales del microservicio.

### Repository Pattern

Spring Data JPA será utilizado como implementación del Repository Pattern.

Los repositories deben extender de `JpaRepository`.

Ejemplo:

```java
public interface AsistenciaRepository extends JpaRepository<Asistencia, Long> {
}
```

El controller no debe acceder directamente al repository.

### Factory Pattern

Se debe utilizar Factory Pattern para centralizar la creación de objetos principales del dominio, especialmente al convertir datos de entrada en entidades.

Uso recomendado:

```text
DTO de entrada -> Factory -> Entity
```

Ejemplo conceptual:

```text
AsistenciaRequestDTO -> AsistenciaFactory -> Asistencia
AnotacionRequestDTO -> AnotacionFactory -> Anotacion
```

El Factory Pattern debe utilizarse preferentemente en:

* creación de entidades desde DTOs
* actualización controlada de entidades existentes
* construcción de objetos con reglas mínimas de negocio

Ejemplos de clases esperadas:

```text
AsistenciaFactory
AnotacionFactory
```

Reglas para usar Factory Pattern:

* No crear entities directamente dentro del controller.
* Evitar lógica de construcción repetida dentro del service.
* Usar factories para crear o actualizar entidades desde DTOs.
* Mantener los factories simples y enfocados.
* No usar Factory Pattern para operaciones que solo consultan datos.

Ejemplo de flujo recomendado:

```text
Controller -> Service -> Factory -> Repository
```

Ejemplo práctico:

```text
POST /asistencias
        ↓
AsistenciaController
        ↓
AsistenciaService
        ↓
AsistenciaFactory
        ↓
AsistenciaRepository
```

---

## Estructura de paquetes esperada

Mantener esta estructura:

```text
src/main/java/cl/colegio/records_service
├── config
├── controller
├── dto
├── entity
├── exception
├── factory
├── mapper
├── repository
├── service
│   └── impl
└── RecordsServiceApplication.java
```

No crear paquetes innecesarios sin justificación.

---

## Reglas importantes de desarrollo

* No colocar lógica de negocio en los controllers.
* Los controllers deben llamar a services.
* Los services deben llamar a repositories.
* Los services pueden usar factories para crear o actualizar entidades.
* No acceder directamente a repositories desde controllers.
* No escribir SQL manual en controllers ni services.
* Usar DTOs para requests y responses cuando se creen o actualicen registros.
* Usar entities únicamente para persistencia.
* Usar factories para crear entities desde DTOs.
* Usar mappers para convertir entities a DTOs de respuesta cuando corresponda.
* Usar inyección por constructor.
* Preferir `@RequiredArgsConstructor` de Lombok.
* Mantener nombres claros y consistentes.
* Las clases deben usar PascalCase.
* Los métodos y variables deben usar camelCase.
* No cambiar nombres de paquetes existentes.
* No cambiar el nombre de la base de datos.
* No cambiar el puerto del servicio salvo que se solicite explícitamente.

---

## Base de datos

Este microservicio utiliza PostgreSQL.

Base de datos:

```text
records_db
```

Usuario local sugerido:

```text
postgres
```

Contraseña local sugerida:

```text
postgres
```

Puerto:

```text
5432
```

---

## Reglas de base de datos

* No crear tablas manualmente.
* Todas las tablas deben crearse mediante Liquibase.
* Mantener `spring.jpa.hibernate.ddl-auto=none`.
* No usar `ddl-auto=create`.
* No usar `ddl-auto=update`.
* No definir claves foráneas hacia bases de datos de otros microservicios.
* Los identificadores externos se manejan como referencias lógicas.

---

## Referencias lógicas a otros microservicios

Los siguientes campos no son claves foráneas físicas:

* `idEstudiante`
* `idDocente`
* `idCurso`

Estos campos representan referencias lógicas a otros microservicios:

* `idEstudiante`: referencia lógica a `users-service`.
* `idDocente`: referencia lógica a `users-service`.
* `idCurso`: referencia lógica a `academy-service`.

No crear relaciones físicas entre bases de datos de distintos microservicios.

---

## Modelo de datos actual

### Tabla `asistencia`

Representa el registro de asistencia de un estudiante.

Campos:

* `id_asistencia`
* `id_estudiante`
* `id_docente`
* `id_curso`
* `fecha`
* `estado_asistencia`
* `observacion`

Entidad Java esperada:

```text
Asistencia
```

---

### Tabla `anotacion`

Representa una anotación asociada a un estudiante.

Campos:

* `id_anotacion`
* `id_estudiante`
* `id_docente`
* `fecha`
* `tipo_anotacion`
* `descripcion`

Entidad Java esperada:

```text
Anotacion
```

---

## Liquibase

Los archivos de Liquibase deben estar en:

```text
src/main/resources/db/changelog
```

El archivo principal es:

```text
db.changelog-master.xml
```

Los cambios deben organizarse en archivos separados, por ejemplo:

```text
001-create-asistencia.xml
002-create-anotacion.xml
003-insert-data.xml
```

---

## Reglas de Liquibase

* No editar changesets ya aplicados si la base de datos no será reiniciada.
* Para cambios nuevos, crear un nuevo changeset.
* Usar IDs únicos por changeset.
* Usar nombres descriptivos.
* Usar Liquibase para insertar datos iniciales.
* La evaluación requiere al menos 10 registros iniciales, por lo tanto se deben mantener registros de prueba en los changelogs cuando corresponda.

---

## Endpoints requeridos

Cada entidad debe tener CRUD completo.

---

### Endpoints para Asistencia

Implementar:

```text
GET /asistencias
GET /asistencias/{id}
POST /asistencias
PUT /asistencias/{id}
DELETE /asistencias/{id}
```

Endpoints adicionales requeridos:

```text
GET /asistencias/estado/{estado}
GET /asistencias/fecha?inicio=YYYY-MM-DD&fin=YYYY-MM-DD
```

Ejemplos de estados válidos:

```text
PRESENTE
AUSENTE
ATRASADO
JUSTIFICADO
```

---

### Endpoints para Anotacion

Implementar:

```text
GET /anotaciones
GET /anotaciones/{id}
POST /anotaciones
PUT /anotaciones/{id}
DELETE /anotaciones/{id}
```

Endpoints adicionales requeridos:

```text
GET /anotaciones/tipo/{tipo}
GET /anotaciones/estudiante/{idEstudiante}
```

Ejemplos de tipos válidos:

```text
POSITIVA
NEGATIVA
OBSERVACION
```

---

## DTOs recomendados

Crear DTOs separados para requests y responses.

Ejemplo para asistencia:

```text
AsistenciaRequestDTO
AsistenciaResponseDTO
```

Ejemplo para anotación:

```text
AnotacionRequestDTO
AnotacionResponseDTO
```

No es recomendable exponer directamente las entities en las respuestas finales de la API.

---

## Manejo de errores

Implementar manejo de errores de forma centralizada.

Paquete esperado:

```text
exception
```

Clases recomendadas:

```text
ResourceNotFoundException
GlobalExceptionHandler
```

Opcional:

```text
BadRequestException
```

---

## Códigos HTTP esperados

Utilizar códigos HTTP adecuados:

```text
200 OK
```

Para consultas exitosas.

```text
201 Created
```

Para creación exitosa de registros.

```text
204 No Content
```

Para eliminación exitosa.

```text
400 Bad Request
```

Para datos inválidos.

```text
404 Not Found
```

Cuando un recurso no existe.

---

## Validaciones

Usar Bean Validation cuando corresponda.

Ejemplos:

* `@NotNull`
* `@NotBlank`
* `@Size`
* `@PastOrPresent`
* `@Positive`

Los DTOs de entrada deben contener validaciones cuando sea necesario.

---

## Repositories

Los repositories deben extender de `JpaRepository`.

Ejemplo:

```java
public interface AsistenciaRepository extends JpaRepository<Asistencia, Long> {
}
```

Agregar métodos derivados de Spring Data JPA cuando sean necesarios.

Ejemplos:

```java
List<Asistencia> findByEstadoAsistencia(String estadoAsistencia);

List<Asistencia> findByFechaBetween(LocalDate inicio, LocalDate fin);

List<Anotacion> findByTipoAnotacion(String tipoAnotacion);

List<Anotacion> findByIdEstudiante(Long idEstudiante);
```

---

## Services

Crear interfaces en el paquete `service`.

Crear implementaciones en el paquete `service.impl`.

Ejemplo:

```text
AsistenciaService
AsistenciaServiceImpl
AnotacionService
AnotacionServiceImpl
```

Los services deben contener la lógica de negocio y validaciones principales.

---

## Controllers

Los controllers deben exponer endpoints REST.

Ejemplos:

```text
AsistenciaController
AnotacionController
```

Los controllers deben:

* recibir requests
* validar entradas con `@Valid`
* llamar a services
* retornar responses adecuadas

No deben contener lógica de negocio compleja.

---

## Factory

Crear factories en el paquete:

```text
factory
```

Clases recomendadas:

```text
AsistenciaFactory
AnotacionFactory
```

Responsabilidades de los factories:

* crear entidades desde DTOs de entrada
* actualizar entidades existentes desde DTOs
* centralizar lógica simple de construcción de objetos

Ejemplo conceptual:

```java
@Component
public class AsistenciaFactory {

    public Asistencia crearDesdeRequest(AsistenciaRequestDTO dto) {
        return Asistencia.builder()
                .idEstudiante(dto.idEstudiante())
                .idDocente(dto.idDocente())
                .idCurso(dto.idCurso())
                .fecha(dto.fecha())
                .estadoAsistencia(dto.estadoAsistencia())
                .observacion(dto.observacion())
                .build();
    }

    public void actualizarDesdeRequest(Asistencia asistencia, AsistenciaRequestDTO dto) {
        asistencia.setIdEstudiante(dto.idEstudiante());
        asistencia.setIdDocente(dto.idDocente());
        asistencia.setIdCurso(dto.idCurso());
        asistencia.setFecha(dto.fecha());
        asistencia.setEstadoAsistencia(dto.estadoAsistencia());
        asistencia.setObservacion(dto.observacion());
    }
}
```

---

## Mapper

Usar clases mapper para convertir entre entities y DTOs de respuesta.

Ejemplos:

```text
AsistenciaMapper
AnotacionMapper
```

El mapper debe encargarse de:

* convertir entity a response DTO
* convertir listas de entities a listas de response DTOs si corresponde

El mapper no debe encargarse de reglas de negocio complejas.

---

## Configuración esperada en application.properties

Mantener una configuración similar a esta:

```properties
spring.application.name=records-service

server.port=8083

spring.datasource.url=jdbc:postgresql://localhost:5432/records_db
spring.datasource.username=postgres
spring.datasource.password=postgres

spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

spring.liquibase.enabled=true
spring.liquibase.change-log=classpath:db/changelog/db.changelog-master.xml
```

No modificar estos valores sin autorización explícita.

---

## Docker

El proyecto utiliza PostgreSQL mediante Docker durante el desarrollo.

Comando sugerido para levantar PostgreSQL en Windows PowerShell:

```powershell
docker run --name records-postgres ^
-e POSTGRES_USER=postgres ^
-e POSTGRES_PASSWORD=postgres ^
-e POSTGRES_DB=records_db ^
-p 5432:5432 ^
-d postgres
```

Comando sugerido para Linux o Mac:

```bash
docker run --name records-postgres \
-e POSTGRES_USER=postgres \
-e POSTGRES_PASSWORD=postgres \
-e POSTGRES_DB=records_db \
-p 5432:5432 \
-d postgres
```

---

## Ejecución del proyecto

En Windows PowerShell:

```powershell
.\mvnw spring-boot:run
```

En Linux o Mac:

```bash
./mvnw spring-boot:run
```

---

## Flujo de trabajo recomendado

Antes de implementar una funcionalidad:

1. Revisar la estructura actual del proyecto.
2. Verificar si requiere cambios en base de datos.
3. Si requiere cambios, crear un nuevo changelog de Liquibase.
4. Crear o actualizar entity.
5. Crear o actualizar repository.
6. Crear o actualizar DTOs.
7. Crear o actualizar factory si se requiere crear o actualizar entidades.
8. Crear o actualizar mapper para responses.
9. Crear o actualizar service.
10. Crear o actualizar service implementation.
11. Crear o actualizar controller.
12. Probar endpoint con navegador, Postman o curl.
13. Hacer commit con mensaje claro.

---

## Commits recomendados

Usar commits pequeños y descriptivos.

Ejemplos:

```text
Add asistencia CRUD
Add anotacion entity and repository
Add Liquibase seed data
Add asistencia factory
Add advanced asistencia filters
Add global exception handler
```

Evitar commits genéricos como:

```text
avance
cambios
final
```

---

## Cosas que NO se deben hacer

* No agregar Spring Security todavía salvo que se solicite.
* No implementar JWT todavía salvo que se solicite.
* No agregar Kafka o RabbitMQ todavía salvo que se solicite.
* No crear el microservicio de comunicaciones en este repositorio.
* No convertir el proyecto a Gradle.
* No cambiar de PostgreSQL a otra base de datos.
* No cambiar el nombre de la base de datos.
* No cambiar el package base.
* No mezclar lógica de controller, service, factory, mapper y repository.
* No crear tablas manualmente en DBeaver.
* No usar `ddl-auto=create` ni `ddl-auto=update`.

---

## Objetivo inmediato del proyecto

Completar el microservicio `records-service` con:

* CRUD completo para asistencia.
* CRUD completo para anotación.
* Endpoints avanzados requeridos por la evaluación.
* Factory Pattern aplicado en la creación y actualización de entidades.
* Repository Pattern aplicado mediante Spring Data JPA.
* Liquibase con creación de tablas y datos iniciales.
* PostgreSQL funcionando en Docker.
* Código organizado por capas.
* Preparación para futura integración con API Gateway, frontend y JWT.

---

## Consideraciones para futuras etapas

Más adelante este microservicio deberá integrarse con:

* `api-gateway`
* `users-service`
* `academy-service`
* frontend React + Vite

La comunicación con otros microservicios debe realizarse mediante HTTP/REST a través del API Gateway o mediante clientes HTTP definidos explícitamente.

No implementar integración entre microservicios hasta que sea solicitada.

---

## Prioridad actual

La prioridad actual es mantener el microservicio simple, funcional y bien estructurado.

Primero debe funcionar correctamente:

```text
Base de datos -> Liquibase -> Entity -> Repository -> Service -> Factory -> Mapper -> Controller -> Endpoint
```

Luego se agregan mejoras como validaciones, Swagger, Docker Compose, API Gateway y JWT.

````

Ahora sí, después de reemplazar el `AGENTS.md`, puedes hacer otro commit pequeño:

```bash
git add AGENTS.md
git commit -m "Update agent instructions with Factory Pattern"
````
