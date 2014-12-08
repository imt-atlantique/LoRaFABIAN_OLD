LoRaFABIAN
==========

LoRAFABIAN proyect. Open Source Code.

This repository gathers the diferent parts of code that can be used for building the LoRaFABIAN Architecture.
We present the code used for the Demonstrator used in the late part of the year 2014.


##Demo (Scenario 1)

LoRaFABIAN demonstrator used, with slight modifications, on several events: Rencontres Inria Industrie, FOSSA 2014 and on the visit of Axelle Lemaire for the inaguration of the "French Tech" in Rennes.


###Architecture
**Node** (Arduino+Wi6Labs-Shield) <--- *LoRA [802.15.4+CoAP]*---> **Node-K** (Kerlkink IoT) <---*UDP[SemtechJsonProtocol]*---> **Area GW+Proxy** <---*HTTP*---> **Android App**.

###Code
* **Node Arduino**. [Code: /Arduino/demo/loraDemoADALED ](https://github.com/telecombretagne/LoRaFABIAN/tree/master/Arduino/demo/loraDemoADALED)
* **Node Wi6Labs-Shield**. [Code: Wi6labs/lorafabian/examples/lorafabian/lorafab_beacon_answer](https://github.com/Wi6labs/lorafabian/tree/master/examples/lorafabian/lorafab_beacon_answer)
* **Node-K**. [Code: Lora-net/packet_forwarder/basic_pkt_fwd](https://github.com/Lora-net/packet_forwarder/tree/master/basic_pkt_fwd)
* **AreaGW+Proxy**. [Code: /Area-GW/Area-GW-HTTPProxy](https://github.com/telecombretagne/LoRaFABIAN/tree/master/Area-GW/Area-GW-HTTPProxy)
* **Android APP**. [Code: /Android/android_lora_demo_app](https://github.com/telecombretagne/LoRaFABIAN/tree/master/Android/android_lora_demo_app).


####Lora Configuration
Must be configured on Node Wi6Labs-Shield, and also on Node-K/basick_pkt_forwarder.
Must Match on both Sides. In our demos we used:

  * SF 8
  * BW 250 kHz
  * Frequency: 870.0 MHz
  * CR: 4/6

####Semtech Protocol (Json Tunnel)
 * Address:  *The IP of the AreaGW+Proxy*
 * Upstream Protocol UDP port: *1790*
 * Downstream Protocol UDP port: *1792*
