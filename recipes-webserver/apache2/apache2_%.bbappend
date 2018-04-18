do_install_append() {
    sed -e 's/\(\s\+DirectoryIndex\s\+\)index.html/\1home.html/g' -i ${D}/${sysconfdir}/${BPN}/httpd.conf
}
