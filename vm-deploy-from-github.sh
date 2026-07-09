#!/usr/bin/env bash
set -e

if [ -z "$1" ]; then
  echo "Uso: ./vm-deploy-from-github.sh https://github.com/FailPatch/marketflow.git"
  exit 1
fi

REPO_URL="$1"
APP_DIR="$HOME/marketflow"

sudo apt update
sudo apt install -y git openjdk-17-jdk maven

if [ -d "$APP_DIR/.git" ]; then
  cd "$APP_DIR"
  git pull
else
  git clone "$REPO_URL" "$APP_DIR"
  cd "$APP_DIR"
fi

if [ -d "marketplace-cloud" ]; then
  cd marketplace-cloud
fi

mvn clean package

sudo mkdir -p /opt/marketflow
sudo cp target/marketplace-cloud-1.0.0.jar /opt/marketflow/app.jar

sudo tee /etc/systemd/system/marketflow.service > /dev/null <<'SERVICE'
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
SERVICE

sudo systemctl daemon-reload
sudo systemctl enable marketflow
sudo systemctl restart marketflow
sudo systemctl status marketflow --no-pager
