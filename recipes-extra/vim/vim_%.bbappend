# Recipe append for VIM

FILESEXTRAPATHS_prepend := "${THISDIR}/vim:"

SRC_URI += " \
    file://.vimrc \
    "
