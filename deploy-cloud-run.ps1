param(
    [Parameter(Mandatory=$true)]
    [string]$ProjectId,

    [string]$Region = "us-central1",
    [string]$ServiceName = "marketflow"
)

Write-Host "Compilando proyecto..."
mvn clean package

Write-Host "Configurando proyecto Google Cloud: $ProjectId"
gcloud config set project $ProjectId

Write-Host "Activando servicios necesarios..."
gcloud services enable run.googleapis.com cloudbuild.googleapis.com artifactregistry.googleapis.com

Write-Host "Construyendo imagen..."
gcloud builds submit --tag "gcr.io/$ProjectId/$ServiceName"

Write-Host "Desplegando en Cloud Run..."
gcloud run deploy $ServiceName --image "gcr.io/$ProjectId/$ServiceName" --platform managed --region $Region --allow-unauthenticated

Write-Host "Despliegue terminado."
