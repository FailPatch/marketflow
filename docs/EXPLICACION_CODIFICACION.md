# Explicacion de codificacion por modulos

Modulo de acceso:

- `AuthController`, `AuthService` y `UserDao` gestionan login, registro y roles.

Modulo de marketplace:

- `MarketplaceController` y `MarketplaceService` gestionan catalogo, carrito, compra, cupones, comprobantes, metodos de pago y reseñas.

Modulo vendedor:

- Permite publicar productos con nombre, categoria, precio, stock, descripcion e imagen.
- La publicacion queda pendiente hasta que el administrador la apruebe.

Modulo administrador:

- Permite aprobar o pausar productos, ver vendedores, revisar reseñas, penalizar o retirar penalizaciones.
- Si un vendedor llega a cinco penalizaciones, queda inactivo y sus productos se pausan.

Modulo gestion:

- `ManagementController` y `PurchaseOrderService` cubren empleados, proveedores, articulos y ordenes de compra.
- Articulos permite grabar, buscar, actualizar y eliminar.

Modulo auditoria:

- `AuditDao` registra acciones importantes para explicar trazabilidad durante la presentacion.
