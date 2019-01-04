IMAGE 		:= fsl-image-multimedia-full
MACHINE 	:= wandboard
IMAGEPATH 	:= ./tmp/deploy/images/$(MACHINE)
DEVICE		:= /dev/sdb
KERNEL		:= virtual/kernel
BB 			:= bitbake

# pi = package-index
all: kernel image pi 

image: $(IMAGE)

# Rebuild image if building kernel
kernel: $(KERNEL) $(IMAGE)

$(KERNEL):
	$(BB) $(KERNEL)

$(IMAGE):
	$(BB) $(IMAGE)

pi:
	$(BB) package-index

clean:
	$(BB) $(KERNEL) -c cleansstate
	$(BB) $(IMAGE) -c cleansstate

flash:
	@sudo bash -c "bmaptool copy $(IMAGEPATH)/$(IMAGE)-$(MACHINE).wic $(DEVICE)"
	@sync

