DESCRIPTION = "A console image with Qt5"

LICENSE = "MIT"

require recipes-qt/images/qt5-image.bb

QT5_EXTRA_PKGS = " \
	qtbase	\
	qtdeclarative \
	qtmultimedia \
	qtsvg \
	qtsensors \
	qtimageformats \
	qtsystems \
	qtscript \
	qt3d \
	qtgraphicaleffects \
	qtconnectivity \
	qtlocation \
	qtquickcontrols \
	qtquickcontrols2 \
    "
QT5_NO_INSTALL = " \
    qtwebengine \
    "

QT5_PKGS = " \
	qtbase-dev \
	qtbase-mkspecs \
	qtbase-plugins \
	qtbase-tools \
	qtcanvas3d \
	qtcharts \
	qtdatavis3d \
	qtenginio \
	qtgamepad \
	qtnetworkauth \
	qtquick1 \
	qtscxml \
	qtserialbus \
	qtserialport \
	qtserialport-dev \
	qtserialport-mkspecs \
	qttools \
	qttranslations \
	qtvirtualkeyboard \
	qtwebchannel \
	qtwebkit-examples \
	qtwebkit \
	qtwebsockets \
	qtx11extras \
	qtxmlpatterns \
	"

QT5_CONN = " \
    libconnman-qt5 \
    libqofono \
    "

QT5_TOOLS = " \
    qtchooser \
    "

QT5_OWN = " \
    qtfoo \
    "

FONTS = " \
	fontconfig \
	fontconfig-dev \
	fontconfig-utils \
	ttf-bitstream-vera \
	"

IMAGE_INSTALL += " \
    ${QT5_PKGS} \
    ${QT5_CONN} \
    ${QT5_TOOLS} \
    ${QT5_OWN} \
    ${FONTS} \
    "

# Export the image name
export IMAGE_BASENAME = "tinker-qt5"

