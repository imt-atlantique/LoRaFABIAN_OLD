package server.lora.json;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.ArrayUtils;

import server.lora.LoRAProtocol;

public class GenericJsonDownstreamFrame {
	
	
	public String frequency = "870.000000";
	public String datarate = "\"SF8BW250\"";
	public String codeerror = "\"4/6\"";
	
	private byte payload[] = null;
	
	
	public byte[] getPayload() {
		return payload;
	}


	public void setPayload(byte[] payload) {
		this.payload = payload;
	}


	public byte[] get_json_packet(  ){ // byte[] gw_mac and  node_nr objetc with node mac and gw mac minimum information needed
		
		byte[] buff_header  = new byte[4];
		buff_header[0] = LoRAProtocol.PROTOCOL_VERSION;
		buff_header[3] = LoRAProtocol.PKT_PULL_RESP;


		/* start of JSON structure */
		
		String json_beacon_as_string= "{\"txpk\":{"
									+  "\"imme\":true"
									+ ",\"freq\":" + frequency
									+ ",\"rfch\":0"
									+ ",\"powe\":14"
									+ ",\"modu\":\"LORA\""
									+ ",\"datr\":" + datarate
									+ ",\"codr\":" + codeerror
									+ ",\"ipol\":false"
									;
		
		
		/*
		try {
			
			payload = Hex.decodeHex(( "41C8" 				// Frame control: 0x41C8 (specific, read IEEE 802.15.4)
									+ "D1"					// Sequence Number: NOT USED (could be any)
									+ "FAB1"				// Destination PAN ID:  0xFAB1 (LoRA Fabian)
									+ "FFFF"				// Destination address: 0xFFFF (Broadcast) Source PAN ID:  (NULL)
									+ "AA666B0000000001"	// Source address: 0X1234 1234 1234 1234
									+ "5001525db46e5f6964"	// CoAP Payload
					).toCharArray());
		
		
			payload = "La rompes pebete 802154 + CoAP LED ON".getBytes();
			
		} catch (DecoderException e) {	e.printStackTrace();}
	    */

		int  payload_size = payload.length; 

		/* Packet payload size */
		json_beacon_as_string += ",\"size\":" + payload_size ; // the lenght of the payload (plain text-unencoded)
		
		/* Packet base64-encoded payload */
		json_beacon_as_string += ",\"data\":\"";
		
		json_beacon_as_string += Base64.encodeBase64String(payload); // ENCODED DATA
		
		json_beacon_as_string += "\"";
		
		/* End of packet serialization */
		json_beacon_as_string += "}}\0";
		
				
		return ArrayUtils.addAll(buff_header, json_beacon_as_string.getBytes() ); // use first 4 bytes of buff_header and then concat json_beacon_as_string;
		

	}
	
	
}
