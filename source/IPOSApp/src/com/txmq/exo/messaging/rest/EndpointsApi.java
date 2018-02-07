package com.txmq.exo.messaging.rest;

import com.txmq.exo.core.ExoPlatformLocator;
import com.txmq.exo.messaging.ExoMessage;
import com.txmq.socketdemo.SocketDemoState;
import com.txmq.socketdemo.SocketDemoTransactionTypes;
import ipos.hashgraph.model.Document;
import ipos.hashgraph.model.Documents;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Path("/Hashgraph/1.0.0")
public class EndpointsApi {

	@GET
	@Path("/endpoints")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getEndpoints() {
		SocketDemoState state = (SocketDemoState) ExoPlatformLocator.getPlatform().getState();
		return Response.ok().entity(state.getEndpoints()).build();
	}

	@GET
    @Path("/state")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getState() {
        SocketDemoState state = (SocketDemoState) ExoPlatformLocator.getPlatform().getState();
        return Response.ok().entity(state).build();
    }


	@GET
	@Path("/documents")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDocuments() {
		SocketDemoState state = (SocketDemoState) ExoPlatformLocator.getPlatform().getState();
		Documents result = new Documents();
		if(state.getDocuments().size() == 0) {
		    return Response.ok().entity(result).build();
        }
		result.setHash(state.getDocuments());
		return Response.ok().entity(result).build();
	}

	@POST
	@Path("/document")
	@Produces(MediaType.APPLICATION_JSON)
	public Response addDocument(Document document) {
		ExoMessage message = new ExoMessage(new SocketDemoTransactionTypes(SocketDemoTransactionTypes.ADD_DOC), document);
        boolean transactionStatus = false;
		try {
		    transactionStatus = ExoPlatformLocator.getPlatform().createTransaction(message.serialize(), null);
        } catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Response.status(201).entity("{\n" +
                "  \"result\":"+transactionStatus+"\n" +
                "}").build();
	}

}
