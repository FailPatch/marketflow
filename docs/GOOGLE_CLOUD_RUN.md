# Despliegue en Google Cloud Run

Esta version del proyecto esta lista para desplegarse en Google Cloud Run usando el `Dockerfile`.

## 1. Requisitos

- Tener una cuenta de Google Cloud.
- Crear un proyecto en Google Cloud.
- Activar facturacion en el proyecto.
- Instalar Google Cloud CLI.

## 2. Iniciar sesion

```powershell
gcloud auth login
```

## 3. Seleccionar tu proyecto

Reemplaza `TU_PROJECT_ID` por el ID real de tu proyecto:

```powershell
gcloud config set project TU_PROJECT_ID
```

## 4. Activar servicios necesarios

```powershell
gcloud services enable run.googleapis.com cloudbuild.googleapis.com artifactregistry.googleapis.com
```

## 5. Ubicarte en la carpeta del proyecto

```powershell
cd C:\Users\megag\Documents\Codex\2026-05-24\files-mentioned-by-the-user-avance\marketplace-cloud
```

## 6. Compilar localmente antes de subir

```powershell
mvn clean package
```

## 7. Subir a Cloud Run

Opcion recomendada usando el Dockerfile:

```powershell
gcloud builds submit --tag gcr.io/TU_PROJECT_ID/marketflow
gcloud run deploy marketflow --image gcr.io/TU_PROJECT_ID/marketflow --platform managed --region us-central1 --allow-unauthenticated
```

Cuando termine, Google Cloud mostrara una URL parecida a:

```text
https://marketflow-xxxxx-uc.a.run.app
```

Esa es la URL publica de tu marketplace.

## 8. Variables Supabase

El archivo `.env` no se sube a Google Cloud por seguridad. Si luego conectas Supabase real, configura las variables en Cloud Run:

```powershell
gcloud run services update marketflow --region us-central1 --set-env-vars SUPABASE_URL="https://TU-PROYECTO.supabase.co",SUPABASE_ANON_KEY="TU_KEY",SUPABASE_DB_URL="jdbc:postgresql://db.TU-PROYECTO.supabase.co:5432/postgres?sslmode=require",SUPABASE_DB_USER="postgres",SUPABASE_DB_PASSWORD="TU_PASSWORD"
```

## 9. Actualizar despues de cambios

Cada vez que edites el proyecto:

```powershell
mvn clean package
gcloud builds submit --tag gcr.io/TU_PROJECT_ID/marketflow
gcloud run deploy marketflow --image gcr.io/TU_PROJECT_ID/marketflow --platform managed --region us-central1 --allow-unauthenticated
```

