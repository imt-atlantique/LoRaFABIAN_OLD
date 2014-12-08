package server.coap;


import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.server.resources.CoapExchange;

/**
 * This resource implements a test of specification for the ETSI IoT CoAP Plugtests, London, UK, 7--9 Mar 2014.
 */
public class PathSub extends CoapResource {

	public PathSub(String name) {
		super(name);
		getAttributes().setTitle("Hierarchical link description sub-resource");
	}
	 
	@Override
	public void handleGET(CoapExchange exchange) {
		exchange.respond(this.getURI() + this.getChildren());
		
	}

}