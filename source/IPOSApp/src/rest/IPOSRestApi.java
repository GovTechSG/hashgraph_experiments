package rest;

import com.txmq.exo.core.ExoPlatformLocator;
import com.txmq.exo.messaging.ExoMessage;
import com.txmq.socketdemo.SocketDemoState;
import com.txmq.socketdemo.SocketDemoTransactionTypes;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Path("/HashgraphIPOS/1.0.0")
public class IPOSRestApi {
	@GET
	@Path("/documents")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getZoo() {
		SocketDemoState state = (SocketDemoState) ExoPlatformLocator.getState();
		
		return Response.ok().entity(state).build();
	}
	
	@POST
	@Path("/documents/{value}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response addAnimal(String string) {
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
