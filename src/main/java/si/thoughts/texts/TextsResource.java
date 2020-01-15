package si.thoughts.texts;

import com.kumuluz.ee.logs.cdi.Log;

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
public class TextsResource {
    @Inject
    private ConfigurationProperties cfg;

    @GET
    @Path("info")
    public Response getInfo(){
        String result = "Database url: " + cfg.getDbUrl() + " | "
                + "User: " + cfg.getDbUser() + " | " + "Password: " +cfg.getDbPassword();
        return Response.ok(result).build();
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
}
