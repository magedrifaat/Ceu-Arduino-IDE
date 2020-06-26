cd `dirname $0`
cd $PWD/../repos/ceu-arduino/

CEU_SRC=$1

ARD_BOARD=$(%~dp0/arduino --get-pref board | tail -1)

ARD_PORT=$(%~dp0/arduino --get-pref serial.port | tail -1)

make ceu CEU_SRC=$CEU_SRC ARD_BOARD_=$ARD_BOARD ARD_PORT_=$ARD_PORT
