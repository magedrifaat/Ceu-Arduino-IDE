@echo off

set CEU_SRC=%1

rem This gets the board name by running arduino in get pref mode
for /f "tokens=*" %%f in ('call %~dp0/arduino_debug.exe --get-pref board') do set board=%%f

rem this gets the port
for /f "tokens=*" %%f in ('call %~dp0/arduino_debug.exe --get-pref serial.port') do set port=%%f

cd /d %~dp0/../repos/ceu-arduino

..\..\mingw\bin\make c CEU_SRC=%CEU_SRC% ARD_BOARD=%board% ARD_PORT=%port%

exit ERRORLEVEL
