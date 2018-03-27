package ipos.hashgraph.rest;

import com.txmq.exo.core.ExoPlatformLocator;
import com.txmq.exo.messaging.ExoMessage;
import io.swagger.annotations.Api;
import io.swagger.jaxrs.PATCH;
import ipos.hashgraph.IPOSAppState;
import ipos.hashgraph.model.Document;
import ipos.hashgraph.model.Documents;
import ipos.hashgraph.transaction.TransactionType;

import javax.print.Doc;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.Serializable;
import java.util.Optional;

@Path("/Hashgraph/1.0.0")
@Api(value = "/Hashgraph/1.0.0", description = "Documents endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DocumentApiImpl {

	@GET
	@Path("/endpoints")
	public Response getEndpoints() {
		IPOSAppState state = (IPOSAppState) ExoPlatformLocator.getPlatform().getState();
		return Response.ok().entity(state.getEndpoints()).build();
	}

	@GET
	@Path("/state")
    public Response getState() {
        IPOSAppState state = (IPOSAppState) ExoPlatformLocator.getPlatform().getState();
        return Response.ok().entity(state).build();
    }


	@GET
	@Path("/documents")
	public Response getDocuments() {
        IPOSAppState state = (IPOSAppState) ExoPlatformLocator.getPlatform().getState();
		if(state.getDocuments().size() == 0) {
		    return Response.ok().entity(state.getDocuments()).build();
        }

		return Response.ok().entity(state.getDocuments()).build();
	}

	@POST
	@Path("/document")
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

	@GET
	@Path("/verify/{hash}")
	public Response verifyDoc(@PathParam("hash") String hash) {
		IPOSAppState state = (IPOSAppState) ExoPlatformLocator.getPlatform().getState();
		Optional<ExoMessage> docHash = state.getDocuments().stream().filter(h -> {
            Document document = (Document) h.getPayload();
            return document.getHash().equals(hash);
		}).findAny();

		if(docHash.isPresent()) {
			return Response.ok().entity(docHash.get()).build();
		} else {
			return Response.status(Response.Status.BAD_REQUEST).entity("{\n" +
					"  \"error\":\"Not Found\"\n" +
					"}").build();
		}
	}

}
