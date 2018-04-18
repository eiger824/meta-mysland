DESCRIPTION = "A dummy Qt5 recipe to test functionality"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

DEPENDS += "qtbase"

S = "${WORKDIR}/${PN}-${PV}"

PR = "r1"

SRC_URI = " \
    file://main.cpp \
    file://mainwindow.cpp \
    file://mainwindow.h \
    file://mainwindow.ui \
    file://qtfoo.pro \
    "

inherit qmake5

do_configure_prepend() {
    # Move everything to "S"
    mv ${WORKDIR}/*cpp ${WORKDIR}/*.h ${WORKDIR}/*.ui ${WORKDIR}/*.pro ${S}
}

do_install() {
    # Install binary
    install -d -m 0755 ${D}/${bindir}
    install -m 0755 qtfoo ${D}/${bindir}/qtfoo
}

FILES_${PN} = " \
    ${bindir}/qtfoo \
    "

