cd `dirname $0`
cd $PWD/../repos/ceu-arduino/

CEU_SRC=$1

ARD_BOARD=$(`dirname $0`/arduino --get-pref board | tail -1)
ARD_ARCH=$(`dirname $0`/arduino --get-pref target_platform | tail -1)

make ceu CEU_SRC=$CEU_SRC ARD_BOARD=$ARD_BOARD ARD_ARCH=$ARD_ARCH