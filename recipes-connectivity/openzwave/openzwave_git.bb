SUMMARY = "A C++ library to control Z-Wave Networks via a USB Z-Wave Controller."
HOMEPAGE = "http://www.openzwave.com/"

LICENSE = "LGPLv3"
LIC_FILES_CHKSUM = "file://license/lgpl.txt;md5=7be289db0a5cd2c8acf72a8cbd0c15df"

inherit pkgconfig

DEPENDS = "udev coreutils-native libxml2-native"

SRCREV = "3b96eca384e0bc71846b3706ab89bb2107605024"
# counts git commits since 1.5 tag
PV = "1.4.3251"

SRC_URI = "git://github.com/OpenZWave/open-zwave.git;protocol=https \
          "

S = "${WORKDIR}/git"

EXTRA_OEMAKE = "PREFIX=${prefix} \
                BITBAKE_ENV=1 \
                MACHINE=${TARGET_ARCH} \
                DESTDIR=${D} \
                SYSCONFDIR=${sysconfdir}/openzwave \
                sysconfdir=${sysconfdir}/openzwave \
                instlibdir=${libdir} \
                pkgconfigdir=${libdir}/pkgconfig "

TARGET_CPPFLAGS += "-Wno-format-truncation"

do_compile() {
	sed -i -e 's:$(PREFIX)/etc:${sysconfdir}:g' ${S}/cpp/build/Makefile
	sed -i -e 's,pkgconfigdir ?,pkgconfigdir :,g' ${S}/cpp/build/support.mk
	oe_runmake
}

do_install() {
	oe_runmake install
}

FILES_${PN}-dev += "${bindir}/ozw_config"
