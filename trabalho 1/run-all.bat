@echo off
REM Script para iniciar servidor e ambos clientes em janelas separadas
REM Executa: run-server, espera 3s, run-client, espera 1s, run-admin

necho Iniciando ServidorQ5...
call "%~dp0run-server.bat"

necho Aguardando 3 segundos para o servidor subir...
timeout /t 3 /nobreak >nul

necho Iniciando ClienteQ5...
call "%~dp0run-client.bat"

necho Aguardando 1 segundo...
timeout /t 1 /nobreak >nul

necho Iniciando ClienteAdminQ5...
call "%~dp0run-admin.bat"

necho Todos os processos foram iniciados em janelas separadas.
exit /b 0
