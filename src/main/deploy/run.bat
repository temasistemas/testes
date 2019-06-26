@echo off
setlocal ENABLEDELAYEDEXPANSION
REM Arquivo para execucao do aplicativo 'testes'
REM Author: Laerson Vieira

SET STARTCMD=start
SET JAVACMD=javaw

if not "x%JAVA_HOME%" == "x" (
	echo Alterando o programa para utilizar a variavel JAVA_HOME : '%JAVA_HOME%\bin\java'  
	SET JAVACMD=%JAVA_HOME:"=%\bin\javaw
)

SET DEBUG_OPCIONAL=
if "%1" == "-debug" (
	SET DEBUG_OPCIONAL=-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=localhost:8787
	SET JAVACMD=%JAVACMD:javaw=java%
	SET STARTCMD=call
)

echo.
echo Linha de execucao: %STARTCMD% %JAVACMD% %DEBUG_OPCIONAL%-Dlog4j.configurationFile=log4j2.xml -jar testes-jfx.jar  
echo.

%STARTCMD% %JAVACMD% %DEBUG_OPCIONAL%-Dlog4j.configurationFile=log4j2.xml -jar testes-jfx.jar

echo.
echo Arquivo finalizado