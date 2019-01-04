DESCRIPTION = "A script that sets up an OpenVPN server and a client"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

S = "${WORKDIR}"

PR = "r0"

SRC_URI = " \
    file://openvpn-setup.sh \
    file://rules.ipt \
    "

RDEPENDS_${PN} += "bash iptables openvpn"

# We have a lot of kernel module dependencies

MODS_EXCLUDED += " \
    kernel-module-ip-tables \
    kernel-module-ipt-masquerade \
    kernel-module-iptable-filter \
    kernel-module-iptable-nat \
    kernel-module-nf-conntrack \
    kernel-module-nf-conntrack-ipv4 \
    kernel-module-nf-defrag-ipv4 \
    kernel-module-nf-nat \
    kernel-module-x-tables \
    "

RDEPENDS_${PN} += " \
    "

do_install() {
	install -d -m 0755 ${D}/${sysconfdir}/scripts
	install -m 0755 openvpn-setup.sh ${D}/${sysconfdir}/scripts/openvpn-setup.sh
    install -d -m 0755 ${D}/home/root
    install -m 0644 rules.ipt ${D}/home/root/rules.ipt
}

FILES_${PN} += " \
	${sysconfdir}/scripts/openvpn-setup.sh \
    /home/root/rules.ipt \
	"

