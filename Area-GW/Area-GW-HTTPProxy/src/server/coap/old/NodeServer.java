package server.coap.old;


import static org.eclipse.californium.core.coap.CoAP.ResponseCode.*;
import static org.eclipse.californium.core.coap.MediaTypeRegistry.*;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.Request;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.eclipse.californium.core.server.resources.Resource;

/**
 * This resource implements a test of specification for the
 * ETSI IoT CoAP Plugtests, London, UK, 7--9 Mar 2014.
 */
public class NodeServer extends CoapResource {

	public NodeServer(int node_nr) {
		//this(node_nr.to);
		//Integer.par
		super("node_" + Integer.toString(node_nr));
		
		

		Resource seg2 = new NodeServer("led");
		Resource seg2_1 = new NodeServer("on");
		Resource seg2_2 = new NodeServer("off");

		add(seg2);
		seg2.add(seg2_1);
		seg2.add(seg2_2);
	}
	
	public NodeServer(String name) {
		super(name);
		getAttributes().setTitle("Long path resource");
	}

	@Override
	public void handleGET(CoapExchange exchange) {
		Request request = exchange.advanced().getRequest();
		
		String payload = String.format("Long path resource\n" +
									   "Type: %d (%s)\nCode: %d (%s)\nMID: %d",
									   request.getType().value,
									   request.getType(),
									   request.getCode().value,
									   request.getCode(),
									   request.getMID()
									  );
		
		// complete the request
		exchange.respond(CONTENT, payload, TEXT_PLAIN);
	}
}