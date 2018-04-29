DESCRIPTION = "Some useful fonts to use when loading fonts from QFontDatabase"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "http://sourceforge.net/projects/dejavu/files/dejavu/2.37/dejavu-fonts-ttf-2.37.tar.bz2"
SRC_URI[md5sum] = "d0efec10b9f110a32e9b8f796e21782c"
SRC_URI[sha256sum] = "fa9ca4d13871dd122f61258a80d01751d603b4d3ee14095d65453b4e846e17d7"

S = "${WORKDIR}/${PN}-${PV}"

do_install() {
    # Install fonts directory
    echo "Working directory is: " ${WORKDIR}
    echo "S is: " ${S}
    echo "Pwd is " $(pwd)
    install -d -m 0755 ${D}/${libdir}/fonts

    install -d -m 0755 ${D}/${libdir}/fonts/ttf
    install -m 0644 ${S}/ttf/* ${D}/${libdir}/fonts/ttf

    install -d -m 0755 ${D}/${libdir}/fonts/fontconfig
    install -m 0644 ${S}/fontconfig/* ${D}/${libdir}/fonts/fontconfig

    install -m 0644 ${S}/AUTHORS ${D}/${libdir}/fonts
    install -m 0644 ${S}/BUGS ${D}/${libdir}/fonts
    install -m 0644 ${S}/NEWS ${D}/${libdir}/fonts
    install -m 0644 ${S}/LICENSE ${D}/${libdir}/fonts
    install -m 0644 ${S}/README.md ${D}/${libdir}/fonts
    install -m 0644 ${S}/langcover.txt ${D}/${libdir}/fonts
    install -m 0644 ${S}/status.txt ${D}/${libdir}/fonts
    install -m 0644 ${S}/unicover.txt ${D}/${libdir}/fonts

    # Install symlinks
    files=$(find ./ttf -type f)
    echo $files | sed -e 's/\s\+/\n/g'
    for file in $files; do
        ln -s $file ${D}/${libdir}/fonts/$(basename $file)
    done 

    fontconfigs=$(find ./fontconfig -type f)
    for config in $fontconfigs; do
        ln -s $config ${D}/${libdir}/fonts/$(basename $config)
    done
}

FILES_${PN} += " \
    ${libdir} \
    ${libdir}/fonts \
    ${libdir}/fonts/* \
    "

