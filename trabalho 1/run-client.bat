@echo off
REM Script para iniciar o ClienteQ5 em nova janela (mantém a janela aberta)
pushd "%~dp0"
start "ClienteQ5" cmd /k "java -cp ^"%~dp0..\bin;%~dp0gson-2.13.2.jar^" ClienteQ5"
popd
exit /b 0
