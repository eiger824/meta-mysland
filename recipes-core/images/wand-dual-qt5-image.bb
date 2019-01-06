DESCRIPTION = "A console image with Qt5"

LICENSE = "MIT"

require wand-dual-image.bb

PV = "0.2"

# Exclude for now. The problem is that since we are using the latest upstream kernel
# (v4.16.5 as of 30/04/2018), the drivers for the Vivante GPU of the Wandboard Dual
# haven't been ported to this kernel (they are valid up to kernel version 4.1.15??).
# So instead, we will use the framebuffer to render graphics (sw rendering). The
# major disadvantage is that we need to exclude many packages that require the GPU
# driver (or some sort of OpenGL presence) which we are excluding from our builds
# rightnow.

QT5_PKGS_DONT_INCLUDE = " \
	qtwebchannel \
	qtwebengine \
	qtwebkit-examples \
	qtwebkit \
	qtwebsockets \
	qtxmlpatterns \
	qtdeclarative \
	qtquickcontrols \
	qtquickcontrols2 \
	qtscxml \
	qt3d \
	qtcanvas3d \
	qtdatavis3d \
	qtgraphicaleffects \
	qtenginio \
	qtgamepad \
	qtimageformats \
	qtvirtualkeyboard \
	qtcharts \
	qtlocation \
	qttools \
    "

QT5_PKGS = " \
	qtbase \
	qtbase-dev \
	qtbase-mkspecs \
	qtbase-plugins \
	qtbase-tools \
	qtconnectivity \
	qtmultimedia \
	qtnetworkauth \
	qtscript \
	qtsensors \
	qtserialbus \
	qtserialport \
	qtserialport-dev \
	qtserialport-mkspecs \
	qtsvg \
	qtsystems \
	qttranslations \
	"

FONTS = " \
	fontconfig \
	fontconfig-dev \
	fontconfig-utils \
	ttf-bitstream-vera \
    dejavu-fonts-ttf \
	"

TSLIB = " \
	tslib \
	tslib-calibrate \
	tslib-conf \
	tslib-dev \
	"
APPS = " \
	mouseevents \
    ping-server \
    fallingblocks \
	"

IMAGE_INSTALL += " \
	${QT5_PKGS} \
	${FONTS} \
	${TSLIB} \
	${APPS} \
	"

export IMAGE_BASENAME = "wand-dual-qt5-image"

