DESCRIPTION = "A simple TCP server that responds to PING commands from a master node"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

S = "${WORKDIR}"

PR = "r0"

SRC_URI = " \
	file://server.c \
	file://Makefile \
	"

TARGET_CC_ARCH += "${LDFLAGS}" 

do_compile() {
    ${MAKE} ping-server 
}

do_install() {
	# binary
	install -d -m 0755 ${D}/${bindir}
    install -m 0755 ping-server ${D}/${bindir}/ping-server
}
FILES_${PN} += " \
        ping-server \
		"


