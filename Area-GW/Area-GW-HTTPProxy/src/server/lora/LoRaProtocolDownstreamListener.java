package server.lora;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.Arrays;

import org.apache.commons.codec.binary.Hex;

import server.LoRaFabServer;

public class LoRaProtocolDownstreamListener implements Runnable  {
	
	/* variables for protocol management */
	
	//uint32_t raw_mac_h; /* Most Significant Nibble, network order */
	//uint32_t raw_mac_l; /* Least Significant Nibble, network order */
	//uint64_t gw_mac; /* MAC address of the client (gateway) */
	//uint8_t ack_command;
	
	private static byte[] raw_mac_h  = new byte[4]; /* Most Significant Nibble, network order */
	private static byte[] raw_mac_l  = new byte[4]; /* Least Significant Nibble, network order */
	private static byte[] gw_mac	 = new byte[8]; /* MAC address of the client (gateway) */
	private static byte[] ack_command= new byte[1];
	
	/* ************************************ */

	@Override
	public void run() {
		
		try
	    {
	        while( true ) // Need to Block if not I will make new Thread at each (fast) iteration
	        {
	           
	        	// Create a NEW (!) DatagramPacket to receive one datagram packet from the downstreamServerSocket
	            DatagramPacket packet = new DatagramPacket( new byte[LoRaFabServer.UDP_PACKETSIZE], LoRaFabServer.UDP_PACKETSIZE ) ;

	           // Receive a packet (BLOCKING!)
	            LoRaFabServer.getDownstreamServerSocket().receive( packet ) ;

	
	           // Print Info about the sender and the packet
	           System.out.print("[down]-> pkt in , host "+ packet.getAddress() +" (port "+ packet.getPort() +"), " + packet.getLength() + " bytes");
	
	           // Reply to the downstreamServerSocket with the corresponding response (AKC)
	           new Thread(new DownstreamProtocolResponder(LoRaFabServer.getDownstreamServerSocket(), packet)).start();
	        
	           //upstreamServerSocket.send( packet ) ;
	           
	       }  
	    }catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	    }
		
	}
	
	private static class DownstreamProtocolResponder implements Runnable {

		DatagramSocket socket = null;
	    DatagramPacket packet = null;

	    public DownstreamProtocolResponder(DatagramSocket socket, DatagramPacket packet) {
	        this.socket = socket;
	        this.packet = packet;
	    }

	    public void run() {
	        
	    	byte[] response_data = makeACKResponse(packet); // Make the ACK packet.
	    	
	    	if (response_data != null){
	    		
	    		DatagramPacket response_datagram = new DatagramPacket(response_data, LoRAProtocol.PKT_ACK_SIZE , packet.getAddress(), packet.getPort());
	    		// response_data.length >= DOWNSTEAM_PROTOCOL_ACK_SIZE
	    		
		        try {
		        	
					socket.send(response_datagram);
					System.out.println(", "+ response_data.length +" bytes sent"); // To be coorect we should print  DOWNSTEAM_PROTOCOL_ACK_SIZE
					
					// *******************************
					// Here we sent another datagram containing the coap_beacon on json format.
					
					/*int sizeof_coap_pull_resp = generate_json_coap_beacon( gw_mac ); // will be a PULL_RESP packet!
					printf("This is Json we will sendi: %s \n", (buff_up + 4) );
					// Here we should put another timer to send the coap beacons, not to send them with same period as the PULL_ACKs
					byte_nb = sendto(sock, (void *)buff_up, sizeof_coap_pull_resp, 0, (struct sockaddr *)&dist_addr, addr_len);
					*/
					
					
					/*// Temporarly Disable CoAP Beacon from here!!
					byte[] byte_coap_beacon = CoAPBeaconBuilder.generate_json_coap_beacon(null); 
					DatagramPacket response_coap_beacon_datagram = new DatagramPacket(byte_coap_beacon, byte_coap_beacon.length , packet.getAddress(), packet.getPort());
					socket.send(response_coap_beacon_datagram);
					System.out.print(", plus coap beacon "+ byte_coap_beacon.length +" bytes sent\n"); // To be coorect we should print  DOWNSTEAM_PROTOCOL_ACK_SIZE
					*/
					
					
				} catch (IOException e) {
					System.out.print(", send error: " + e.getMessage() + "\n");
					//e.printStackTrace();
				}
	    		
	    	}
	    	
	        
	    }

	    
		private byte[] makeACKResponse(DatagramPacket packet) {
			
			
			int byte_nb = packet.getLength();
			byte[] databuf = packet.getData();
					
			/* check and parse the payload */
			if (byte_nb < 12) { /* not enough bytes for packet from gateway */
				System.out.print(" (too short for GW <-> MAC protocol)\n");
				return null;//continue;
			}
			
			/* don't touch the token in position 1-2, it will be sent back "as is" for acknowledgement */
			if (databuf[0] != LoRAProtocol.PROTOCOL_VERSION) { /* check protocol version number */
				System.out.print(", invalid version " + databuf[0] +"\n");
				return null;//continue;
			}
			
			//raw_mac_h = *((uint32_t *)(databuf+4));
			//raw_mac_l = *((uint32_t *)(databuf+8));
			//gw_mac    = ((uint64_t)ntohl(raw_mac_h) << 32) + (uint64_t)ntohl(raw_mac_l);
			
			gw_mac = Arrays.copyOfRange(databuf, 4 , 12);

			/* interpret gateway command */
			switch (databuf[3]) {
				case LoRAProtocol.PKT_PUSH_DATA:
					//System.out.println(", PUSH_DATA from gateway 0x%08X%08X\n", (uint32_t)(gw_mac >> 32), (uint32_t)(gw_mac & 0xFFFFFFFF));
					System.out.print(", PUSH_DATA from gateway (net byte order not converted) 0x" + Hex.encodeHexString(gw_mac) + " \n");
					ack_command[0] = LoRAProtocol.PKT_PUSH_ACK;
					//System.out.println("<-  pkt out, PUSH_ACK for host %s (port %s)", host_name, port_name);
					System.out.print("[down]<-  pkt out, PUSH_ACK for (host:port) "+ packet.getSocketAddress() + " " );
					break;
				case LoRAProtocol.PKT_PULL_DATA:
					//System.out.println(", PULL_DATA from gateway 0x%08X%08X\n", (uint32_t)(gw_mac >> 32), (uint32_t)(gw_mac & 0xFFFFFFFF));
					System.out.print(", PULL_DATA from gateway (byte order not checked) 0x" + Hex.encodeHexString(gw_mac) + " \n");
					ack_command[0] = LoRAProtocol.PKT_PULL_ACK;
					
					// ATTENTION complete Code!!
					// This is a correct PULL_DATA packet, hence we add this GW / Socket
					LoRaFabServer.lora_gw_route.gateway_mac = Hex.encodeHexString(gw_mac);
					LoRaFabServer.lora_gw_route.inet_socket_down = (InetSocketAddress) packet.getSocketAddress();
					
					
					//System.out.println("<-  pkt out, PULL_ACK for host %s (port %s)", host_name, port_name);
					System.out.print("[down]<-  pkt out, PULL_ACK for host:port "+ packet.getSocketAddress() + " )" );
					break;
				default:
					System.out.print(", unexpected command (byte as int): " + Byte.toString(databuf[3]) +"\n");
					
					return null;//continue;
			}
			
			/* add some artificial latency */
			//usleep(30000); /* 30 ms */
			try { Thread.sleep(30); } catch (InterruptedException e) { e.printStackTrace(); }

			/* send acknowledge and check return value */
			databuf[3] = ack_command[0];
			//byte_nb = sendto(sock, (void *)databuf, 4, 0, (struct sockaddr *)&dist_addr, addr_len);
			/*if (byte_nb == -1) {
				System.out.println(", send error:%s\n", strerror(errno));
			} else {
				System.out.println(", %i bytes sent\n", byte_nb);
			}*/
			
			//byte[] myvar = "Any String you want\n".getBytes();
			//return myvar;
			
			
			databuf = Arrays.copyOfRange(databuf, 0 , LoRAProtocol.PKT_ACK_SIZE);
			
			return databuf;
			
		}
	
	
	}

}
