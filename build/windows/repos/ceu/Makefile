include Makefile.conf

CEU_EXE   ?= ceu
CEU_ARGS_ = --ceu-features-trace=true --ceu-err-unused=pass $(CEU_ARGS)
CC_ARGS_  = -lpthread -llua5.3 $(CC_ARGS)

build:
	$(CEU_EXE) --pre --pre-input=$(CEU_SRC) --pre-args=-I./include                \
		--ceu --ceu-features-async=true $(CEU_ARGS_) --ceu-features-os=true \
			  --ceu-features-lua=true --ceu-features-thread=true \
			  --ceu-features-dynamic=true \
		--env --env-types=env/types.h --env-threads=env/threads.h --env-main=env/main.c --env-output=tmp/_ceu_app.c \
		--cc --cc-args="$(CC_ARGS_)"                        \
			 --cc-output=tmp/tmp.exe

run:
	@(cd tmp && tmp.exe)

.PHONY: build
