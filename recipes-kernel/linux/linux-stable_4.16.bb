require recipes-kernel/linux/linux-yocto.inc

COMPATIBLE_MACHINE = "wandboard"

RDEPENDS_kernel-base += "kernel-devicetree"

S = "${WORKDIR}/git"

PV = "4.16.6"

KERNEL_DEVICETREE ?= " \
    imx6q-wandboard.dtb \
    imx6q-wandboard-revb1.dtb \
    imx6q-wandboard-revd1.dtb \
    imx6dl-wandboard.dtb \
    imx6dl-wandboard-revb1.dtb \
    imx6dl-wandboard-revd1.dtb \
 "

LINUX_VERSION = "4.16"
LINUX_VERSION_EXTENSION = "-stable"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-${PV}:"

SRC_URI = " \
    git://git.kernel.org/pub/scm/linux/kernel/git/stable/linux-stable.git;branch=linux-${LINUX_VERSION}.y \
    file://defconfig \
    file://0001-Turn-on-logs-in-sdio.patch \
    file://0002-Remove-misleading-goto-label.patch \
    file://0003-Leave-usdhc2-as-in-revB1-wandboards.patch \
    "
SRCREV = "22bc2b8a6aa4f3c42ff243b1528afd498c8150b1"

PACKAGES =+ "kernel-headers"
FILES_kernel-headers = "${exec_prefix}/src/linux*"

