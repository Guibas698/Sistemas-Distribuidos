@echo off
cd /d "%~dp0"
javac -cp "..\bin;gson-2.13.2.jar" -d ..\bin TestWriteToText.java
if errorlevel 1 pause & exit /b 1
java -cp "..\bin;gson-2.13.2.jar" TestWriteToText
pause
