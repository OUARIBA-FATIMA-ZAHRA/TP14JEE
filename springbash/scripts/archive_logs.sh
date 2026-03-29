#!/bin/bash

LOG_DIR="logs"
ARCHIVE_DIR="archives"
DATE=$(date +%Y%m%d_%H%M%S)
ARCHIVE_NAME="logs_archive_$DATE.tar.gz"

echo "========================================"
echo "Archivage des logs"
echo "========================================"
echo ""

mkdir -p "$ARCHIVE_DIR"

if [ -d "$LOG_DIR" ] && [ "$(ls -A $LOG_DIR 2>/dev/null)" ]; then
    echo "Creation de l'archive: $ARCHIVE_NAME"

    # Version corrigée pour Windows/Git Bash
    cd "$LOG_DIR" && tar -czf "../$ARCHIVE_DIR/$ARCHIVE_NAME" *.log 2>/dev/null && cd ..

    if [ $? -eq 0 ] && [ -f "$ARCHIVE_DIR/$ARCHIVE_NAME" ]; then
        echo "Archive creee avec succes: $ARCHIVE_DIR/$ARCHIVE_NAME"
        echo "Taille: $(du -h "$ARCHIVE_DIR/$ARCHIVE_NAME" | cut -f1)"

        read -p "Voulez-vous nettoyer les logs apres archivage? (o/N): " CLEAN
        if [ "$CLEAN" = "o" ] || [ "$CLEAN" = "O" ]; then
            > "$LOG_DIR/application.log"
            echo "Logs nettoyes"
        fi
    else
        echo "ERREUR: Echec de la creation de l'archive"
        echo "Verifiez qu'il y a des fichiers .log dans $LOG_DIR"
        ls -la "$LOG_DIR"
    fi
else
    echo "Aucun log a archiver dans $LOG_DIR"
    ls -la "$LOG_DIR"
fi

echo ""
echo "Archives disponibles:"
ls -lh "$ARCHIVE_DIR" 2>/dev/null || echo "Aucune archive"