@echo off
setlocal
cd /d "%~dp0"
set "OUT_DIR=%TEMP%\transcript-generation-system-out"
if exist "%OUT_DIR%" rmdir /s /q "%OUT_DIR%"
mkdir "%OUT_DIR%"
javac -d "%OUT_DIR%" src\com\lawrence\transcript\*.java src\com\lawrence\transcript\exception\*.java src\com\lawrence\transcript\formatter\*.java src\com\lawrence\transcript\gui\*.java src\com\lawrence\transcript\model\*.java src\com\lawrence\transcript\service\*.java
if errorlevel 1 (
  echo Build failed.
  pause
  exit /b 1
)
java -cp "%OUT_DIR%" com.lawrence.transcript.Main