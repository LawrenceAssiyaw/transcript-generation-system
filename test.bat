@echo off
setlocal
cd /d "%~dp0"
if not exist out mkdir out
javac -d out src\com\lawrence\transcript\*.java src\com\lawrence\transcript\exception\*.java src\com\lawrence\transcript\formatter\*.java src\com\lawrence\transcript\gui\*.java src\com\lawrence\transcript\model\*.java src\com\lawrence\transcript\service\*.java
if errorlevel 1 (
  echo Build failed.
  pause
  exit /b 1
)
java -cp out com.lawrence.transcript.TestHarness
pause
