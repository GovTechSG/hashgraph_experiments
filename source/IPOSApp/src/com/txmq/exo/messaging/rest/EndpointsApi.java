package com.txmq.exo.messaging.rest;

import com.txmq.exo.core.ExoPlatformLocator;
import com.txmq.exo.messaging.ExoMessage;
import com.txmq.socketdemo.SocketDemoState;
import com.txmq.socketdemo.SocketDemoTransactionTypes;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * This class implements a REST endpoint for retrieving a list of endpoints that the Swirld
 * exposes.  Endpoints self-report by issuing an ExoMessage, which is logged in state.
 * 
 */
@Path("/Hashgraph/1.0.0")
public class EndpointsApi {

	@GET
	@Path("/endpoints")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getEndpoints() {
        System.out.println("In here-------------------");
		SocketDemoState state = (SocketDemoState) ExoPlatformLocator.getPlatform().getState();
		return Response.ok().entity(state.getEndpoints()).build();
	}


	@GET
	@Path("/documents")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDocuments() {
		SocketDemoState state = (SocketDemoState) ExoPlatformLocator.getState();

		return Response.ok().entity(state).build();
	}

	@POST
	@Path("/document/{string}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response addDocument(@PathParam("string") String string) {
		ExoMessage message = new ExoMessage(new SocketDemoTransactionTypes(SocketDemoTransactionTypes.ADD_DOC), string);

		try {
			ExoPlatformLocator.getPlatform().createTransaction(message.serialize(), null);
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Response.status(201).entity(string).build();
	}
	
}
