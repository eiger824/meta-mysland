SUMMARY = "A minimal set of nice keymaps to have with vim"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

PR = "r0"

SRC_URI = "\
    file://vim-keymaps.txt \
    file://plugins/*.vim \
    "

do_install() {
    install -dm0755 ${D}/home/root
    install -m0644 ${WORKDIR}/vim-keymaps.txt ${D}/home/root/.vimrc

    install -dm0755 ${D}/home/root/.vim/plugins
    install -m0755 ${WORKDIR}/plugins/plugins/*.vim ${D}/home/root/.vim/plugins
}

FILES_${PN} = "/home/root/.vimrc /home/root/.vim/plugins/*"
