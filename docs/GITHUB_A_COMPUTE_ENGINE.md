# Subir a GitHub y desplegar en Compute Engine

Este flujo sirve si quieres subir primero el proyecto a GitHub y luego instalarlo en tu VM de Google Cloud.

## 1. Preparar el proyecto antes de subir a GitHub

No subas archivos pesados ni secretos. El proyecto ya tiene `.gcloudignore` y `.dockerignore`, pero para GitHub revisa que no subas:

- `target/`
- `.env`
- archivos `.zip`
- logs

Si tu repositorio no tiene `.gitignore`, crea uno con:

```text
target/
.env
*.zip
*.war
server.log
server-error.log
.idea/
.vscode/
```

## 2. Crear repositorio en GitHub

1. Entra a GitHub.
2. Crea un repositorio, por ejemplo:

```text
marketflow
```

3. Copia la URL del repositorio:

```text
https://github.com/TU_USUARIO/marketflow.git
```

## 3. Subir el proyecto desde VS Code

Desde la carpeta `marketplace-cloud`:

```powershell
git init
git add .
git commit -m "Version final MarketFlow"
git branch -M main
git remote add origin https://github.com/TU_USUARIO/marketflow.git
git push -u origin main
```

Si ya tenias Git iniciado:

```powershell
git add .
git commit -m "Actualiza MarketFlow"
git push
```

## 4. Entrar a la VM por SSH

En Google Cloud:

1. Ve a `Compute Engine > Instancias de VM`.
2. Busca `ventas-por-cliente`.
3. Presiona `SSH`.

## 5. Instalar Git, Java y Maven en la VM

Dentro del SSH:

```bash
sudo apt update
sudo apt install -y git openjdk-17-jdk maven
java -version
mvn -version
```

## 6. Clonar el repositorio en la VM

```bash
git clone https://github.com/TU_USUARIO/marketflow.git
cd marketflow
```

Si tu repo tiene todo el workspace y el proyecto esta dentro de `marketplace-cloud`, usa:

```bash
cd marketplace-cloud
```

## 7. Compilar en la VM

```bash
mvn clean package
```

## 8. Ejecutar la aplicacion

```bash
java -jar target/marketplace-cloud-1.0.0.jar
```

Si ves:

```text
Tomcat started on port 8080
```

la aplicacion ya esta corriendo.

## 9. Abrir puerto 8080

Desde tu PC con Google Cloud CLI:

```powershell
gcloud compute firewall-rules create allow-marketflow-8080 --allow tcp:8080 --source-ranges 0.0.0.0/0 --target-tags marketflow-server
gcloud compute instances add-tags ventas-por-cliente --tags marketflow-server --zone us-central1-b
```

Luego entra con:

```text
http://IP_EXTERNA:8080
```

## 10. Dejarlo corriendo como servicio

En la VM, dentro de la carpeta del proyecto:

```bash
sudo mkdir -p /opt/marketflow
sudo cp target/marketplace-cloud-1.0.0.jar /opt/marketflow/app.jar
sudo nano /etc/systemd/system/marketflow.service
```

Pega:

```ini
[Unit]
Description=MarketFlow Marketplace
After=network.target

[Service]
WorkingDirectory=/opt/marketflow
ExecStart=/usr/bin/java -jar /opt/marketflow/app.jar
Restart=always
RestartSec=5
User=root
Environment=PORT=8080

[Install]
WantedBy=multi-user.target
```

Activa:

```bash
sudo systemctl daemon-reload
sudo systemctl enable marketflow
sudo systemctl start marketflow
sudo systemctl status marketflow
```

## 11. Actualizar cuando hagas cambios

En tu PC:

```powershell
git add .
git commit -m "Mejoras del marketplace"
git push
```

En la VM:

```bash
cd marketflow
git pull
mvn clean package
sudo cp target/marketplace-cloud-1.0.0.jar /opt/marketflow/app.jar
sudo systemctl restart marketflow
```

