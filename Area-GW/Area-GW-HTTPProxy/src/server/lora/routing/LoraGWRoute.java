package server.lora.routing;

import java.net.InetAddress;
import java.net.InetSocketAddress;

public class LoraGWRoute {
	
	public String gateway_mac;
	//private byte[] gw_mac = new byte[8]; /* MAC address of the client (gateway) */
	
	// Two possible solution: Two Sockets, or one IP + 2 port numbers
	public InetSocketAddress inet_socket_up = null;
	public InetSocketAddress inet_socket_down = null;
	
	public InetAddress ip_addr;
	public int 		port_up;
	public int 		port_down;
	

}
