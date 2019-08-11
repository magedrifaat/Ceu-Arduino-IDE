@echo off

set CEU_SRC=%1
set UPLOAD=%2
set ARD_BOARD=%3
set ARD_ARCH=%4
set ARD_PORT=%5


cd /d D:\Programs\ceu-maker-windows\repos\ceu-arduino

if "%UPLOAD%" NEQ "true" goto :2
..\..\mingw\bin\make c CEU_SRC=%CEU_SRC% ARD_BOARD=%ARD_BOARD% ARD_PORT=%ARD_PORT%
goto :3

:2
..\..\mingw\bin\make ceu CEU_SRC=%CEU_SRC% ARD_BOARD=%ARD_BOARD% ARD_PORT=%ARD_PORT%

:3
@echo off

exit ERRORLEVEL