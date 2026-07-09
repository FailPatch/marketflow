# Manual Tecnico

## Capas

- `controller`: recibe solicitudes HTTP.
- `service`: contiene reglas de negocio.
- `dao`: simula persistencia y mantiene separada la capa de datos.
- `model`: entidades del dominio.
- `templates`: vistas HTML5 con Thymeleaf.
- `static`: CSS y JavaScript.

## Patrones usados

- MVC: separa controlador, modelo y vista.
- DAO: separa el acceso a datos.
- Service Layer: centraliza reglas de negocio.

## Despliegue

El proyecto se empaqueta como JAR ejecutable de Spring Boot y puede ejecutarse en Google Cloud Run mediante Docker.

