# Despliegue en Compute Engine VM

Esta guia es para subir MarketFlow a una instancia de VM como la que aparece en Google Cloud Compute Engine.

Instancia vista en consola:

- Nombre: `ventas-por-cliente`
- Zona: `us-central1-b`
- Tipo: VM Linux en Compute Engine

## 1. Compilar el proyecto en tu PC

Desde Visual Studio Code, abre una terminal en:

```powershell
cd C:\Users\megag\Documents\Codex\2026-05-24\files-mentioned-by-the-user-avance\marketplace-cloud
```

Compila:

```powershell
mvn clean package
```

Eso genera:

```text
target\marketplace-cloud-1.0.0.jar
```

## 2. Entrar por SSH a la VM

En Google Cloud:

1. Ve a `Compute Engine > Instancias de VM`.
2. En la fila `ventas-por-cliente`, presiona `SSH`.
3. Se abrira una terminal en el navegador.

## 3. Preparar Java en la VM

Dentro del SSH de la VM, pega:

```bash
sudo apt update
sudo apt install -y openjdk-17-jre
java -version
```

## 4. Subir el JAR a la VM

Opcion A: usando Google Cloud CLI desde tu PC:

```powershell
gcloud compute scp target\marketplace-cloud-1.0.0.jar ventas-por-cliente:/home/$env:USERNAME/marketplace-cloud-1.0.0.jar --zone us-central1-b
```

Si tu usuario de la VM es diferente, usa esta forma:

```powershell
gcloud compute scp target\marketplace-cloud-1.0.0.jar TU_USUARIO_VM@ventas-por-cliente:/home/TU_USUARIO_VM/marketplace-cloud-1.0.0.jar --zone us-central1-b
```

Opcion B: desde la ventana SSH de Google Cloud, usa el menu de la terminal para subir archivo si aparece la opcion `Upload file`.

## 5. Probar la aplicacion en la VM

En la terminal SSH de la VM:

```bash
java -jar marketplace-cloud-1.0.0.jar
```

Si arranca bien, veras algo como:

```text
Tomcat started on port 8080
```

## 6. Abrir el puerto 8080 en Google Cloud

En tu PC, usando Google Cloud CLI:

```powershell
gcloud compute firewall-rules create allow-marketflow-8080 --allow tcp:8080 --source-ranges 0.0.0.0/0 --target-tags marketflow-server
gcloud compute instances add-tags ventas-por-cliente --tags marketflow-server --zone us-central1-b
```

Tambien puedes hacerlo desde la consola:

1. Ve a `VPC network > Firewall`.
2. Crea una regla de entrada.
3. Target tag: `marketflow-server`.
4. Puerto: `tcp:8080`.
5. Source IPv4 ranges: `0.0.0.0/0`.
6. Agrega el tag `marketflow-server` a la VM.

## 7. Entrar a la pagina

En la tabla de instancias, copia la IP externa de la VM.

Abre:

```text
http://IP_EXTERNA:8080
```

Ejemplo:

```text
http://34.123.45.67:8080
```

## 8. Dejar la app corriendo aunque cierres SSH

La forma simple:

```bash
nohup java -jar marketplace-cloud-1.0.0.jar > marketflow.log 2>&1 &
```

Ver logs:

```bash
tail -f marketflow.log
```

Detener:

```bash
pkill -f marketplace-cloud-1.0.0.jar
```

## 9. Forma mas profesional: servicio systemd

Crea carpeta:

```bash
sudo mkdir -p /opt/marketflow
sudo cp marketplace-cloud-1.0.0.jar /opt/marketflow/app.jar
```

Crea el servicio:

```bash
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

Ver logs:

```bash
journalctl -u marketflow -f
```

## 10. Si usas Supabase

En el servicio systemd puedes agregar variables:

```ini
Environment=SUPABASE_DB_URL=jdbc:postgresql://db.TU-PROYECTO.supabase.co:5432/postgres?sslmode=require
Environment=SUPABASE_DB_USER=postgres
Environment=SUPABASE_DB_PASSWORD=TU_PASSWORD
```

Despues:

```bash
sudo systemctl daemon-reload
sudo systemctl restart marketflow
```

