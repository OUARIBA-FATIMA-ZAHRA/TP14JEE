@echo off
setlocal enabledelayedexpansion

set APP_NAME=springbash
set VERSION=1.0.0
set JAR_FILE=target\%APP_NAME%-%VERSION%.jar
set DEPLOY_DIR=deployment
set BACKUP_DIR=backups
set LOG_DIR=logs

echo ========================================
echo Deploiement de l'application %APP_NAME%
echo ========================================

REM Creer les repertoires necessaires
if not exist %DEPLOY_DIR% mkdir %DEPLOY_DIR%
if not exist %BACKUP_DIR% mkdir %BACKUP_DIR%
if not exist %LOG_DIR% mkdir %LOG_DIR%

echo.
echo [1/5] Nettoyage du projet...
call mvn clean > %LOG_DIR%\deploy.log 2>&1
if !errorlevel! neq 0 (
    echo ERREUR: Le nettoyage a echoue
    exit /b 1
)
echo Nettoyage termine avec succes

echo.
echo [2/5] Compilation du projet...
call mvn package -DskipTests >> %LOG_DIR%\deploy.log 2>&1
if !errorlevel! neq 0 (
    echo ERREUR: La compilation a echoue
    type %LOG_DIR%\deploy.log
    exit /b 1
)
echo Compilation terminee avec succes

echo.
echo [3/5] Verification du fichier JAR...
if not exist %JAR_FILE% (
    echo ERREUR: Le fichier JAR %JAR_FILE% n'a pas ete trouve
    exit /b 1
)
echo JAR cree: %JAR_FILE%

REM Sauvegarder l'ancienne version
if exist %DEPLOY_DIR%\%APP_NAME%.jar (
    set BACKUP_NAME=%APP_NAME%_%date:~6,4%%date:~3,2%%date:~0,2%_%time:~0,2%%time:~3,2%%time:~6,2%.jar
    set BACKUP_NAME=!BACKUP_NAME: =0!
    copy %DEPLOY_DIR%\%APP_NAME%.jar %BACKUP_DIR%\!BACKUP_NAME! >nul
    echo Sauvegarde creee: !BACKUP_NAME!
)

echo.
echo [4/5] Deploiement du nouveau JAR...
copy %JAR_FILE% %DEPLOY_DIR%\%APP_NAME%.jar >nul
echo Nouveau JAR deploye dans %DEPLOY_DIR%\%APP_NAME%.jar

echo.
echo [5/5] Redemarrage de l'application...
call scripts\stop.bat >nul 2>&1
timeout /t 3 /nobreak >nul
call scripts\run.bat

timeout /t 5 /nobreak >nul

echo.
echo Verification du deploiement...
call scripts\healthcheck.bat
if !errorlevel! equ 0 (
    echo.
    echo ========================================
    echo DEPLOIEMENT REUSSI!
    echo ========================================
    echo Version: %VERSION%
    echo URL: http://localhost:8081
    echo ========================================
) else (
    echo.
    echo ========================================
    echo ERREUR: Le deploiement a echoue
    echo ========================================
    echo L'application ne repond pas correctement
    echo Consultez les logs: %LOG_DIR%\application.log
    exit /b 1
)

exit /b 0