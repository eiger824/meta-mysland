FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://systemd-additions.cfg"

SRC_URI += "\
    file://0001-Add-HW-OOB-interrupt-for-bcmdhd-driver.patch \
    "
