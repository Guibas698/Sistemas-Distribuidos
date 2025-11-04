@echo off
REM Script para iniciar o ServidorQ5 em nova janela (mantém a janela aberta)
REM Uso: duplo-clique em run-server.bat ou execute pelo run-all.bat

pushd "%~dp0"
start "ServidorQ5" cmd /k "java -cp ^"%~dp0..\bin;%~dp0gson-2.13.2.jar^" ServidorQ5"
popd
exit /b 0
