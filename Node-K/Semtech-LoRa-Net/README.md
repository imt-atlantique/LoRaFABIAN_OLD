## Note
This is the original Kerlink IoT Station Wiki for using the Semtech LoRa Package with the IoT Station

## Introduction
SEMTECH company gives access to its LoRa libraries and tools across a [Github](https://github.com/Lora-net/lora_gateway).

```
 # cd ~/workdir/
 # git clone https://github.com/Lora-net/lora_gateway.git
 # cd lora_gateway

```
All the help you need is in the README file.
## Configuration
The package configuration must be adapted to the platform.

- Github HAL  **v1.5.0**:
Download the following patch files for Lora-net/lora_gateway: [kerlink_patch_hal_v1.5.0.tar.gz](https://github.com/telecombretagne/LoRaFABIAN/raw/master/Node-K/Semtech-LoRa-Net/kerlink_patch_hal_v1.5.0.tar.gz)

```
# cd lora_gateway
# git apply kerlink_LoRaIoTStationV1_custo_HAL_v1.5.0_lora_gateway.patch
# cd ../packet_forwarder
# git apply kerlink_LoRaIoTStationV1_custo_HAL_v1.3.0_packet_forwarder.patch

```

## Compilation

```
 # cd ~/lora_gateway/libloragw
 # make
/opt/toolchains/arm-2011.03-wirma2/bin/arm-none-linux-gnueabi-gcc -c -O2 -Wall -Wextra -std=c99 -Iinc -I. src/loragw_hal.c -o obj/loragw_hal.o -D LGW_PHY="\"native\"" -D DEBUG_HAL=0
/opt/toolchains/arm-2011.03-wirma2/bin/arm-none-linux-gnueabi-gcc -c -O2 -Wall -Wextra -std=c99 -Iinc -I. src/loragw_gps.c -o obj/loragw_gps.o -D DEBUG_GPS=0
/opt/toolchains/arm-2011.03-wirma2/bin/arm-none-linux-gnueabi-gcc -c -O2 -Wall -Wextra -std=c99 -Iinc -I. src/loragw_reg.c -o obj/loragw_reg.o -D DEBUG_REG=0
/opt/toolchains/arm-2011.03-wirma2/bin/arm-none-linux-gnueabi-gcc -c -O2 -Wall -Wextra -std=c99 -Iinc -I. src/loragw_spi.native.c -o obj/loragw_spi.o -D DEBUG_SPI=0
/opt/toolchains/arm-2011.03-wirma2/bin/arm-none-linux-gnueabi-gcc -c -O2 -Wall -Wextra -Iinc -I. src/loragw_aux.c -o obj/loragw_aux.o -D DEBUG_AUX=0
/opt/toolchains/arm-2011.03-wirma2/bin/arm-none-linux-gnueabi-ar rcs libloragw.a obj/loragw_hal.o obj/loragw_gps.o obj/loragw_reg.o obj/loragw_spi.o obj/loragw_aux.o
/opt/toolchains/arm-2011.03-wirma2/bin/arm-none-linux-gnueabi-gcc -O2 -Wall -Wextra -std=c99 -Iinc -I. tst/test_loragw_spi.c obj/loragw_spi.o -o test_loragw_spi -lrt
/opt/toolchains/arm-2011.03-wirma2/bin/arm-none-linux-gnueabi-gcc -O2 -Wall -Wextra -std=c99 -Iinc -I. tst/test_loragw_reg.c obj/loragw_reg.o obj/loragw_spi.o -o test_loragw_reg -lrt
/opt/toolchains/arm-2011.03-wirma2/bin/arm-none-linux-gnueabi-gcc -O2 -Wall -Wextra -std=c99 -Iinc -I. tst/test_loragw_hal.c obj/loragw_hal.o obj/loragw_reg.o obj/loragw_spi.o obj/loragw_aux.o -o test_loragw_hal -lrt
/opt/toolchains/arm-2011.03-wirma2/bin/arm-none-linux-gnueabi-gcc -O2 -Wall -Wextra -std=c99 -Iinc -I. tst/test_loragw_gps.c obj/loragw_gps.o obj/loragw_hal.o obj/loragw_reg.o obj/loragw_spi.o obj/loragw_aux.o -o test_loragw_gps -lrt

```

Note: the toolchain must be installed.
The original toolchain by Kerlink is : arm-2011.03-wirma2

Must be compatible with the one 2011.03 Found here.
http://sourcery.mentor.com/public/gnu_toolchain/arm-none-linux-gnueabi/


Toolchain instalation from Kerlink Wiki.

## Toolchain
Kerlink provides the [arm-2011.03-wirma2-r59.tar.xz]() package which contains the cross-toolchain, libraries and headers necessary to the compilation of an application. This toolchain is extracted from Buildroot.
### Toolchain Install 

```
mkdir /opt/toolchains
cd /opt/toolchains
tar xJf arm-2011.03-wirma2-r59.tar.xz

```

You must add the toolchain to your environnement PATH:

```
PATH=$PATH:/opt/toolchains/arm-2011.03-wirma2/bin

```

In order to run in 64bits hardware, you must install ia32-libs :

```
sudo apt-get install ia32-libs

```
