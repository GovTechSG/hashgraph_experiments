package com.txmq.exo.messaging.rest;

import com.txmq.exo.core.ExoPlatformLocator;
import com.txmq.exo.messaging.ExoMessage;
import com.txmq.socketdemo.IPOSState;
import com.txmq.socketdemo.SocketDemoTransactionTypes;
import ipos.hashgraph.model.Document;
import ipos.hashgraph.model.Documents;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Path("/Hashgraph/1.0.0")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class DocumentApi {

	@GET
	@Path("/endpoints")
	public Response getEndpoints() {
		IPOSState state = (IPOSState) ExoPlatformLocator.getPlatform().getState();
		return Response.ok().entity(state.getEndpoints()).build();
	}

	@GET
    @Path("/state")
    public Response getState() {
        IPOSState state = (IPOSState) ExoPlatformLocator.getPlatform().getState();
        return Response.ok().entity(state).build();
    }


	@GET
	@Path("/documents")
	public Response getDocuments() {
        IPOSState state = (IPOSState) ExoPlatformLocator.getPlatform().getState();
		Documents result = new Documents();
		if(state.getDocuments().size() == 0) {
		    return Response.ok().entity(result).build();
        }

		result.setHash(state.getDocuments());
		return Response.ok().entity(result).build();
	}

	@POST
	@Path("/document")
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
