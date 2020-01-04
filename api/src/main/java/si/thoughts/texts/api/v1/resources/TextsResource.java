package si.thoughts.texts.api.v1.resources;

import com.kumuluz.ee.logs.cdi.Log;
import org.eclipse.microprofile.metrics.annotation.Counted;
import si.thoughts.texts.lib.Text;
import si.thoughts.texts.services.TextsBean;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Log
@ApplicationScoped
@Path("/comments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TextsResource {

    @Inject
    private TextsBean textsBean;

    @GET
    @Counted
    public Response getTexts(@QueryParam("textId") Integer textId) {

        List<Text> texts;
        texts = textsBean.getTexts();

        return Response.ok(texts).build();
    }

    @GET
    @Path("count")
    public Response getTextsCount() {

        List<Text> texts;
        texts = textsBean.getTexts();

        return Response.ok(texts.size()).build();
    }
}
