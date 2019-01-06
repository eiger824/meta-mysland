FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

PACKAGECONFIG += "networkd resolved"

SRC_URI += " \
    file://wired.network \
    file://wireless.network \
    "

do_install_append() {
    install -d -m0755 ${D}${sysconfdir}/systemd/network
    install -m0644 ${WORKDIR}/wired.network ${D}${sysconfdir}/systemd/network
    install -m0644 ${WORKDIR}/wireless.network ${D}${sysconfdir}/systemd/network
}

FILES_${PN} += " \
    ${sysconfdir}/systemd/network/wired.network \
    ${sysconfdir}/systemd/network/wireless.network \
    "
