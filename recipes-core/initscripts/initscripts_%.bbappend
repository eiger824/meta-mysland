pkg_postinst_${PN}_append() {

	cd $D${sysconfdir}/init.d  ||  exit 0
	
	if [ -n "$D" ]; then
		OPTS="--root=$D"
	else
		OPTS=""
	fi

	for i in ${SYSTEMD_DISABLED_SYSV_SERVICES} ; do
		if [ -e $i -o -e $i.sh ]  &&   ! [ -e $D${sysconfdir}/systemd/system/$i.service -o -e $D${systemd_unitdir}/system/$i.service ] ; then
			# Reverse the masking operation
			systemctl $OPTS unmask $i.service
		fi
	done
}
