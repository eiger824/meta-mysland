DESCRIPTION = "A set of Qt5 C++ programs to interact with OpenZWave"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

DEPENDS += "qtbase"

S = "${WORKDIR}/${PN}-${PV}"

PR = "r1"

SRC_URI = "git://github.com/eiger824/openzwave-qt5.git;protocol=https;branch=master;destsuffix=${PN}-${PV}"

SRCREV_pn-${PN} = "master"

# Since this recipe depends on openzwave, we want openzwave to be populated in the sysroot
# before we compile this recipe.
DEPENDS += " openzwave"
do_compile[deptask] = "do_populate_sysroot"

inherit qmake5

TARGET_CXXFLAGS += "-I=/usr/include/openzwave -I${S}/common"
TARGET_LDFLAGS  += "-L=/usr/lib -lopenzwave"

do_compile_prepend() {
    # Workaround to get the right linking flags
    sed -e 's/^\(QMAKE_LIBS += \).*$/\1-L=\/usr\/lib -lopenzwave/' -i ${S}/server/server.pro
}

do_install() {
    # Install binary destination dir
    install -d -m 0755 ${D}/${bindir}
    # Install daemon
    install -m 0755 server/ozw-daemon ${D}/${bindir}/ozw-daemon
    # Install proxy client
    install -m 0755 client-proxy/ozw-proxy-client ${D}/${bindir}/ozw-proxy-client
    # Install GUI client
    install -m 0755 client/ozw-client ${D}/${bindir}/ozw-client

    # Install d-bus configuration directory
    install -d -m 0755 ${D}/${sysconfdir}/dbus-1/system.d
    # Install D-Bus configuration file
    install -m 0644 ${S}/common/dbus/mysland-openzwave.conf ${D}/${sysconfdir}/dbus-1/system.d/mysland-openzwave.conf 

    # Install web directory
    install -d -m 0755 ${D}/${datadir}/apache2/htdocs
    # Install web files
    install -m 0755 ${S}/web/home.html ${D}/${datadir}/apache2/htdocs/home.html
    install -m 0755 ${S}/web/action_switch_binary.php ${D}/${datadir}/apache2/htdocs/action_switch_binary.php
    install -m 0755 ${S}/web/action_switch_multilevel.php ${D}/${datadir}/apache2/htdocs/action_switch_multilevel.php

    # Install ozw logo
    install -m 0644 ${S}/server/imgs/ozwlogo.png ${D}/${datadir}/apache2/htdocs/ozwlogo.png
}

# Create different packages with server or clients
PACKAGES += "${PN}-srv ${PN}-cli"

# Assign files to these packages
FILES_COMMON = " \
    ${sysconfdir}/dbus-1/system.d/mysland-openzwave.conf \
    ${datadir}/apache2/htdocs/* \
    "

FILES_${PN}-srv = " \
    ${FILES_COMMON} \
    ${bindir}/ozw-daemon \
    "

FILES_${PN}-cli = " \
    ${FILES_COMMON} \
    ${bindir}/ozw-client \
    ${bindir}/ozw-proxy-client \
    "

