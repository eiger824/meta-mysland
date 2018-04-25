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
	wireless-tools \
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
	gdbserver \
	gcc \
	gcc-symlinks \
	gettext \
	git \
	ldd \
	libstdc++ \
	libstdc++-dev \
	libtool \
	make \
	perl-modules \
	pkgconfig \
	python-modules \
	python3-modules \
	"

EDITORS = " \
	vim \
	nano \
	"

DEV_EXTRAS = " \
	ntp \
	ntp-tickadj \
	"

TOOLS = " \
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
	wget \
	zip \
	"

MQTT = " \
	libmosquitto1 \
	libmosquittopp1 \
	mosquitto \
	mosquitto-dev \
	mosquitto-clients \
	python-paho-mqtt \
	"

IMAGE_INSTALL += " \
	${UTILS} \
	${KERNEL_EXTRA_INSTALL} \
	${WIFI_SUPPORT} \
	${DEV_SDK_INSTALL} \
	${EDITORS} \
	${DEV_EXTRAS} \
	${TOOLS} \
	"

export IMAGE_BASENAME = "wbdimg"

