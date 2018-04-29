#!/bin/bash

############################################################
# Author     : Santiago Pagola
# Date       : 2018/04/29
#
#
# Description: Script to flash RootFs, Boot files and
#              bootloader for the wandboard
############################################################

######################### Variables #########################
MACHINE=wandboard
OE_BUILD_DIR=${HOME}/wandboard-jumpnow/build

SDCARD_UNIT=sdb

SDCARD_DEV=/dev/${SDCARD_UNIT}
SDCARD_DEV_ROOTFS_PART=${SDCARD_DEV}1
###############################################################

print_vars()
{
    echo "$(basename $0): A script to flash RootFS, U-Boot files, Kernel zImage and Device Tree Files to an SDCard"
    echo -e "\nCurrently defined variables:"
    echo -e "MACHINE:\t${MACHINE}"
    echo -e "OE_BUILD_DIR:\t${OE_BUILD_DIR}"
    echo -e "SDCARD_UNIT:\t${SDCARD_UNIT}"
    echo -e "IMAGENAME:\t${IMAGENAME}"

    echo -ne "\nPlease hit any key to continue..."
    read foo
}

die()
{
    echo $@
    echo "Exiting..."
    exit 1
}

check_env_vars()
{
    if [[ -z ${MACHINE} ]] || [[ -z ${OE_BUILD_DIR} ]]; then
        die "You need to set both MACHINE and OE_BUILD_DIR variables in order to run this script."
    fi
    ##### These two variables can be set from the environment or
    ##### modified directly from here.
    if [[ -z ${IMAGENAME} ]]; then
        IMAGENAME="wand-dual-qt5"
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
    if [[ -n ${MOUNTED_PARTS} ]]; then
        echo "$(eval echo ${MOUNTED_PARTS} | sed 's/\n/ /g') are mounted. Unmounting first ..."
        echo "------------------"
        umount ${MOUNTED_PARTS}
        echo "------------------"
        if [ $? -ne 0 ]; then
            die "This script will now terminate."
        else
            echo "Unmounting done! Let's proceed..."
        fi
    fi
}

# Print some intro
print_vars

# Check if the needed variables are set
check_env_vars

# Check if the card is in the reader...
check_if_sdcard_exists

# Check if some partitions are still mounted
check_if_sdcard_mounted

# We want to create an ext4 partition for the rootfs, starting at 4MB
echo "Creating partition"
sudo dd if=/dev/zero of=${SDCARD_DEV} bs=1024 count=2014
{
    echo 8182,,,*
} | sudo sfdisk ${SDCARD_DEV}

# Next: copy the bootloader to the unpartitioned area (first 4MiB)
DEPLOY_DIR=${OE_BUILD_DIR}/tmp/deploy/images/${MACHINE}
if [[ ! -d ${DEPLOY_DIR} ]]; then
    die "Error: \"${DEPLOY_DIR}\" does not exist."
fi

if [ ! -f ${DEPLOY_DIR}/SPL-${MACHINE} ]; then
    echo "File not found: ${DEPLOY_DIR}/SPL-${MACHINE}"
    exit 1
fi

if [ ! -f ${DEPLOY_DIR}/u-boot-${MACHINE}.img ]; then
    echo "File not found: ${DEPLOY_DIR}/u-boot-${MACHINE}.img"
    exit 1
fi

echo "Copying SPL ..."
sudo dd if=${DEPLOY_DIR}/SPL-${MACHINE} of=${SDCARD_DEV} conv=notrunc seek=2 skip=0 bs=512

echo "Copying U-Boot ..."
sudo dd if=${DEPLOY_DIR}/u-boot-${MACHINE}.img of=${SDCARD_DEV} conv=notrunc seek=69 skip=0 bs=1K


# Next, copy the root file system
echo "Formatting ${SDCARD_DEV_ROOTFS_PART} as ext4"
sudo mkfs.ext4 -L ROOT ${SDCARD_DEV_ROOTFS_PART}

echo "Mounting ${SDCARD_DEV_ROOTFS_PART}"
sudo mount ${SDCARD_DEV_ROOTFS_PART} /media/card

echo "Extracting ${IMAGENAME}-image-${MACHINE}.tar.xz to /media/card"
sudo tar -C /media/card -xJf ${DEPLOY_DIR}/${IMAGENAME}-image-${MACHINE}.tar.xz

echo "Writing hostname to /etc/hostname"
export TARGET_HOSTNAME
sudo -E bash -c 'echo ${TARGET_HOSTNAME} > /media/card/etc/hostname'        

echo "Unmounting ${SDCARD_DEV_ROOTFS_PART}"
sudo umount ${SDCARD_DEV_ROOTFS_PART}

sync && echo "Done!"

