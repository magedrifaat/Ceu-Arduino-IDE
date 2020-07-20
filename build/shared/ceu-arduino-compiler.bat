@echo off

set CEU_SRC=%1


rem This gets the board name by running arduino in get pref mode
for /f "tokens=*" %%f in ('call %~dp0/arduino_debug.exe --get-pref board') do set board=%%f

cd /d %~dp0/../repos/ceu-arduino

..\..\mingw\bin\make ceu CEU_SRC=%CEU_SRC% ARD_BOARD=board

exit ERRORLEVEL
