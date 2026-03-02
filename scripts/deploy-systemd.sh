#!/usr/bin/env bash
set -euo pipefail

APP_NAME="${APP_NAME:-team-service}"
APP_DIR="${APP_DIR:-/opt/team-service}"
APP_PORT="${APP_PORT:-8080}"
JAR_SOURCE="${JAR_SOURCE:-$APP_DIR/team-service.jar}"
SERVICE_FILE="/etc/systemd/system/${APP_NAME}.service"

if command -v apt-get >/dev/null 2>&1; then
  sudo apt-get update -y
  sudo apt-get install -y openjdk-25-jre-headless
elif command -v dnf >/dev/null 2>&1; then
  sudo dnf install -y java-25-openjdk
elif command -v yum >/dev/null 2>&1; then
  sudo yum install -y java-25-openjdk
else
  echo "Unsupported package manager. Install Java 25 manually." >&2
  exit 1
fi

sudo mkdir -p "$APP_DIR"
sudo cp "$JAR_SOURCE" "$APP_DIR/${APP_NAME}.jar"
sudo chown -R "$USER":"$USER" "$APP_DIR"

cat <<SERVICE | sudo tee "$SERVICE_FILE" >/dev/null
[Unit]
Description=Spring Boot Team Service
After=network.target

[Service]
Type=simple
User=$USER
WorkingDirectory=$APP_DIR
Environment=SERVER_PORT=$APP_PORT
ExecStart=/usr/bin/java -jar $APP_DIR/${APP_NAME}.jar
SuccessExitStatus=143
Restart=always
RestartSec=5

[Install]
WantedBy=multi-user.target
SERVICE

sudo systemctl daemon-reload
sudo systemctl enable "$APP_NAME"
sudo systemctl restart "$APP_NAME"
sudo systemctl --no-pager --full status "$APP_NAME" | head -n 40
