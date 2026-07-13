# Dominio para la VM de Google Cloud

Objetivo:

```text
https://tudominio.com
```

en vez de:

```text
http://IP_EXTERNA:8080
```

## 1. Comprar o tener un dominio

Necesitas un dominio propio, por ejemplo:

```text
compraexpress.pe
vendeya.com
plazaflash.com
```

Puedes comprarlo en un registrador como Namecheap, GoDaddy, Hostinger, Squarespace Domains, Porkbun, etc.

## 2. Reservar una IP externa estatica para la VM

En Google Cloud Console:

1. Ve a `VPC network`.
2. Entra a `IP addresses`.
3. Busca la IP externa que usa tu VM.
4. Cambiala de `Ephemeral` a `Static`, o reserva una nueva IP estatica.
5. Asocia esa IP a la VM `ventas-por-cliente`.

Esto es importante porque si usas una IP temporal, puede cambiar y tu dominio dejaria de funcionar.

## 3. Crear registros DNS

Tienes dos opciones.

### Opcion A: DNS donde compraste el dominio

En el panel del registrador crea:

```text
Tipo: A
Nombre: @
Valor: IP_ESTATICA_DE_TU_VM
TTL: Automatico o 3600
```

Para `www`:

```text
Tipo: CNAME
Nombre: www
Valor: tudominio.com
```

### Opcion B: Cloud DNS

En Google Cloud:

1. Ve a `Cloud DNS`.
2. Crea una zona publica.
3. Pon tu dominio.
4. Crea un registro `A` apuntando a la IP estatica de la VM.
5. Copia los nameservers de Cloud DNS.
6. En el registrador del dominio, reemplaza los nameservers por los de Google Cloud.

## 4. Abrir puertos 80 y 443

En Google Cloud Console:

1. Ve a `VPC network > Firewall`.
2. Crea una regla de entrada para HTTP:

```text
tcp:80
source: 0.0.0.0/0
target tag: marketflow-server
```

3. Crea otra para HTTPS:

```text
tcp:443
source: 0.0.0.0/0
target tag: marketflow-server
```

4. Asegurate de que la VM tenga el tag:

```text
marketflow-server
```

## 5. Instalar Nginx en la VM

Entra por SSH a la VM:

```bash
sudo apt update
sudo apt install -y nginx
```

## 6. Configurar Nginx como proxy hacia Spring Boot

Crea el archivo:

```bash
sudo nano /etc/nginx/sites-available/marketflow
```

Pega, cambiando `tudominio.com` por tu dominio real:

```nginx
server {
    listen 80;
    server_name tudominio.com www.tudominio.com;

    location / {
        proxy_pass http://127.0.0.1:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

Activa:

```bash
sudo ln -s /etc/nginx/sites-available/marketflow /etc/nginx/sites-enabled/marketflow
sudo nginx -t
sudo systemctl restart nginx
```

Ahora deberia abrir:

```text
http://tudominio.com
```

## 7. Activar HTTPS gratis con Certbot

Instala Certbot:

```bash
sudo apt install -y certbot python3-certbot-nginx
```

Genera certificado:

```bash
sudo certbot --nginx -d tudominio.com -d www.tudominio.com
```

Despues entra con:

```text
https://tudominio.com
```

## 8. Resumen visual

```text
Usuario
  ↓
tudominio.com
  ↓ DNS A record
IP estatica de la VM
  ↓ puerto 80/443
Nginx
  ↓ proxy
Spring Boot en localhost:8080
```

