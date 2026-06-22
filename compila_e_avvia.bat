@echo off
setlocal EnableDelayedExpansion

set "SCRIPT_DIR=%~dp0"
set "SRC_DIR=%SCRIPT_DIR%src\dashboard"
set "BIN_DIR=%SCRIPT_DIR%bin"
set "LIB_DIR=%SCRIPT_DIR%lib"
set "FLATLAF_JAR=%LIB_DIR%\flatlaf.jar"
set "FLATLAF_EXTRAS_JAR=%LIB_DIR%\flatlaf-extras.jar"
set "JSVG_JAR=%LIB_DIR%\jsvg.jar"
set "JAR_FILE=%SCRIPT_DIR%CalenDaros.jar"
set "SOURCES_FILE=%BIN_DIR%\sources.txt"
set "MANIFEST_FILE=%BIN_DIR%\MANIFEST.MF"

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

if not exist "%LIB_DIR%" mkdir "%LIB_DIR%"
if not exist "%FLATLAF_JAR%" (
    echo [INFO] Download FlatLaf...
    powershell -NoProfile -ExecutionPolicy Bypass -Command "$ErrorActionPreference='Stop'; $metadata=[xml](Invoke-WebRequest -UseBasicParsing 'https://repo1.maven.org/maven2/com/formdev/flatlaf/maven-metadata.xml').Content; $version=$metadata.metadata.versioning.release; if(-not $version){$version=$metadata.metadata.versioning.latest}; $url='https://repo1.maven.org/maven2/com/formdev/flatlaf/'+$version+'/flatlaf-'+$version+'.jar'; Invoke-WebRequest -UseBasicParsing $url -OutFile '%FLATLAF_JAR%'; Write-Host ('[INFO] FlatLaf ' + $version + ' scaricato.')"
    if errorlevel 1 (
        echo [ATTENZIONE] Download FlatLaf non riuscito. L'app usera il Look ^& Feel di sistema.
    )
)
if exist "%FLATLAF_JAR%" if not exist "%FLATLAF_EXTRAS_JAR%" (
    echo [INFO] Download FlatLaf Extras...
    powershell -NoProfile -ExecutionPolicy Bypass -Command "$ErrorActionPreference='Stop'; $metadata=[xml](Invoke-WebRequest -UseBasicParsing 'https://repo1.maven.org/maven2/com/formdev/flatlaf-extras/maven-metadata.xml').Content; $version=$metadata.metadata.versioning.release; if(-not $version){$version=$metadata.metadata.versioning.latest}; $url='https://repo1.maven.org/maven2/com/formdev/flatlaf-extras/'+$version+'/flatlaf-extras-'+$version+'.jar'; Invoke-WebRequest -UseBasicParsing $url -OutFile '%FLATLAF_EXTRAS_JAR%'; Write-Host ('[INFO] FlatLaf Extras ' + $version + ' scaricato.')"
    if errorlevel 1 (
        echo [ATTENZIONE] Download FlatLaf Extras non riuscito. L'app usera le icone interne.
    )
)
if exist "%FLATLAF_EXTRAS_JAR%" if not exist "%JSVG_JAR%" (
    echo [INFO] Download JSVG...
    powershell -NoProfile -ExecutionPolicy Bypass -Command "$ErrorActionPreference='Stop'; $metadata=[xml](Invoke-WebRequest -UseBasicParsing 'https://repo1.maven.org/maven2/com/github/weisj/jsvg/maven-metadata.xml').Content; $version=$metadata.metadata.versioning.release; if(-not $version){$version=$metadata.metadata.versioning.latest}; $url='https://repo1.maven.org/maven2/com/github/weisj/jsvg/'+$version+'/jsvg-'+$version+'.jar'; Invoke-WebRequest -UseBasicParsing $url -OutFile '%JSVG_JAR%'; Write-Host ('[INFO] JSVG ' + $version + ' scaricato.')"
    if errorlevel 1 (
        echo [ATTENZIONE] Download JSVG non riuscito. L'app usera le icone interne.
    )
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

echo [INFO] Inclusione librerie nel JAR...
pushd "%BIN_DIR%"
if exist "%FLATLAF_JAR%" jar xf "%FLATLAF_JAR%"
if exist "%FLATLAF_EXTRAS_JAR%" jar xf "%FLATLAF_EXTRAS_JAR%"
if exist "%JSVG_JAR%" jar xf "%JSVG_JAR%"
if exist META-INF rmdir /s /q META-INF
popd

echo [INFO] Creazione JAR eseguibile...
(
    echo Manifest-Version: 1.0
    echo Main-Class: dashboard.Calendario
    echo.
) > "%MANIFEST_FILE%"

jar cfm "%JAR_FILE%" "%MANIFEST_FILE%" -C "%BIN_DIR%" .
if errorlevel 1 (
    echo [ERRORE] Creazione JAR non riuscita.
    pause
    exit /b 1
)
if exist "%MANIFEST_FILE%" del /q "%MANIFEST_FILE%"

echo [INFO] Avvio dell'applicazione...
if exist "%FLATLAF_JAR%" (
    if exist "%FLATLAF_EXTRAS_JAR%" (
        if exist "%JSVG_JAR%" (
            java "-Dfile.encoding=UTF-8" -cp "%JAR_FILE%;%FLATLAF_JAR%;%FLATLAF_EXTRAS_JAR%;%JSVG_JAR%" dashboard.Calendario --always-on-top --top-left --dark %*
        ) else (
            java "-Dfile.encoding=UTF-8" -cp "%JAR_FILE%;%FLATLAF_JAR%;%FLATLAF_EXTRAS_JAR%" dashboard.Calendario --always-on-top --top-left --dark %*
        )
    ) else (
        java "-Dfile.encoding=UTF-8" -cp "%JAR_FILE%;%FLATLAF_JAR%" dashboard.Calendario --always-on-top --top-left --dark %*
    )
) else (
    java "-Dfile.encoding=UTF-8" -jar "%JAR_FILE%" --always-on-top --top-left --dark %*
)

pause
