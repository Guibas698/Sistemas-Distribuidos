@echo off
cd /d "%~dp0"
javac -cp "gson-2.13.2.jar;..\bin" -d ..\bin WriteReadableAbsolute.java Suplemento.java
if errorlevel 1 pause & exit /b 1
java -cp "..\bin;gson-2.13.2.jar" WriteReadableAbsolute
pause
