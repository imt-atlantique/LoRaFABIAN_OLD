package server.lora;

import java.net.DatagramPacket;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.ArrayUtils;

import server.LoRaFabServer;
import server.coap.CoAPResourceLedON;
import server.coap.NodePathResource;
import server.coap.old.NodeServer;

public class LoRaProtocolUpstreamListener implements Runnable {

	@Override
	public void run() {
		
		try
	    {
			

			
	        while( true ) // Need to Block if not I will make new Thread at each (fast) iteration
	        {
	           
				//Create a NEW (!) DatagramPacket to receive one datagram packet from the downstreamServerSocket
	            DatagramPacket packet = new DatagramPacket( new byte[LoRaFabServer.UDP_PACKETSIZE], LoRaFabServer.UDP_PACKETSIZE ) ; 

	           // Receive a packet (BLOCKING!)
	           System.out.println("[up] Listening... "); 
	           LoRaFabServer.getUpstreamServerSocket().receive( packet ) ;
	           
	           byte[] payload = ArrayUtils.subarray(packet.getData(), 0, packet.getLength());
	           
	           // Print Info about the sender and the packet
	           System.out.println("[up]-> pkt in , host "+ packet.getAddress() +" (port "+ packet.getPort() +"), " + packet.getLength() + " bytes");
	           //System.out.println("[up] packet data HEX as STRING: " + Hex.encodeHexString( payload ) );
	           
	           String timeStamp = new SimpleDateFormat("yyyy/MM/dd_HH:mm:ss").format(Calendar.getInstance().getTime());
	           System.out.println("[up]\t Timestamp : " + timeStamp );
	           
	           
	           createCoAPResources();
	           
	           // Reply to the downstreamServerSocket with the corresponding response (AKC)
	           //new Thread(new DownstreamProtocolResponder(downstreamServerSocket, packet)).start();
	           byte[] resp = {(byte)0x40 ,(byte)0x40 ,(byte)0x40 ,(byte)0x40 ,(byte)0x40 ,(byte)0x40 };
	           packet.setData(resp);
	           
	           LoRaFabServer.getUpstreamServerSocket().send( packet );
	           // HERE WE SHOULD RESPOND WITH THE PROPER ACK!!!
	           
	           //upstreamServerSocket.send( packet ) ;
	       }  
	        
	    }catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	    }
		
	}
	
	
	private void createCoAPResources()
	{
		System.out.println("[up]\t WE CREATE COAP Server Resources for Node XX");
		
		
		// here all the resources of node might be created first time, and associate his name with a GATEWAY MAC (latter map GW mac with SOCKET)
		//LoRaFabServer.HTTPcoapProxy.targetServerA.add(new server.coap.CoAPResourceLedON("node_11_led_on"));
		LoRaFabServer.HTTPcoapProxy.targetServerA.add(new NodePathResource("node_0001"));
		//LoRaFabServer.HTTPcoapProxy.targetServerA.add(new NodeServer(1));
		
	}

}
