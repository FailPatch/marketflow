# Configuracion de Supabase

Supabase usa PostgreSQL. Para conectar el proyecto:

1. Crea un proyecto en Supabase.
2. Entra a `Project Settings > Database`.
3. Copia el host, usuario, password y connection string.
4. Edita el archivo `.env` del proyecto.
5. Reemplaza `TU-PROYECTO`, `TU_SUPABASE_ANON_KEY` y `TU_PASSWORD_DE_BASE_DE_DATOS`.

Ejemplo de JDBC:

```text
SUPABASE_DB_URL=jdbc:postgresql://db.TU-PROYECTO.supabase.co:5432/postgres?sslmode=require
```

Nota: la version actual mantiene datos en memoria para la presentacion. El `.env`, el driver PostgreSQL y el esquema SQL dejan preparado el proyecto para migrar los DAO a Supabase.

El archivo `cloud-sql/schema.sql` ya contempla campos de rating:

- `rating_average`: promedio visible del producto.
- `review_count`: cantidad de reseñas usadas para calcular el promedio.
