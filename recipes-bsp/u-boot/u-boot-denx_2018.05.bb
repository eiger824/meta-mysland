SUMMARY = "Das U-boot - Universal Boot Loader for embedded devices - Mainline Denx U-Boot"

HOMEPAGE = "http://www.denx.de/wiki/U-Boot/WebHome"
SECTION = "bootloaders"

PROVIDES = "virtual/bootloader"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=a2c678cfd4a4d97135585cad908541c6"

require recipes-bsp/u-boot/u-boot.inc

DEPENDS += "bc-native dtc-native"

S = "${WORKDIR}/git"

SRC_URI = "git://git.denx.de/u-boot.git;protocol=https;branch=master;destsuffix=git"
# Using v2018.05
SRCREV = "ec5c4a8fd64a178a4d159917cda0aa176e5a9be5"

UBOOT_SUFFIX = "img"
SPL_BINARY = "SPL"
