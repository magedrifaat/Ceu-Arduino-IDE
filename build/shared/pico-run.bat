@echo off

set CEU_SRC=%1

cd /d %~dp0/../repos/pico-ceu

..\..\mingw\bin\make run CEU_SRC=%CEU_SRC%

exit ERRORLEVEL
