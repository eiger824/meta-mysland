DESCRIPTION = "A console image with Qt5"

LICENSE = "MIT"

require wbdimg.bb

QT5_PKGS = " \
	qt3d \
	qtbase \
	qtbase-dev \
	qtbase-mkspecs \
	qtbase-plugins \
	qtbase-tools \
	qtcanvas3d \
	qtcharts \
	qtconnectivity \
	qtdatavis3d \
	qtdeclarative \
	qtenginio \
	qtgamepad \
	qtgraphicaleffects \
	qtimageformats \
	qtlocation \
	qtmultimedia \
	qtnetworkauth \
	qtquick1 \
	qtquickcontrols \
	qtquickcontrols2 \
	qtscript \
	qtscxml \
	qtsensors \
	qtserialbus \
	qtserialport \
	qtserialport-dev \
	qtserialport-mkspecs \
	qtsvg \
	qtsystems \
	qttools \
	qttranslations \
	qtvirtualkeyboard \
	qtwebchannel \
	qtwebengine \
	qtwebkit-examples \
	qtwebkit \
	qtwebsockets \
	qtx11extras \
	qtxmlpatterns \
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
	tslib-tests \
	"
APPS = " \
	mouseevents \
	qt-launcher \
    ping-server \
    fallingblocks \
	"

IMAGE_INSTALL += " \
	${QT5_PKGS} \
	${FONTS} \
	${TSLIB} \
	${APPS} \
	"

export IMAGE_BASENAME = "wbdimg-qt5"

