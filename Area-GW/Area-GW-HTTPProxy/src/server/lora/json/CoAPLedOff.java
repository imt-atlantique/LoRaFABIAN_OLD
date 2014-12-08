package server.lora.json;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.ArrayUtils;

public class CoAPLedOff {

	
	public static byte[] get_json_packet(){
		
		//ArrayUtils.addAll(buff_header, json_beacon_as_string.getBytes() )
		
		GenericJsonDownstreamFrame json_frame = new GenericJsonDownstreamFrame();
		
		byte[] payload = null;
		
		try {
			payload = Hex.decodeHex(( "41C8" 				// Frame control: 0x41C8 (specific, read IEEE 802.15.4)
					+ "D1"					// Sequence Number: NOT USED (could be any)
					+ "FAB1"				// Destination PAN ID:  0xFAB1 (LoRA Fabian)
					+ "0001"				// Destination address: 0x0001 (NODE 01) Source PAN ID:  (NULL)
					+ "AA666B0000000001"	// Source address: 0X1234 1234 1234 1234 // MUst be the GW MAC Address..
					).toCharArray());
			
			/*
			 * 	GET node_0001/led/off

				Constrained Application Protocol, Non-Confirmable, GET, MID:38287 (0x958f)
				5001958fb96e6f64655f30303031036c6564036f6666
				
				5001958f
					b96e6f64655f30303031	// Opt Name: #1: Uri-Path: node_0001
					036c6564				// Opt Name: #2: Uri-Path: led
					036f6666				// Opt Name: #3: Uri-Path: off
	
			 * */
			//payload = ArrayUtils.addAll(payload, "COAPLEDOFF".getBytes() ); /// CoAP Payload :"5001525db46e5f6964".getBytes()
			byte[] coap_payload = Hex.decodeHex( ("5001958fb96e6f64655f30303031036c6564036f6666".toCharArray())) ;

			
			payload = ArrayUtils.addAll(payload ,  coap_payload ); //
			
			
		} catch (DecoderException e) { e.printStackTrace(); }
		
		json_frame.setPayload(payload);
		
		return json_frame.get_json_packet();
		
	}

}
