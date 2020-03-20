@echo off

set CEU_SRC=%1
set UPLOAD=%2
set ARD_BOARD=%3
set ARD_ARCH=%4
set ARD_PORT=%5

cd /d %~dp0/../repos/ceu-arduino

IF "%UPLOAD%"=="true" (
    ..\..\mingw\bin\make c CEU_SRC=%CEU_SRC% ARD_BOARD=%ARD_BOARD% ARD_PORT=%ARD_PORT%
) ELSE (
    ..\..\mingw\bin\make ceu CEU_SRC=%CEU_SRC% ARD_BOARD=%ARD_BOARD% ARD_PORT=%ARD_PORT%
)

exit ERRORLEVEL
