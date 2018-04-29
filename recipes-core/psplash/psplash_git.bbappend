pkg_postinst_${PN}_append () {
	if ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}; then
		if [ -n "$D" ]; then
			OPTS="--root=$D"
		fi
		# Revert the masking operation
		systemctl $OPTS unmask psplash.service
	fi
}
