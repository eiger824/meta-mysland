# According to the systemd units provided by these recipes, systemd will look
# the configuration files at /etc/wpa-supplicant/wpa-supplicant{-nl80211@wlan0,-wired@eth0},
# but by default there are no such files installed. We add thus our own interface-specific
# configurations

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += " \
    file://wpa_supplicant-nl80211-wlan0.conf \
    file://wpa_supplicant-wlan0.conf \
    "

# Install these under /etc/wpa-supplicant
do_install_append() {
    install -d -m0755 ${D}${sysconfdir}/wpa_supplicant
    install -m0644 ${WORKDIR}/wpa_supplicant-wlan0.conf ${D}${sysconfdir}/wpa_supplicant
    install -m0644 ${WORKDIR}/wpa_supplicant-nl80211-wlan0.conf ${D}${sysconfdir}/wpa_supplicant
}


# Ship them together with the rest of the files
FILES_${PN} += " \
    ${sysconfdir}/wpa_supplicant/*.conf \
    "
