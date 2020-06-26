cd `dirname $0`
cd $PWD/../repos/pico-ceu/

CEU_SRC=$1

make run CEU_SRC=$CEU_SRC
