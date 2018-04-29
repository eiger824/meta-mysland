pkg_postinst_${PN}_append () {
	if ${@bb.utils.contains('DISTRO_FEATURES','systemd sysvinit','true','false',d)}; then
		if [ -n "$D" ]; then
			OPTS="--root=$D"
		fi
		# Revert mask operation
		systemctl $OPTS unmask keymap.service
	fi
}

