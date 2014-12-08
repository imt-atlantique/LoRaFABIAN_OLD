package server;


import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import server.lora.LoRaProtocolDownstreamListener;
import server.lora.LoRaProtocolUpstreamListener;
import server.lora.routing.LoraGWRoute;
import server.lora.test.DonwstreamTesterCoAPBeaconSender;
import server.proxy.HTTPCoAPCrossProxy;

public class LoRaFabServer {

	//*= This might be suplanted by a list of all lora gw routes
	public static LoraGWRoute lora_gw_route = new LoraGWRoute();
	
	public final static int UDP_PACKETSIZE = 4096 ;
	
	private static int UPSTREAM_PROTOCOL_UDP_PORT	= 1790;
	private static int DOWNSTEAM_PROTOCOL_UDP_PORT	= 1792;
	
	
	private static DatagramSocket downstreamServerSocket = null;
	private static DatagramSocket upstreamServerSocket = null;
	
	public static DatagramSocket getDownstreamServerSocket() {
		return downstreamServerSocket;
	}

	public static DatagramSocket getUpstreamServerSocket() {
		return upstreamServerSocket;
	}
	
	
	public static HTTPCoAPCrossProxy HTTPcoapProxy;

	
	
	public static void main(String[] args) {
		
		try {
			
			bind_sockets();
		
			// Check java.net.BindException: Address already in use !!! We might EXIT
			System.out.println( "The LoRaFab Server is ready" ) ;
			
			//  echo -ne '\x01\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00' | nc -u localhost 1792
			
			(new Thread(new LoRaProtocolUpstreamListener())).start();	// UPSTREAM 	LISTENER THREAD
			(new Thread(new LoRaProtocolDownstreamListener())).start(); // DOWNSTREAM	LISTENER THREAD
			
			(new Thread(new DonwstreamTesterCoAPBeaconSender())).start(); // DOWNSTREAM	TESTEr THREAD
			
			
			try {
				HTTPcoapProxy = new HTTPCoAPCrossProxy();
				
			} catch (IOException e) {
				
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		
		} catch (SocketException | UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println( "Exception: Exiting Application" ) ;
			
		}
		
	}

	/**
	 * @throws UnknownHostException 
	 * @throws SocketException 
	 * 
	 */
	private static void bind_sockets() throws SocketException, UnknownHostException {

			// InetAddress laddr_ipv4 = InetAddress.getLoopbackAddress(); // I dont know if its ipv4 or ipv6
			
			String bindAddress="0.0.0.0"; // Making sure it will use IPv4
			
			downstreamServerSocket 	= new DatagramSocket(DOWNSTEAM_PROTOCOL_UDP_PORT	, InetAddress.getByName(bindAddress));
			upstreamServerSocket 	= new DatagramSocket(UPSTREAM_PROTOCOL_UDP_PORT		, InetAddress.getByName(bindAddress));
			
			
		
	}

}
