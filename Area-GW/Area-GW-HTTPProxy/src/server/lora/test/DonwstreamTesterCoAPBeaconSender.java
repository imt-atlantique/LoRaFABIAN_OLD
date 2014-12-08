package server.lora.test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

import server.LoRaFabServer;
import server.lora.LoRAProtocol;
import server.lora.json.CoAPBeaconBuilder;

public class DonwstreamTesterCoAPBeaconSender implements Runnable {

	@Override
	public void run() {
		
		while( true ) // Need to Block if not I will make new Thread at each (fast) iteration
        {
			// Sleep 5 Secs
			//System.out.println("[down_tester] Going to sleep");
			int timetosleep = 10000 ; // 60000; // 1 min
			try { Thread.sleep(timetosleep); } catch (InterruptedException e) { e.printStackTrace(); }
			//System.out.println("[down_tester] Woke Up");
			
			
			
			InetSocketAddress inet_socket_addr = LoRaFabServer.lora_gw_route.inet_socket_down ;
			
			//lora_gw_route.inet_socket_down;
			
			// We need to send a Packet to a Remote Socket
			
			if (inet_socket_addr != null){
				
			
				try {
					
					//byte[] resp = {(byte)0x40 ,(byte)0x40 ,(byte)0x40 ,(byte)0x40 ,(byte)0x40 ,(byte)0x40 };
					
					byte[] resp = "La Rompes Pebete!!!".getBytes();
					resp = CoAPBeaconBuilder.generate_json_coap_beacon(null);
					
					DatagramPacket response_datagram = new DatagramPacket( resp, resp.length , inet_socket_addr);
					
					// We send the data
					System.out.println("[down_coap_beacon]<- Sending "+ resp.length + " bytes to : " + inet_socket_addr );
					LoRaFabServer.getDownstreamServerSocket().send(response_datagram);
					
					
				} catch (SocketException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			}
			
			
    		// response_data.length >= DOWNSTEAM_PROTOCOL_ACK_SIZE

			
        }

	}

}
