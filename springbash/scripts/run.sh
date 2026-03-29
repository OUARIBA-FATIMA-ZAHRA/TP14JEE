#!/bin/bash

APP_NAME="springbash"
LOG_DIR="logs"
LOG_FILE="$LOG_DIR/application.log"
PID_FILE="$LOG_DIR/app.pid"
PORT=8081

echo "========================================"
echo "Demarrage de $APP_NAME sur le port $PORT"
echo "========================================"

mkdir -p "$LOG_DIR"

if [ -f "$PID_FILE" ]; then
    PID=$(cat "$PID_FILE")
    if ps -p "$PID" > /dev/null 2>&1; then
        echo "ERREUR: L'application est deja en cours (PID: $PID)"
        echo "Utilisez stop.sh pour l'arreter"
        exit 1
    else
        rm -f "$PID_FILE"
    fi
fi

echo "Demarrage de l'application..."
nohup mvn spring-boot:run > "$LOG_FILE" 2>&1 &
APP_PID=$!
echo $APP_PID > "$PID_FILE"

sleep 3

echo ""
echo "Application demarree sur le port $PORT"
echo "URL: http://localhost:$PORT"
echo "Logs: $LOG_FILE"
echo ""
echo "Pour arreter: ./scripts/stop.sh"
echo "Pour voir les logs: ./scripts/logs.sh"