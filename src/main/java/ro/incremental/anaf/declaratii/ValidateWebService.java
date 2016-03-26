package ro.incremental.anaf.declaratii;

/**
 * Created by Alex Proca <alex.proca@gmail.com> on 18/03/16.
 */

import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/form")
public class ValidateWebService {

    @GET
    @Path("/available")
    @Produces(MediaType.TEXT_PLAIN)
    public String available() {
        return "yes";
    }

    @POST
    @Path("/validate")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String available(JSONObject input) {
        String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" + org.json.JSONML.toString(input);
        return xml;
    }
}
