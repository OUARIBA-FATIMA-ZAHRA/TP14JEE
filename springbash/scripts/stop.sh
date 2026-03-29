#!/bin/bash

PID_FILE="logs/app.pid"
PORT=8081

echo "========================================"
echo "Arret de l'application sur le port $PORT"
echo "========================================"

if [ -f "$PID_FILE" ]; then
    PID=$(cat "$PID_FILE")
    echo "Arret du processus $PID"
    kill -9 "$PID" 2>/dev/null
    rm -f "$PID_FILE"
    echo "Application arretee"
else
    echo "Recherche des processus Java sur le port $PORT..."
    PIDS=$(lsof -ti :$PORT 2>/dev/null)
    if [ -n "$PIDS" ]; then
        for PID in $PIDS; do
            echo "Arret du processus $PID"
            kill -9 "$PID" 2>/dev/null
        done
    fi
fi

sleep 2

echo "Verification du port $PORT..."
if lsof -ti :$PORT > /dev/null 2>&1; then
    echo "ATTENTION: Le port $PORT est toujours utilise"
else
    echo "Port $PORT libere avec succes"
fi

echo "Arret termine"