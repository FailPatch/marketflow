# Explicacion de MVC y DAO

La aplicacion usa una arquitectura MVC con capa DAO.

Modelo:

- Representa entidades del sistema como `User`, `Product`, `Employee`, `Provider`, `Article` y `PurchaseOrder`.

Vista:

- Archivos HTML5 con Thymeleaf ubicados en `src/main/resources/templates`.
- Muestran catalogo, login, registro, carrito, panel vendedor, panel admin, dashboard y gestion.

Controlador:

- Clases ubicadas en `controller`.
- Reciben las rutas web, validan el rol y conectan la vista con los servicios.

Servicio:

- Clases ubicadas en `service`.
- Contienen reglas de negocio como control de stock, compras, penalizaciones y ordenes de compra.

DAO:

- Clases ubicadas en `dao`.
- Encapsulan el acceso a datos. En esta version usan listas en memoria para demo; pueden migrarse a base de datos sin cambiar las vistas.

Esta arquitectura reemplaza microservicios porque entrega separacion por capas y modulos sin la complejidad de desplegar varios servicios independientes.
