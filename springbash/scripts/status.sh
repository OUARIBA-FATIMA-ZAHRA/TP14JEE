#!/bin/bash

PID_FILE="logs/app.pid"
PORT=8081

echo "========================================"
echo "Statut de l'application"
echo "========================================"
echo ""

echo "[1] Verification du processus:"
if [ -f "$PID_FILE" ]; then
    PID=$(cat "$PID_FILE")
    if ps -p "$PID" > /dev/null 2>&1; then
        echo "    [+] Processus actif (PID: $PID)"
    else
        echo "    [-] Processus inactif"
    fi
else
    echo "    [-] Aucun fichier PID"
fi

echo ""
echo "[2] Verification du port $PORT:"
if lsof -ti :$PORT > /dev/null 2>&1; then
    echo "    [+] Port $PORT occupe"
else
    echo "    [-] Port $PORT libre"
fi

echo ""
echo "[3] Verification HTTP:"
if curl -s --max-time 3 http://localhost:$PORT/api/health > /dev/null 2>&1; then
    echo "    [+] API repond"
else
    echo "    [-] API ne repond pas"
fi

echo ""
echo "========================================"