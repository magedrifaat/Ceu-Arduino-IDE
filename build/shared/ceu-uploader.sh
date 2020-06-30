cd `dirname $0`
cd $PWD/../repos/ceu-arduino/

CEU_SRC=$1

ARD_BOARD=$(`dirname $0`/arduino --get-pref board | tail -1)

ARD_PORT=$(`dirname $0`/arduino --get-pref serial.port | tail -1)

make c CEU_SRC=$CEU_SRC ARD_BOARD=$ARD_BOARD ARD_PORT=$ARD_PORT
