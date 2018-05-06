require recipes-kernel/linux/linux-yocto.inc

COMPATIBLE_MACHINE = "beaglebone"

RDEPENDS_kernel-base += "kernel-devicetree"

S = "${WORKDIR}/git"

PV = "4.16.7"

KERNEL_DEVICETREE = " \
    am335x-boneblack.dtb \
    am335x-boneblack-wireless.dtb \
 "

LINUX_VERSION = "4.16"
LINUX_VERSION_EXTENSION = "-stable"

SRC_URI = " \
    git://git.kernel.org/pub/scm/linux/kernel/git/stable/linux-stable.git;branch=linux-${LINUX_VERSION}.y \
    file://defconfig \
    "

SRCREV = "9dc30ff9a115559cc55673d0b1d3c576402d073e"

PACKAGES =+ "kernel-headers"
FILES_kernel-headers = "${exec_prefix}/src/linux*"

