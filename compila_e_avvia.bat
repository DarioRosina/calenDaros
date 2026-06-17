@echo off
setlocal EnableDelayedExpansion

set "SCRIPT_DIR=%~dp0"
set "SRC_DIR=%SCRIPT_DIR%src\dashboard"
set "BIN_DIR=%SCRIPT_DIR%bin"
set "JAR_FILE=%SCRIPT_DIR%CalenDaros.jar"
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

where jar >nul 2>nul
if errorlevel 1 (
    echo [ERRORE] jar non trovato. Installa un JDK e aggiungi la cartella bin al PATH.
    pause
    exit /b 1
)

echo [INFO] Pulizia cartella bin...
if exist "%BIN_DIR%" rmdir /s /q "%BIN_DIR%"
mkdir "%BIN_DIR%"
if exist "%JAR_FILE%" del /q "%JAR_FILE%"

echo [INFO] Generazione lista file sorgenti...
(
    for /R "%SRC_DIR%" %%F in (*.java) do (
        set "SOURCE=%%F"
        set "SOURCE=!SOURCE:\=/!"
        echo "!SOURCE!"
    )
) > "%SOURCES_FILE%"

echo [INFO] Compilazione in corso...
javac -encoding UTF-8 -d "%BIN_DIR%" @"%SOURCES_FILE%"
if errorlevel 1 (
    echo [ERRORE] Compilazione non riuscita. Controlla i messaggi sopra.
    pause
    exit /b 1
)

echo [INFO] Copia delle risorse statiche...
if exist "%SRC_DIR%\img" xcopy /E /I /Y "%SRC_DIR%\img" "%BIN_DIR%\dashboard\img" >nul
if exist "%SRC_DIR%\i18n" xcopy /E /I /Y "%SRC_DIR%\i18n" "%BIN_DIR%\dashboard\i18n" >nul
if exist "%SOURCES_FILE%" del /q "%SOURCES_FILE%"

echo [INFO] Creazione JAR eseguibile...
jar cfe "%JAR_FILE%" dashboard.Calendario -C "%BIN_DIR%" .
if errorlevel 1 (
    echo [ERRORE] Creazione JAR non riuscita.
    pause
    exit /b 1
)

echo [INFO] Avvio dell'applicazione...
java "-Dfile.encoding=UTF-8" -jar "%JAR_FILE%" --always-on-top

pause
