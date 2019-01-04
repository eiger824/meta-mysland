DESCRIPTION = "A console image with some utilities"

LICENSE = "MIT"

IMAGE_FEATURES += "package-management" 
inherit core-image

UTILS = " \
	openssh \
	openssh-keygen \
	openssh-sftp-server \
	psplash \
	tzdata \
	"

KERNEL_EXTRA_INSTALL = " \
	kernel-modules \
	"

WIFI_SUPPORT = " \
	crda \
	iw \
	wpa-supplicant \
	"

DEV_SDK_INSTALL = " \
	bash \
	binutils \
	binutils-symlinks \
	coreutils \
	cpp \
	cpp-symlinks \
	diffutils \
	file \
	g++ \
	g++-symlinks \
	gdb \
	gcc \
	gcc-symlinks \
	gettext \
	git \
	ldd \
	libstdc++ \
	libstdc++-dev \
	libtool \
	make \
    cmake \
	perl-modules \
	pkgconfig \
	python-modules \
	python3-modules \
	"

EDITORS = "\
	vim \
    vim-keymaps \
	nano \
	"

DEV_EXTRAS = "\
	ntp \
	ntp-tickadj \
	"

TOOLS = "\
	acpid \
	bc \
	bzip2 \
	devmem2 \
	dosfstools \
	ethtool \
	findutils \
	i2c-tools \
	iperf3 \
	htop \
	less \
	memtester \
	netcat \
	procps \
	rsync \
	sysfsutils \
	tcpdump \
	unzip \
	util-linux \
	util-linux-blkid \
    util-linux-lscpu \
	wget \
	zip \
	"

CORE_FW = "firmware-imx linux-firmware"

ZWAVE = "\
    openzwave \
    mysland-ozw \
    mysland-ozw-srv \
    mysland-ozw-cli \
    mysland-ozw-web \
    "
CORE_PKGS = "\
    libgcc libgcc-dev libstdc++-staticdev \
    autoconf automake ccache chkconfig glib-networking glibmm \
    "

OPENVPN = "\
    openvpn \
    openvpn-dev \
    openvpn-sample \
    openvpn-setup \
    "

IPROUTE2 = "\
    iproute2-ss \
    iproute2-tc \
    iproute2-lnstat \
    iproute2-ifstat \
    iproute2-genl \
    iproute2-rtacct \
    iproute2-nstat \
    iproute2-ss \
    iproute2-rtacct \
    "
MISCELLANEOUS = "\
	opkg \
	opkg-utils \
	bluez5 \
	connman \
	connman-conf \
    bcm4339-nvram-config \
	lsof \
	ifupdown \
	init-ifupdown \
    fbset-modes \
    fbgrab \
    easy-rsa \
    ufw \
    bridge-utils \
    iptables \
    apache2 \
    php \
    php-cli \
    php-modphp \
    man \
    man-pages \
    sudo \
    strace \
    packagegroup-core-tools-debug \
    packagegroup-tools-bluetooth \
    packagegroup-core-buildessential \
    customenv \
    ftp-setup \
    wlan-setup \
    mesa \
    u-boot-scr \
    ${CORE_PKGS} \
	"

IMAGE_INSTALL += " \
	${UTILS} \
	${KERNEL_EXTRA_INSTALL} \
	${WIFI_SUPPORT} \
	${DEV_SDK_INSTALL} \
	${EDITORS} \
	${DEV_EXTRAS} \
	${TOOLS} \
    ${CORE_FW} \
    ${OPENVPN} \
    ${IPROUTE2} \
    ${MISCELLANEOUS} \
	"

export IMAGE_BASENAME = "wand-dual-image"

