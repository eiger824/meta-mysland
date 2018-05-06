DESCRIPTION = "AR1xxx UART Open Source Linux Driver"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

PR = "r3"

DEPENDS += "linux-libc-headers"
RDEPENDS_${PN} += "kernel-image"

# do_compile[deptask] = "do_populate_sysroot"

inherit module

do_rm_work[noexec] = "1"

DOWNLOAD_NAME = "AR1010-AR1011-AR1100-LINUX-UART-V102"

SRC_URI = " \
    http://ww1.microchip.com/downloads/en/DeviceDoc/${DOWNLOAD_NAME}.tar.gz \
    file://0001-Change-for-Yocto-builds.patch \
    file://0002-Remove-unnecessary-steps-for-Yocto-builds.patch \
    file://0003-Uncomment-some-printouts.patch \
    "

SRC_URI[m5dsum] = "727c3e3ac5bc0f016eae87ec21f61972"
SRC_URI[sha256sum] = "086ac9a51d3e559965ee74932c359ff5a3e17cf4880ea8204c9c58c8ddfab169"

S = "${WORKDIR}/${DOWNLOAD_NAME}"

TARGET_CFLAGS += "--sysroot=${STAGING_DIR_TARGET} -mfloat-abi=hard"

INSANE_SKIP_${PN}-tools = "ldflags"
INSANE_SKIP_${PN}       = "ldflags"

do_install_append() {
    # Install userspace tools
    install -d -m 0755 ${D}/${bindir}/microchip
    install -m 0755 inputactivate ${D}/${bindir}/microchip/inputactivate
    install -m 0755 inputverify ${D}/${bindir}/microchip/inputverify
    # Install setup script
    install -m 0755 install.sh ${D}/${bindir}/microchip/setup-microchip.sh
    # Install U-dev rule
    install -d -m 0755 ${D}/${sysconfdir}/udev/rules.d
    install -m 0644 10-ar1xxxuart.rules ${D}/${sysconfdir}/udev/rules.d/10-ar1xxxuart.rules
}

PACKAGES += "${PN}-tools ${PN}-module"

FILES_${PN}-tools += " \
    ${bindir}/microchip/inputactivate \
    ${bindir}/microchip/inputverify \
    ${bindir}/microchip/setup-microchip.sh \
    ${sysconfdir}/udev/rules.d/10-ar1xxxuart.rules \
    "

FILES_${PN}-module += " \
    ${libdir}/modules/*/ \
    "

# The global package contains everything
FILES_${PN} += " \
    ${bindir}/microchip/inputactivate \
    ${bindir}/microchip/inputverify \
    ${bindir}/microchip/setup-microchip.sh \
    ${sysconfdir}/udev/rules.d/10-ar1xxxuart.rules \
    ${libdir}/modules/*/ \
    "

