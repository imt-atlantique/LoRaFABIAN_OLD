package server.lora.json;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.ArrayUtils;


/*
 * 
 * 
 * 
 * 

Constrained Application Protocol, Non-Confirmable, GET, MID:60220
01.. .... = Version: 1
..01 .... = Type: Non-Confirmable (1)
.... 0000 = Token Length: 0
Code: GET (1)
Message ID: 60220 (0xeb3c)

Constrained Application Protocol, Non-Confirmable, GET, MID:60220
GET node_0001/led/on

5001eb3cb96e6f64655f30303031036c6564026f6e

5001eb3c
	b96e6f64655f30303031	// Opt Name: #1: Uri-Path: node_0001
	036c6564				// Opt Name: #2: Uri-Path: led
	026f6e					// Opt Name: #3: Uri-Path: off

--------------------------

GET node_0001/led/off

Constrained Application Protocol, Non-Confirmable, GET, MID:38287 (0x958f)
5001958fb96e6f64655f30303031036c6564036f6666

5001958f
	b96e6f64655f30303031	// Opt Name: #1: Uri-Path: node_0001
	036c6564				// Opt Name: #2: Uri-Path: led
	036f6666				// Opt Name: #3: Uri-Path: off
	
 * 
 * */
public class CoAPLedOn {
	
	static String freq = "870.000000";
	static String datr = "\"SF8BW250\"";
	static String codr = "\"4/6\"";

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
			Constrained Application Protocol, Non-Confirmable, GET, MID:60220 (0xeb3c)
			GET node_0001/led/on

			5001eb3cb96e6f64655f30303031036c6564026f6e

			5001eb3c
				b96e6f64655f30303031	// Opt Name: #1: Uri-Path: node_0001
				036c6564				// Opt Name: #2: Uri-Path: led
				026f6e					// Opt Name: #3: Uri-Path: off
*/

			//payload = ArrayUtils.addAll(payload, "COAPLEDON".getBytes() ); // CoAP Payload for testing ASCII
			byte[] coap_payload = Hex.decodeHex( ("5001eb3cb96e6f64655f30303031036c6564026f6e".toCharArray())) ;

			
			payload = ArrayUtils.addAll(payload ,  coap_payload ); //
			
			
		} catch (DecoderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		json_frame.setPayload(payload);
		
		return json_frame.get_json_packet();
		
		
	}

}
