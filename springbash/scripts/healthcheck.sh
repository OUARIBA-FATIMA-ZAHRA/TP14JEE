#!/bin/bash

APP_URL="http://localhost:8081/actuator/health"

echo "========================================"
echo "Health Check - Spring Bash"
echo "========================================"
echo ""

curl -s "$APP_URL" | python3 -m json.tool 2>/dev/null || curl -s "$APP_URL"

if [ $? -eq 0 ]; then
    echo ""
    echo "[SUCCES] Application operationnelle"
    exit 0
else
    echo ""
    echo "[ECHEC] Application non disponible"
    exit 1
fi