# Apply our custom patches
FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += " \
    file://0001-Custom-modifications.patch \
    "
