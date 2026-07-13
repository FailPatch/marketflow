#!/usr/bin/env bash
set -e

sudo apt update
sudo apt install -y openjdk-17-jre

sudo mkdir -p /opt/marketflow
sudo cp "$HOME/marketplace-cloud-1.0.0.jar" /opt/marketflow/app.jar

sudo tee /etc/systemd/system/marketflow.service > /dev/null <<'SERVICE'
[Unit]
Description=NEXORA Marketplace
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
