cd `dirname $0`
cd $PWD/../repos/pico-ceu/

CEU_SRC=$1

make build CEU_SRC=$CEU_SRC
