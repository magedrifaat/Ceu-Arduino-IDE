@echo off

set CEU_SRC=%1

cd /d %~dp0/../repos/ceu

..\..\mingw\bin\make build CEU_SRC=%CEU_SRC%

exit ERRORLEVEL
