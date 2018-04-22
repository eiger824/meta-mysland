do_install_append() {
    sed -e 's/\(\s\+DirectoryIndex\s\+\)index.html/\1home.html/g' -i ${D}/${sysconfdir}/${BPN}/httpd.conf
}

pkg_postinst_${PN}() {
    if ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}; then
        if [ -n "$D" ]; then
            OPTS="--root=$D"
        fi
        systemctl $OPTS enable apache2.service
    fi
}
