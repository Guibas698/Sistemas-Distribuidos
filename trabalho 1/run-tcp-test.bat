@echo off
cd /d "%~dp0"
REM Compila as classes necessárias
javac -cp "..\bin;gson-2.13.2.jar" -d ..\bin SuplementoTcpServer.java SuplementoTcpClient.java PojoEscolhidoOutputStream.java PojoEscolhidoInputStream.java Suplemento.java
if errorlevel 1 pause & exit /b 1
REM Inicia o servidor em nova janela
start "SuplServer" cmd /k java -cp "..\bin;gson-2.13.2.jar" SuplementoTcpServer
REM Espera 1s para o servidor subir
ping -n 2 127.0.0.1 >nul
REM Executa o cliente que envia os dados
java -cp "..\bin;gson-2.13.2.jar" SuplementoTcpClient
pause
