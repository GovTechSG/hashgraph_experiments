package ipos.hashgraph.rest;

import com.txmq.exo.core.ExoPlatformLocator;
import com.txmq.exo.messaging.ExoMessage;
import io.swagger.annotations.Api;
import ipos.hashgraph.IPOSAppState;
import ipos.hashgraph.model.Document;
import ipos.hashgraph.model.Documents;
import ipos.hashgraph.transaction.TransactionType;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;

public class DocumentApiImpl  implements DocumentApi{

	public Response getEndpoints() {
		IPOSAppState state = (IPOSAppState) ExoPlatformLocator.getPlatform().getState();
		return Response.ok().entity(state.getEndpoints()).build();
	}


    public Response getState() {
        IPOSAppState state = (IPOSAppState) ExoPlatformLocator.getPlatform().getState();
        return Response.ok().entity(state).build();
    }


	public Response getDocuments() {
        IPOSAppState state = (IPOSAppState) ExoPlatformLocator.getPlatform().getState();
		Documents result = new Documents();
		if(state.getDocuments().size() == 0) {
		    return Response.ok().entity(result).build();
        }

		result.setHash(state.getDocuments());
		return Response.ok().entity(result).build();
	}

	public Response addDocument(Document document) {
		ExoMessage message = new ExoMessage(new TransactionType(TransactionType.ADD_DOC), document);
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
