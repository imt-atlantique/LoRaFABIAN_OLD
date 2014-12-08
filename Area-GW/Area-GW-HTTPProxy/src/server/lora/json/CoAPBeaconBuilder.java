package server.lora.json;


import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.ArrayUtils;

import server.LoRaFabServer;
import server.lora.LoRAProtocol;

public class CoAPBeaconBuilder {

	
	
	
/*
	typedef struct __attribute__ ((__packed__)) {
		unsigned char frame802154_start[7];
		uint64_t gw_mac;
		unsigned char coap[9];


	} frame_802154;*/

	/* data buffers */
	public static byte[] buff_up  = new byte[5000];
	
	//public int generate_json_coap_beacon( uint64_t gw_mac ){
	//public static int generate_json_coap_beacon( byte[] gw_mac ){
	public static byte[] generate_json_coap_beacon( byte[] gw_mac ){
		
		// https://github.com/Lora-net/packet_forwarder/blob/master/PROTOCOL.TXT#L288
		/*
		 *
		 * ### 5.4. PULL_RESP packet ###

			That packet type is used by the server to send RF packets and associated
			metadata that will have to be emitted by the gateway.

			  Bytes | Function
			:------:|---------------------------------------------------------------------
				  0 | protocol version = 1
				1-2 | unused bytes
				  3 | PULL_RESP identifier 0x03
			  4-end | JSON object, starting with {, ending with }, see section 6

		 *
		 * */
		
		byte[] buff_header  = new byte[4];
		buff_header[0] = LoRAProtocol.PROTOCOL_VERSION;
		buff_header[3] = LoRAProtocol.PKT_PULL_RESP;


		/* start of JSON structure */
		
		String json_beacon_as_string= "{\"txpk\":{"
									+  "\"imme\":true"
									+ ",\"freq\":870.000000"
									+ ",\"rfch\":0"
									+ ",\"powe\":14"
									+ ",\"modu\":\"LORA\""
									+ ",\"datr\":\"SF8"
									+ 			  "BW250\""
									+ ",\"codr\":\"4/6\""
									+ ",\"ipol\":false"
									;


		/* IEEE 802.15.4 Frame Example
		 *
		 *	0x41C8D110-10FFFF36-36323859-535400
		 *
		 * 		Frame control: 0x41C8 (specific, read IEEE 802.15.4)
		 *
		 * 		Sequence Number: 0xD1 (can be other)
		 *
		 *		Destination PAN ID:  0x1010 (OED)
		 *
		 * 		Destination address: 0xFFFF (Broadcast)
		 *
		 * 		Source PAN ID:  (NULL)
		 *
		 * 		Source address number: 0X1234 1234 1234 1234
		 *
		 *
		 */


		// Kerlink GW Macs
		// AA66 6A00 0000 0000 Beaux Arts
		// AA66 6B00 0000 0001 Telecom B
		
		
		//Hex.decodeHex("41C8D1FAB1FFFF".toCharArray()); // Another way using commons
			

		byte databuff_802154_and_coap[] = {	 (byte)0x41,(byte)0xC8
									,(byte)0xD1
									,(byte)0xFA,(byte)0xB1
									,(byte)0xFF,(byte)0xFF
									,(byte)0xAA,(byte)0x66,(byte)0x6B,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x01
									,(byte)0x50,(byte)0x01,(byte)0x52,(byte)0x5d,(byte)0xb4,(byte)0x6e,(byte)0x5f,(byte)0x69,(byte)0x64
								};

	    //0x41 0xC8 0xD1 0x10 0x10 0xFF 0xFF 0x00 0x00 0x00 0x00 0x00 0x6A 0x66 0xAA 0x50 0x01 0x52 0x5D 0xB4 0x6E 0x5F 0x69 0x64

		int  databuff_802154_and_coap_size = databuff_802154_and_coap.length; //24;

		/* CoAP Packet  to send */
		/* Constrained Application Protocol, Non-Confirmable, GET, MID:21085 ( 0x525d )*/
		
		//50 01 52 5d b4 6e 5f 69 64 // Not used on this HardCodedVersion
		byte databuff_coap[] = {(byte)0x50,(byte)0x01,(byte)0x52,(byte)0x5d,(byte)0xb4,(byte)0x6e,(byte)0x5f,(byte)0x69,(byte)0x64};
		int  databuff_coap_size = 9;


		/* Packet payload size */
		json_beacon_as_string += ",\"size\":" + databuff_802154_and_coap_size ; //(databuff_802154_and_coap.length)
				
		/* Packet base64-encoded payload */
		json_beacon_as_string += ",\"data\":\"";
		
		json_beacon_as_string += Base64.encodeBase64String(databuff_802154_and_coap);
		
		json_beacon_as_string += "\"";
		
		
		/* End of packet serialization */
		json_beacon_as_string += "}}\0";
		
 				
		buff_up = ArrayUtils.addAll(buff_header, json_beacon_as_string.getBytes() ); // use first 4 bytes of buff_header and then concat json_beacon_as_string
				
		//buff_index = buff_up.length;
		//return buff_index;
		
		return buff_up;
	}

	
}
