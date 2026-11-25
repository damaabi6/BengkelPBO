@echo off
cd /d "%~dp0"
java -cp "target\bengkelpbo-1.0-SNAPSHOT.jar;target\dependency\*" com.bengkel.Main
if errorlevel 1 pause