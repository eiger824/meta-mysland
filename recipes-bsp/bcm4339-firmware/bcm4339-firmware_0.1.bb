SUMMARY     = "Broadcom BCM4339 (Ampak) firmware files"
DESCRIPTION = "In order to use the bcmdhd wifi/bt driver module, these proprietary firmware files need to be used in the Wandboard Revision D1"

S       = "${WORKDIR}"

SRC_URI = " \
   file://LICENCE.broadcom_bcm43xx \
"

LICENSE     = "Proprietary"
LIC_FILES_CHKSUM = "file://LICENCE.broadcom_bcm43xx;md5=3160c14df7228891b868060e1951dfbc"

PR          = "r0"

MAIN_FW_BIN     = "fw_bcm4339a0_ag.bin"
MAIN_NVRAM_FILE = "nvram_ap6335.txt"

# Main firmware binary
SRC_URI += "file://${MAIN_FW_BIN}"

# NVRAM file
SRC_URI += "file://${MAIN_NVRAM_FILE}"

do_install() {
    install -d -m 0755 ${D}/lib/firmware/brcm
    install -m 0644 ${WORKDIR}/${MAIN_FW_BIN}     ${D}/lib/firmware/brcm/
    install -m 0644 ${WORKDIR}/${MAIN_NVRAM_FILE} ${D}/lib/firmware/brcm/
}

FILES_${PN} = "/lib/firmware/brcm/*"
