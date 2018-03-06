package ipos.hashgraph.rest;

import io.swagger.annotations.Api;
import ipos.hashgraph.model.Document;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/Hashgraph/1.0.0")
@Api(value = "/Hashgraph/1.0.0", description = "Documents endpoints")
public interface DocumentApi {

    @GET
    @Path("/endpoints")
    @Produces(MediaType.APPLICATION_JSON)
    Response getEndpoints();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/state")
    Response getState();

    @GET
    @Path("/documents")
    @Produces(MediaType.APPLICATION_JSON)
    Response getDocuments();

    @POST
    @Path("/document")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response addDocument(Document document);

}
