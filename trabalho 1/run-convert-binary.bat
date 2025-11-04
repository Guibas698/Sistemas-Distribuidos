@echo off
cd /d "%~dp0"
rem Compilar classes necessárias
javac -cp "gson-2.13.2.jar;..\bin" -d ..\bin PojoEscolhidoInputStream.java Suplemento.java ReadBinaryToText.java
if errorlevel 1 pause & exit /b 1
java -cp "..\bin;gson-2.13.2.jar" ReadBinaryToText
pause
