# We want to include apache2 in this list, so add apache2 to the PACKAGECONFIG value
# that was previously defined in the original php recipe

PACKAGECONFIG = "mysql apache2 sqlite3 imap opcache \
                 ${@bb.utils.filter('DISTRO_FEATURES', 'ipv6 pam', d)} \
                 "

DEPENDS_${PN} += "valgrind"
