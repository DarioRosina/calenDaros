@echo off
setlocal

set "SCRIPT_DIR=%~dp0"
set "SRC_DIR=%SCRIPT_DIR%."
set "BIN_DIR=%SCRIPT_DIR%..\bin"
set "SOURCES_FILE=%BIN_DIR%\sources.txt"

cd /d "%SRC_DIR%"

where javac >nul 2>nul
if errorlevel 1 (
    echo [ERRORE] javac non trovato. Installa un JDK e aggiungi la cartella bin al PATH.
    pause
    exit /b 1
)

where java >nul 2>nul
if errorlevel 1 (
    echo [ERRORE] java non trovato. Installa Java e aggiungi la cartella bin al PATH.
    pause
    exit /b 1
)

echo [INFO] Pulizia cartella bin...
if exist "%BIN_DIR%" rmdir /s /q "%BIN_DIR%"
mkdir "%BIN_DIR%"

echo [INFO] Generazione lista file sorgenti...
(
    for /R "%SRC_DIR%" %%F in (*.java) do echo "%%F"
) > "%SOURCES_FILE%"

echo [INFO] Compilazione in corso...
javac -encoding UTF-8 -d "%BIN_DIR%" @"%SOURCES_FILE%"
if errorlevel 1 (
    echo [ERRORE] Compilazione non riuscita. Controlla i messaggi sopra.
    pause
    exit /b 1
)

echo [INFO] Copia delle risorse statiche...
if exist "img" xcopy /E /I /Y "img" "%BIN_DIR%\dashboard\img" >nul
if exist "i18n" xcopy /E /I /Y "i18n" "%BIN_DIR%\dashboard\i18n" >nul
if exist "%SOURCES_FILE%" del /q "%SOURCES_FILE%"

echo [INFO] Avvio dell'applicazione...
cd /d "%BIN_DIR%"
java "-Dfile.encoding=UTF-8" dashboard.Calendario

pause
