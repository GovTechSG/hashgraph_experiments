package com.txmq.exo.messaging.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.txmq.exo.core.ExoPlatformLocator;
import com.txmq.socketdemo.SocketDemoState;

/**
 * This class implements a REST endpoint for retrieving a list of endpoints that the Swirld
 * exposes.  Endpoints self-report by issuing an ExoMessage, which is logged in state.
 * 
 */
@Path("/HashgraphZoo/1.0.0") //TODO:  Remove HashgraphZoo prefix, give the internal APIs their own
public class EndpointsApi {
	@GET
	@Path("/endpoints")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getEndpoints() {
		SocketDemoState state = (SocketDemoState) ExoPlatformLocator.getPlatform().getState();
		return Response.ok().entity(state.getEndpoints()).build();
	}
	
}
