package server.proxy;

import java.io.IOException;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.network.config.NetworkConfig;
import org.eclipse.californium.core.network.config.NetworkConfigDefaults;
import org.eclipse.californium.core.server.resources.CoapExchange;

import org.eclipse.californium.proxy.DirectProxyCoAPResolver;
import org.eclipse.californium.proxy.ProxyHttpServer;
import org.eclipse.californium.proxy.resources.ForwardingResource;
import org.eclipse.californium.proxy.resources.ProxyCoapClientResource;
import org.eclipse.californium.proxy.resources.ProxyHttpClientResource;

/**
 * Http2CoAP: Insert in browser:
 *     URI: http://localhost:8080/proxy/coap://localhost:PORT/target
 * 
 * CoAP2CoAP: Insert in Copper:
 *     URI: coap://localhost:PORT/coap2coap
 *     Proxy: coap://localhost:PORT/targetA
 *
 * CoAP2Http: Insert in Copper:
 *     URI: coap://localhost:PORT/coap2http
 *     Proxy: http://lantersoft.ch/robots.txt
 */
public class HTTPCoAPCrossProxy {
	
	private static final int PORT = NetworkConfig.getStandard().getInt(NetworkConfigDefaults.DEFAULT_COAP_PORT);

	public CoapServer targetServerA;
	
	public HTTPCoAPCrossProxy() throws IOException {
		ForwardingResource coap2coap = new ProxyCoapClientResource("coap2coap");
		ForwardingResource coap2http = new ProxyHttpClientResource("coap2http");
		
		// Create CoAP Server on PORT with proxy resources form CoAP to CoAP and HTTP
		targetServerA = new CoapServer(PORT);
		targetServerA.add(coap2coap);
		targetServerA.add(coap2http);
		//targetServerA.add(new TargetResource("target"));
		targetServerA.start();
		
		ProxyHttpServer httpServer = new ProxyHttpServer(8080);
		
		httpServer.setProxyCoapResolver(new DirectProxyCoAPResolver(coap2coap));
		
		System.out.println("CoAP resource \"target\" available over HTTP at: http://localhost:8080/proxy/coap://localhost:PORT/target");
	}
	
	/**
	 * A simple resource that responds to GET requests with a small response
	 * containing the resource's name.
	 */
	private static class TargetResource extends CoapResource {
		
		private int counter = 0;
		
		public TargetResource(String name) {
			super(name);
		}
		
		@Override
		public void handleGET(CoapExchange exchange) {
			exchange.respond("Response "+(++counter)+" from resource " + getName());
		}
	}
	

}
