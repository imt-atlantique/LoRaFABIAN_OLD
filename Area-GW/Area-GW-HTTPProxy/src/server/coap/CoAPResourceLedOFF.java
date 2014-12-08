package server.coap;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.net.SocketException;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.server.resources.CoapExchange;

import server.LoRaFabServer;
import server.lora.json.CoAPBeaconBuilder;


/**
 * A simple resource that responds to GET requests with a small response
 * containing the resource's name.
 */
public class CoAPResourceLedOFF extends CoapResource {
	
	private int counter = 0;
	
	public CoAPResourceLedOFF(String name) {
		super(name);
	}
	
	@Override
	public void handleGET(CoapExchange exchange) {
		exchange.respond("SEND LED OFF request to LORA NET ("+(++counter)+") this resource is " + getName());
		//Do LORA*
		
		sendLoRADownstreamMessage();
	}

	/**
	 * 
	 */
	private void sendLoRADownstreamMessage() {
		
		// Here the socket down must be base on an ID of the node associated to the LORAGW (routing)
		InetSocketAddress inet_socket_addr = LoRaFabServer.lora_gw_route.inet_socket_down;
		
		// We need to send a Packet to a Remote Socket from our local downstream
		
		if (inet_socket_addr != null){
			
			try {
				
				//byte[] resp = {(byte)0x40 ,(byte)0x40 ,(byte)0x40 ,(byte)0x40 ,(byte)0x40 ,(byte)0x40 };
				
				byte[] resp = "LED OFF!! JSON 802154COAP y PAMELACHU La Rompes Pebete!!!\n".getBytes();
				resp = server.lora.json.CoAPLedOff.get_json_packet();
				//resp = CoAPBeaconBuilder.generate_json_coap_beacon(null);
				
				DatagramPacket response_datagram = new DatagramPacket( resp, resp.length , inet_socket_addr);
				
				// We send the data
				System.out.println("[CoAPResourceLedOFF]<- Sending "+ resp.length + " bytes to : " + inet_socket_addr );
				LoRaFabServer.getDownstreamServerSocket().send(response_datagram);
				
				
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		}else{
			System.out.println("[CoAPResourceLedOFF] ERROR there is no downstream protocol socket");
		}
			
	}
	
}