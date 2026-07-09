param(
    [string]$InstanceName = "ventas-por-cliente",
    [string]$Zone = "us-central1-b",
    [string]$JarPath = "target\marketplace-cloud-1.0.0.jar"
)

Write-Host "Compilando MarketFlow..."
mvn clean package

Write-Host "Subiendo JAR a la VM $InstanceName..."
gcloud compute scp $JarPath "${InstanceName}:~/marketplace-cloud-1.0.0.jar" --zone $Zone

Write-Host "Listo. Ahora entra por SSH a la VM y ejecuta:"
Write-Host "java -jar marketplace-cloud-1.0.0.jar"
