BUILDCFLAGS=-Wall -Wextra -Wpedantic --std=c11

ping-server: server.c
	${CC} ${BUILDCFLAGS} $< -o $@

clean:
	rm -f ping-server *~
