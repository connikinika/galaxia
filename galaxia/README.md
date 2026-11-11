# Galaxia - entrada rápida

Spring Boot 4.1.2 + Thymeleaf (Java 21), misma estructura que `pueba`.

    ./mvnw spring-boot:run        (Windows: mvnw.cmd spring-boot:run)

- Invitado: http://localhost:8080/entrada
- Admin:    http://localhost:8080/admin  (usuario `admin`, clave `admin123`; cámbialas en application.properties)

Los archivos subidos quedan en ./uploads. Los códigos viven en memoria: al reiniciar se pierden (igual que el catálogo de vuelos).
