SUMMARY = "Das U-boot - Universal Boot Loader for embedded devices - Mainline Denx U-Boot"

SECTION = "bootloaders"

# PROVIDES = "virtual/bootloader"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=a2c678cfd4a4d97135585cad908541c6"

require recipes-bsp/u-boot/u-boot.inc

DEPENDS += "bc-native dtc-native"

S = "${WORKDIR}/git"

UBOOT_BRANCH = "2018.03+fslc"

SRC_URI = "git://github.com/Freescale/u-boot-fslc.git;protocol=https;branch=${UBOOT_BRANCH};destsuffix=git"
# Using v2018.03
SRCREV = "7155b10c62fcf06b6975533ae50b83bf594de95b"

UBOOT_SUFFIX = "img"
SPL_BINARY = "SPL"
