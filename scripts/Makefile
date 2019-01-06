# Default image
DEFAULTIMAGE := wand-dual-qt5-image
MACHINE 	 := wandboard
IMAGEPATH 	 := ./tmp/deploy/images/$(MACHINE)
DEVICE		 := /dev/sdb
KERNEL		 := virtual/kernel
PKGIDX		 := package-index
# Preferrable over targets like "meta-toolchain[-X]"
TOOLCHAINCMD := -c populate_sdk
BB 			 := bitbake

# If IMAGE was not provided, set it to the default one
ifndef IMAGE
$(warning IMAGE was not set, setting it to $(DEFAULTIMAGE))
IMAGE=$(DEFAULTIMAGE)
endif

# pi = package-index
all: kernel image pi 

image: $(IMAGE) $(PKGIDX)

# Rebuild image if building kernel
kernel: $(KERNEL) $(IMAGE) $(PKGIDX) 

pi: $(PKGIDX)

$(KERNEL):
	$(BB) $(KERNEL)

$(IMAGE):
	$(BB) $(IMAGE)

$(PKGIDX):
	$(BB) $(PKGIDX)

pi:
	$(BB) package-index

# Cleansstate targets
clean: kernelclean imageclean 
kernelclean:
	$(BB) $(KERNEL) -c cleansstate
imageclean:
	$(BB) $(IMAGE) -c cleansstate

flash:
	@sudo bash -c "bmaptool copy $(IMAGEPATH)/$(IMAGE)-$(MACHINE).wic $(DEVICE)"
	@sync


toolchain:
	$(BB) $(IMAGE) $(TOOLCHAINCMD) 
