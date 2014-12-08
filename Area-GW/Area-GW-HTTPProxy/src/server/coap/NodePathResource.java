package server.coap;


import static org.eclipse.californium.core.coap.CoAP.ResponseCode.*;
import static org.eclipse.californium.core.coap.MediaTypeRegistry.*;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.LinkFormat;
import org.eclipse.californium.core.server.resources.CoapExchange;

/**
 * This resource implements a test of specification for the ETSI IoT CoAP Plugtests, London, UK, 7--9 Mar 2014.
 */
public class NodePathResource extends CoapResource {

	public NodePathResource(String name) {
		
		super(name);
		getAttributes().setTitle("Hierarchical link description entry");
		getAttributes().addContentType(APPLICATION_LINK_FORMAT);
		
		PathSub led = new PathSub("led"); 
		led.add(new CoAPResourceLedON("on"));
		led.add(new CoAPResourceLedOFF("off"));
		
		add(led);
	}
	
	@Override
	public void handleGET(CoapExchange exchange) {
		exchange.respond(CONTENT, LinkFormat.serializeTree(this), APPLICATION_LINK_FORMAT);
	}

}