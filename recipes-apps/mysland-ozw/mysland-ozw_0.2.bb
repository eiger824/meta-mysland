DESCRIPTION = "A set of Qt5 C++ programs to interact with OpenZWave"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

DEPENDS += "qtbase"
RDEPENDS_${PN} += "qtbase apache2 php openzwave"

S = "${WORKDIR}/${PN}-${PV}"

PR = "r16"

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

    # Install systemd init script: install it as "ozwd" to keep the unix daemon naming format
    install -d -m 0755 ${D}/${systemd_system_unitdir}
    install -m 0644 ${S}/common/systemd/mysland-openzwave.service ${D}/${systemd_system_unitdir}/ozwd.service

    # Install web directory: we want rwxrwxrwx permissions since a non-root user wants to write and read files
    install -d -m 0755 ${D}/${datadir}/apache2/htdocs
    # Install web files
    install -m 0755 ${S}/web/home.html ${D}/${datadir}/apache2/htdocs/home.html
    install -m 0755 ${S}/web/action_switch_binary.php ${D}/${datadir}/apache2/htdocs/action_switch_binary.php
    install -m 0755 ${S}/web/action_switch_multilevel.php ${D}/${datadir}/apache2/htdocs/action_switch_multilevel.php

    # Install ozw logo
    install -m 0644 ${S}/server/imgs/ozwlogo.png ${D}/${datadir}/apache2/htdocs/ozwlogo.png
}

pkg_postinst_${PN}() {
    if ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}; then
        if [ -n "$D" ]; then
            OPTS="--root=$D"
        fi
        systemctl $OPTS enable ozwd.service
    fi
    # Change permissions to web directory
    chmod 0777 ${datadir}/apache2/htdocs
}

# Create different packages with server or clients
PACKAGES += "${PN}-srv ${PN}-cli ${PN}-web"

# Assign files to these packages
FILES_COMMON = " \
    ${sysconfdir}/dbus-1/system.d/mysland-openzwave.conf \
    ${systemd_system_unitdir}/ozwd.service \
    "

FILES_${PN}-web = " \
    ${datadir}/apache2/htdocs/* \
    "

FILES_${PN}-srv = " \
    ${bindir}/ozw-daemon \
    "

FILES_${PN}-cli = " \
    ${bindir}/ozw-client \
    ${bindir}/ozw-proxy-client \
    "

# The general package contains configuration files only
FILES_${PN} = " \
    ${FILES_COMMON} \
    "

