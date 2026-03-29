#!/bin/bash

LOG_FILE="logs/application.log"

echo "========================================"
echo "Affichage des logs de l'application"
echo "========================================"
echo ""

if [ ! -f "$LOG_FILE" ]; then
    echo "Aucun fichier de logs trouve: $LOG_FILE"
    echo ""
    echo "Contenu du dossier logs:"
    ls -la logs/ 2>/dev/null || echo "  (dossier logs vide ou inexistant)"
    exit 1
fi

echo "Fichier: $LOG_FILE"
echo "Taille: $(du -h "$LOG_FILE" | cut -f1)"
echo ""
echo "Dernieres 30 lignes:"
echo "----------------------------------------"
tail -30 "$LOG_FILE"
echo "----------------------------------------"
echo ""
echo "Pour suivre les logs en temps reel: tail -f $LOG_FILE"
