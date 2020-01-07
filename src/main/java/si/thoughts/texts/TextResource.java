package si.thoughts.texts;

import com.kumuluz.ee.logs.cdi.Log;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)

@Path("texts")
@Log
public class TextResource {
    @Inject
    private ConfigurationProperties cfg;

    @Inject
    @RestClient
    private RatingsRestService ratingsRestService;

    @Inject
    private CommentsGrpcService commentsGrpcService;

    @POST
    public Response addText(@QueryParam("title") String title,
                            @QueryParam("content") String content){
        try (
                Connection con = DriverManager.getConnection(cfg.getDbUrl(), cfg.getDbUser(), cfg.getDbPassword());
                Statement stmt = con.createStatement();
        ) {
            stmt.executeUpdate("INSERT INTO texts (title, content, created) VALUES ("
                    + title + ", " + content + ",now())");
        }
        catch (SQLException e) {
            System.err.println(e);
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        return Response.ok().build();
    }

    @Path("info")
    @GET
    public Response getInfo(){
        String result = cfg.getDbUrl() + " | " + cfg.getDbUser() + " | " + cfg.getDbPassword();
        return Response.ok(result).build();
    }

    @DELETE
    public Response deleteText(@QueryParam("textId") int textId){
        try (
                Connection con = DriverManager.getConnection(cfg.getDbUrl(), cfg.getDbUser(), cfg.getDbPassword());
                Statement stmt = con.createStatement();
        ){
            stmt.executeUpdate("DELETE FROM texts WHERE id = " + textId);

            commentsGrpcService.commentCleanUp(textId);

        }catch (SQLException e){
            System.err.println(e);
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        return Response.ok().build();
    }

    @GET
    public Response getTexts(){
        List<Text> texts = new LinkedList<Text>();

        try(
                Connection con = DriverManager.getConnection(cfg.getDbUrl(),cfg.getDbUser(),cfg.getDbPassword());
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM texts");
        ){
            while(rs.next()){
                Text text = new Text();
                text.setId(rs.getInt(1));
                text.setTitle(rs.getString(2));
                text.setContent(rs.getString(3));
                text.setCreated(rs.getTimestamp(4).toInstant());
                texts.add(text);
            }
        }
        catch(SQLException e){
            System.err.println(e);
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        return Response.ok(texts).build();
    }

    @GET
    @Path("{textId}")
    public Response getAverageRating(@PathParam("textId") int textId){
        return ratingsRestService.getAverageRating(textId);
    }
}
