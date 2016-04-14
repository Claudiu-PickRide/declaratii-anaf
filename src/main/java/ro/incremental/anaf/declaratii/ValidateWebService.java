package ro.incremental.anaf.declaratii;

/**
 * Created by Alex Proca <alex.proca@gmail.com> on 18/03/16.
 */

import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Path("/")
public class ValidateWebService {

    private static final String indexHtml;
    private static final String javascript;

    static {
        indexHtml = cacheResource("index.html");
        javascript = cacheResource("javascript.js");
    }

    @GET
    @Path("/")
    @Produces(MediaType.TEXT_HTML)
    public Response index() {

        return Response.ok(indexHtml).build();
    }

    @GET
    @Path("/javascript.js")
    @Produces(MediaType.TEXT_HTML)
    public Response javascript() {

        return Response.ok(javascript).build();
    }

    @GET
    @Path("/available")
    @Produces(MediaType.TEXT_PLAIN)
    public String available() {
        return "yes";
    }

    @GET
    @Path("/download/{id}")
    @Produces("application/pdf")
    public Response download(@PathParam("id") String id) {

        Result result = Result.getFromCache(id);

        if (result == null) {
            return Response.noContent().build();
        }

        Response.ResponseBuilder response = Response.ok(result.pdfFile);
        response.header("Content-Disposition", "attachment; filename=\"" + result.decName + ".pdf\"");

        return response.build();
    }

    @POST
    @Path("/validate")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response available(JSONObject input) {

        Result result = Result.generateFromXMLString(json2Xml(input), getDeclName(input));

        if (result.getHashCode() != null) {
            Result.cacheResult(result);
        }

        return Response.ok(result.toJSON(), MediaType.APPLICATION_JSON).build();
    }

    private static String json2Xml(JSONObject input) {
        return "<?xml version=\"1.0\" encoding=\"utf-8\"?>" + org.json.JSONML.toString(input);
    }

    private static String getDeclName(JSONObject input) {
        return input.getString("tagName").replace("declaratie", "d");
    }

    private static String cacheResource(String resource) {
        String line;
        InputStream indexHtmlStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
        BufferedReader htmlReader = new BufferedReader(new InputStreamReader(indexHtmlStream));

        StringBuilder result = new StringBuilder();

        try {
            while ((line = htmlReader.readLine()) != null) {
                result.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result.toString();
    }

}
