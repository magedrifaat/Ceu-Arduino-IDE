cd `dirname $0`
cd $PWD/../repos/ceu-arduino/

CEU_SRC=$1
UPLOAD=$2
ARD_BOARD=$3
ARD_ARCH=$4
ARD_PORT=$5

if [ "$UPLOAD" = "true" ]
then
    make c CEU_SRC=$CEU_SRC ARD_ARCH_=$ARD_ARCH ARD_BOARD_=$ARD_BOARD ARD_PORT_=$ARD_PORT
else
    make ceu CEU_SRC=$CEU_SRC ARD_ARCH_=$ARD_ARCH ARD_BOARD_=$ARD_BOARD ARD_PORT_=$ARD_PORT
fi
