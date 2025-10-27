# Documentación Appmovil

## Resumen
Esta es una aplicación backend escrita con Spring Boot que maneja usuarios y sus cupos. El proyecto sigue una arquitectura en capas (controlador → servicio → repositorio) y usa JPA/Hibernate para persistencia.

## Stack técnico
- Framework: Spring Boot 3.5.7
- Java: JDK 17+
- Persistencia (producción): MySQL (configuración en `src/main/resources/application.properties`)
- Persistencia (tests): H2 en memoria (configuración en `src/test/resources/application-test.properties`)
- ORM: JPA / Hibernate
- Build: Maven
- Tests: JUnit 5, MockMvc (unit), TestRestTemplate (integración)

## Decisiones de arquitectura (breve)
- Modelo simple: `Usuario` (1:N) → `Cupo` (N:1).
- Capas separadas: controladores para HTTP, servicios para lógica y repositorios para acceso a datos.
- Las entidades se exponen directamente en respuestas, pero a futuro se recomienda usar DTOs para separar contrato y dominio.

## Estrategia de pruebas y qué demuestran
- Unit tests (MockMvc): verifican el comportamiento del controlador en aislamiento (rutas, códigos HTTP, validaciones superficiales).
  - Archivos: `src/test/java/.../controller/UsuarioControllerTest.java`
  - Demuestra: que el controlador responde con los códigos esperados y delega correctamente al servicio.

- Integration test (arranque real + H2): `EndToEndIntegrationTest`.
  - Técnica: @SpringBootTest(webEnvironment = RANDOM_PORT) + TestRestTemplate.
  - Demuestra: que la aplicación puede arrancar con el perfil `test`, que Hibernate crea las tablas en H2, y que el flujo POST→GET funciona end-to-end (creación en BD y lectura posterior).

## Ejecución

1) Compilar

```bash
# Desde la raíz del proyecto
./mvnw clean install
```

2) Ejecutar la aplicación (con MySQL - perfil por defecto)

```bash
./mvnw spring-boot:run
```

Esto arrancará en el puerto 8080 (según `application.properties`). Atención: las llamadas POST escribirán en la base de datos configurada (MySQL).

3) Ejecutar la aplicación con perfil de tests (H2 en memoria — NO tocará MySQL)

```bash
./mvnw -Dspring-boot.run.profiles=test spring-boot:run
```

4) Ejecutar tests

```bash
# Ejecutar toda la suite de tests (unit + integración)
./mvnw clean test

# Ejecutar sólo los tests unitarios (ejemplo)
./mvnw -Dtest=com.example.appmovil.controller.UsuarioControllerTest test

# Ejecutar sólo la prueba de integración end-to-end
./mvnw -Dtest=com.example.appmovil.integration.EndToEndIntegrationTest test
```

Qué demuestra cada comando de test:
- `clean test`: compila y ejecuta todos los tests, valida que el comportamiento global no rompa nada.
- `UsuarioControllerTest`: prueba las rutas y respuestas del controlador con mocks (rápido, aislado).
- `EndToEndIntegrationTest`: arranca la app con H2 y prueba el flujo real (útil para detectar problemas de configuración de JPA/Hibernate y compatibilidad de DDL).

## Endpoints y ejemplos (curl)

1) POST — crear un `Cupo` para un `Usuario` (crea Usuario si no existe)

Propósito: crear la cuota/registro asociado a un usuario + móvil.

```bash
curl -s -X POST "http://localhost:8080/usuarios/testuser/cupos?movil=999999999&saldo=50.5&datos=2.75&plataforma=android" \
  -H "Content-Type: application/json" -w "\nHTTP:%{http_code}\n"
```

- Respuesta esperada (si todo va bien): HTTP 201 y cuerpo JSON del `Cupo` creado. Ejemplo:

  {
    "id": 1,
    "movil": "999999999",
    "saldo": 50.5,
    "datos": 2.75,
    "plataforma": "android",
    "usuario": { "id": 1, "nombre": "testuser" }
  }

2) GET — recuperar el `Cupo` por usuario y móvil

Propósito: validar que la cuota fue persistida y se puede recuperar.

```bash
curl -s "http://localhost:8080/usuarios/testuser/cupos/999999999" -w "\nHTTP:%{http_code}\n"
```

- Respuesta esperada: HTTP 200 y el mismo JSON del recurso creado.

Notas de seguridad/operación:
- Si arrancas la app sin `-Dspring-boot.run.profiles=test`, las operaciones POST escribirán en tu base de datos MySQL configurada. Usa el perfil `test` para pruebas locales sin efectos secundarios.

## Consideraciones sobre serialización JSON
- Para evitar ciclos de referencia entre `Usuario` y `Cupo` (que provocaban excepciones de serialización), se añadió `@JsonIgnore` en `Usuario.cupos`. Esto mantiene la respuesta de `Cupo` legible (incluye `usuario`), pero evita que `usuario` vuelva a incluir la lista de `cupos`.
- Alternativas mejores (a futuro): usar `@JsonManagedReference` / `@JsonBackReference` o transformar entidades a DTOs antes de serializar.

## Operaciones adicionales útiles
- Ejecutar solo pruebas de integración y ver logs (útil para depurar DDL/DDL errors):

```bash
./mvnw -Dtest=com.example.appmovil.integration.EndToEndIntegrationTest test -e
tail -n 200 target/surefire-reports/*
```

## Implementaciones a mejorar
- Convertir respuestas públicas a DTOs para evitar exponer entidades JPA directamente.
- Añadir validación de parámetros (ej. usar @Valid y DTOs) en los endpoints.
- Crear pruebas adicionales para casos de error (GET inexistente → 404, validaciones → 400).

## Licencia
Proyecto bajo licencia MIT — ver el archivo LICENSE para detalles.
