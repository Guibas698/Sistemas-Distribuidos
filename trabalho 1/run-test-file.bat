@echo off
REM Executa TestWriteToFile (escreve e lê usando FileInputStream)
pushd "%~dp0"
java -cp "%~dp0..\bin;%~dp0gson-2.13.2.jar" TestWriteToFile
pause
popd
