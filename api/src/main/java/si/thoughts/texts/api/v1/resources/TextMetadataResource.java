package si.thoughts.texts.api.v1.resources;

import si.thoughts.texts.lib.TextMetadata;
import si.thoughts.texts.services.beans.TextMetadataBean;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;

@ApplicationScoped
@Path("/texts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TextMetadataResource {

    @Inject
    private TextMetadataBean textMetadataBean;

    @Context
    protected UriInfo uriInfo;

    @GET
    public Response getTextMetadata() {

        List<TextMetadata> textMetadata = textMetadataBean.getTextMetadataFilter(uriInfo);

        return Response.status(Response.Status.OK).entity(textMetadata).build();
    }

    @GET
    @Path("/{textMetadataId}")
    public Response getTextMetadata(@PathParam("textMetadataId") Integer textMetadataId) {

        TextMetadata textMetadata = textMetadataBean.getTextMetadata(textMetadataId);

        if (textMetadata == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.OK).entity(textMetadata).build();
    }

    @POST
    public Response createTextMetadata(TextMetadata textMetadata) {

        if ((textMetadata.getTitle() == null || textMetadata.getDescription() == null)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            textMetadata = textMetadataBean.createTextMetadata(textMetadata);
        }

        return Response.status(Response.Status.CONFLICT).entity(textMetadata).build();

    }

    @PUT
    @Path("{textMetadataId}")
    public Response putTextMetadata(@PathParam("textMetadataId") Integer textMetadataId,
                                     TextMetadata textMetadata) {

        textMetadata = textMetadataBean.putTextMetadata(textMetadataId, textMetadata);

        if (textMetadata == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.NOT_MODIFIED).build();

    }

    @DELETE
    @Path("{textMetadataId}")
    public Response deleteTextMetadata(@PathParam("textMetadataId") Integer textMetadataId) {

        boolean deleted = textMetadataBean.deleteTextMetadata(textMetadataId);

        if (deleted) {
            return Response.status(Response.Status.NO_CONTENT).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }


}
