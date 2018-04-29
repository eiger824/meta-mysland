#!/bin/bash

############################################################
# Author     : Santiago Pagola
# Date       : 2018/04/28
#
#
# Description: Script to flash RootFs, Boot files and
#              bootloader for the wandboard
############################################################

######################### Variables #########################
SDCARD_UNIT=sdb

SDCARD_DEV=/dev/${SDCARD_UNIT}
SDCARD_DEV_BOOT_PART=${SDCARD_DEV}1
SDCARD_DEV_ROOTFS_PART=${SDCARD_DEV}2

COPY_BOOT_SCRIPT="copy_boot.sh"
COPY_ROOTFS_SCRIPT="copy_rootfs.sh"

EXTLINUX_FILE="extlinux.conf"
EXTLINUX_DIR="extlinux"
###############################################################

die()
{
    echo $@
    echo "Exiting..."
    exit 1
}

check_env_vars()
{
    if [[ -z ${MACHINE} ]] || [[ -z ${OETMP} ]]; then
        die "You need to set both MACHINE and OETMP variables in order to run this script."
    fi
    ##### These two variables can be set from the environment or
    ##### modified directly from here.
    if [[ -z ${IMAGENAME} ]]; then
        IMAGENAME="wbdimg-qt5"
        echo "WARNING: \"IMAGENAME\" variable was not set, defaulting to \"${IMAGENAME}\""
    fi
    if [[ -z ${TARGET_HOSTNAME} ]]; then
        TARGET_HOSTNAME="wbd"
        echo "WARNING: \"TARGET_HOSTNAME\" variable was not set, defaulting to \"${TARGET_HOSTNAME}\""
    fi
}

complete_after_result()
{
    local error_code=$1
    if [ $error_code -eq 0 ]; then
        echo " [ok]"
    else
        echo " [fail]"
    fi
}

check_if_sdcard_exists()
{
    if [[ ! -b ${SDCARD_DEV} ]]; then
        die "Unable to find ${SDCARD_DEV}"
    fi
}

check_if_sdcard_mounted()
{
    # First of all, ensure no partitions are mounted on the sdcard
    MOUNTED_PARTS=$(df | \grep -i -E ${SDCARD_DEV}'[0-9]*' | cut -d" " -f1)
    if [[ -n $MOUNTED_PARTS ]]; then
        echo "$(eval echo $MOUNTED_PARTS | sed 's/\n/ /g') are mounted. Unmounting first ..."
        echo "------------------"
        umount $MOUNTED_PARTS
        echo "------------------"
        if [ $? -ne 0 ]; then
            die "This script will now terminate."
        else
            echo "Unmounting done! Let's proceed..."
        fi
    fi
}

# Check if the needed variables are set
check_env_vars

# Check if the card is in the reader...
check_if_sdcard_exists

# Check if some partitions are still mounted
check_if_sdcard_mounted

# We want to create two partitions on the sdcard: a VFAT filesystem
# for the kernel + dtb(s) and an ext4 partition for the rootfs
{ echo 8192,131072,0x0C,*; echo 139264,+,0x83,-; } | sudo sfdisk ${SDCARD_DEV}

# Create the VFAT filesystem
sudo mkfs.vfat -F 32 ${SDCARD_DEV_BOOT_PART} -n BOOT

# Next: copy the bootloader to the unpartitioned area (first 4MiB) with
# Scott Ellis' script. Check if it exists first ...
if [[ ! -f ${COPY_BOOT_SCRIPT} ]]; then
    die "Error: ${COPY_BOOT_SCRIPT} not present in current directory."
fi
./${COPY_BOOT_SCRIPT} ${SDCARD_UNIT}

# Now, mount this partition into /mnt to copy the zImage and all the dtbs,
# plus the extlinux.conf file
sudo mount ${SDCARD_DEV_BOOT_PART} /mnt

# Create an extlinux directory where the extlinux.conf file will be
test -d /mnt/${EXTLINUX_DIR} || sudo mkdir /mnt/${EXTLINUX_DIR} 

# Time to copy the dtbs, zImage and extlinux.conf
echo -n "Copying ${EXTLINUX_FILE} ... "
sudo cp ${OETMP}/deploy/images/${MACHINE}/extlinux.conf /mnt/extlinux
complete_after_result $?

echo -ne "Copying dtbs ... "
sudo cp ${OETMP}/deploy/images/${MACHINE}/imx6*.dtb /mnt
complete_after_result $?

echo -ne "Copying zImage ... "
sudo cp ${OETMP}/deploy/images/${MACHINE}/zImage /mnt
complete_after_result $?

sync && sudo umount /mnt

# Next, copy the root file system via Scott Ellis' script.
# Again, check if it exists first...
if [[ ! -f ${COPY_ROOTFS_SCRIPT} ]]; then
    die "Error: ${COPY_ROOTFS_SCRIPT} not present in current directory."
fi
./${COPY_ROOTFS_SCRIPT} ${SDCARD_UNIT} ${IMAGENAME} ${TARGET_HOSTNAME}

sync && echo "Done!"

