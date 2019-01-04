IMAGE 		:= fsl-image-multimedia-full
MACHINE 	:= wandboard
IMAGEPATH 	:= ./tmp/deploy/images/$(MACHINE)
DEVICE		:= /dev/sdb
KERNEL		:= virtual/kernel
BB 			:= bitbake

all: image kernel

image: $(IMAGE)
kernel: $(KERNEL)

$(KERNEL):
	$(BB) $(KERNEL)

$(IMAGE):
	$(BB) $(IMAGE)

clean:
	$(BB) $(KERNEL) -c cleansstate
	$(BB) $(IMAGE) -c cleansstate

flash:
	@echo sudo bash -c "bmaptool copy $(IMAGEPATH)/$(IMAGE).wic $(DEVICE)"
