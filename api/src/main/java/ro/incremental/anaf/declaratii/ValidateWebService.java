package ro.incremental.anaf.declaratii;

/**
 * Created by Alex Proca <alex.proca@gmail.com> on 18/03/16.
 */

import eu.pickride.dto.request.PDFGenerationRequestDTO;
import eu.pickride.dto.request.PDFGenerationRequestUsersDTO;
import eu.pickride.dto.response.*;
import eu.pickride.utils.XmlToPdfService;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.glassfish.jersey.media.multipart.*;

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
    @Path("/request_pdfs")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response requestPdfsAlt(PDFGenerationRequestDTO request) {

        // --- Validate request ---
        if (request == null) {
            return errorResponse("'request' body is missing or invalid");
        }
        if (request.getYear() == 0) {
            return errorResponse("'year' is required");
        }
        if (request.getMonth() == 0) {
            return errorResponse("'month' is required");
        }
        if (request.getUsers() == null || request.getUsers().isEmpty()) {
            return errorResponse("'users' array is required and cannot be empty");
        }

        XmlToPdfService pdfService = new XmlToPdfService();
        Map<String, Object> errors = new HashMap<>();

        // assign user id to list of successful declarations
        // assign user id to list of errors
        Map<String, List<PDFGenerationUserDeclarationInfoDTO>> userErrorsFullAlt = new HashMap<>();
        Map<String, List<PDFGenerationUserDeclarationInfoDTO>> usersSuccessFull = new HashMap<>();
        for (PDFGenerationRequestUsersDTO user : request.getUsers()) {

            // assign document number to error
//            Map<String, PDFGenerationResponseErrorDTO> userErrors = new HashMap<>();

            if (user.getUserId() == null || user.getUserId().isEmpty()) {
                errors.put("missing_user_id", "UserId is required for one of the users");
                continue;
            }

            boolean rectificativeD100 = false;
            boolean rectificativeD301 = false;
            boolean rectificativeD390 = false;

            if (user.getRectificative() != null) {
                for (String declarationNumber : user.getRectificative()) {
                    switch (declarationNumber) {
                        case "100":
                            rectificativeD100 = true;
                            break;
                        case "301":
                            rectificativeD301 = true;
                            break;
                        case "390":
                            rectificativeD390 = true;
                            break;
                        default:
                            errors.put(user.getUserId(),
                                    String.format("Unknown declaration number '%s'", declarationNumber));
                    }
                }
            }

            // --- Always process all three declarations ---
            try {
                // region D100
                try {

                    String internalDeclarationNumber = rectificativeD100 ? "710" : "100";

                    String pdfUrl100 = pdfService.generatePdfFromAzure(
                            user.getUserId(), request.getYear(), request.getMonth(), String.format("d%s", internalDeclarationNumber), rectificativeD100);
                    System.out.printf("%s generated", pdfUrl100);
                    if (!usersSuccessFull.containsKey(user.getUserId())) {
                        usersSuccessFull.put(user.getUserId(), new ArrayList<>());
                    }
                    if (rectificativeD100) {
                        usersSuccessFull.get(user.getUserId()).add(new PDFGenerationUserDeclarationInfoDTO(
                                "100",
                                rectificativeD100,
                                null
                        ));
                    } else {
                        usersSuccessFull.get(user.getUserId()).add(new PDFGenerationUserDeclarationInfoDTO(
                                "100",
                                rectificativeD100,
                                null
                        ));
                    }
                } catch (Exception ex) {
                    if (!userErrorsFullAlt.containsKey(user.getUserId())) {
                        userErrorsFullAlt.put(user.getUserId(), new ArrayList<>());
                    }
                    userErrorsFullAlt.get(user.getUserId()).add(
                            new PDFGenerationUserDeclarationInfoDTO(
                                    "100",
                                    rectificativeD100,
                                    ex.getMessage()
                            )
                    );
                }
                // endregion
                // region D301
                try {
                    String pdfUrl301 = pdfService.generatePdfFromAzure(
                            user.getUserId(), request.getYear(), request.getMonth(), "d301", rectificativeD301);
                    System.out.printf("%s generated", pdfUrl301);
                    if (!usersSuccessFull.containsKey(user.getUserId())) {
                        usersSuccessFull.put(user.getUserId(), new ArrayList<>());
                    }
                    usersSuccessFull.get(user.getUserId()).add(new PDFGenerationUserDeclarationInfoDTO(
                            "301",
                            rectificativeD301,
                            null
                    ));
                } catch (Exception ex) {
                    if (!userErrorsFullAlt.containsKey(user.getUserId())) {
                        userErrorsFullAlt.put(user.getUserId(), new ArrayList<>());
                    }
                    userErrorsFullAlt.get(user.getUserId()).add(
                            new PDFGenerationUserDeclarationInfoDTO(
                                    "301",
                                    rectificativeD301,
                                    ex.getMessage()
                            )
                    );
                }
                // endregion
                // region D390
                try {
                    String pdfUrl390 = pdfService.generatePdfFromAzure(
                            user.getUserId(), request.getYear(), request.getMonth(), "d390", rectificativeD390);
                    System.out.printf("%s generated", pdfUrl390);
                    if (!usersSuccessFull.containsKey(user.getUserId())) {
                        usersSuccessFull.put(user.getUserId(), new ArrayList<>());
                    }
                    usersSuccessFull.get(user.getUserId()).add(new PDFGenerationUserDeclarationInfoDTO(
                            "390",
                            rectificativeD390,
                            null
                    ));
                } catch (Exception ex) {
                    if (!userErrorsFullAlt.containsKey(user.getUserId())) {
                        userErrorsFullAlt.put(user.getUserId(), new ArrayList<>());
                    }
                    userErrorsFullAlt.get(user.getUserId()).add(
                            new PDFGenerationUserDeclarationInfoDTO(
                                    "390",
                                    rectificativeD390,
                                    ex.getMessage()
                            )
                    );
                }
                // endregion
            } catch (Exception ex) {
                // TODO figure out what to return here in a more fine-grained way
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
        }

        final PDFGenerationResponseDTO responsePayload =
                new PDFGenerationResponseDTO(
                        request.getUsers().size(),
                        request.getYear(),
                        request.getMonth(),
                        new PDFGenerationResponsesSuccessDTO(usersSuccessFull, usersSuccessFull.size()),
                        userErrorsFullAlt
                );

        return Response.ok(responsePayload, MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Path("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadXmlFile(
            @FormDataParam("file") InputStream uploadedInputStream,
            @FormDataParam("file") FormDataContentDisposition fileDetail,
            @FormDataParam("decName") String decName,
            @FormDataParam("userId") String userId,
            @FormDataParam("rectified") String rectified,
            @QueryParam("sync") @DefaultValue("false") boolean sync) {

        boolean rectifiedBool = false;
        // region checking all parameters and file, return 400 if data is missing or incorrect
        if (rectified == null || rectified.isEmpty()) {
            // Return JSON error response with 400 status
            String errorMessage = "'rectified' parameter not found or empty";
            Result result = new Result(errorMessage, -9);
            return Response.ok(result.toJSON(), MediaType.APPLICATION_JSON).build();

        } else {
            try {
                rectifiedBool = Boolean.parseBoolean(rectified);
            } catch (Exception ex) {
                String errorMessage = "'rectified' parameter must be true or false";
                Result result = new Result(errorMessage, -9);
                return Response.ok(result.toJSON(), MediaType.APPLICATION_JSON).build();
            }
        }

        if (userId == null || userId.isEmpty()) {
            // Return JSON error response with 400 status
            String errorMessage = "'userId' parameter not found or empty";
            Result result = new Result(errorMessage, -9);
            return Response.ok(result.toJSON(), MediaType.APPLICATION_JSON).build();
        }

        if (decName == null || decName.isEmpty()) {
            // Return JSON error response with 400 status
            String errorMessage = "'decName' parameter not found or empty";
            Result result = new Result(errorMessage, -9);
            return Response.ok(result.toJSON(), MediaType.APPLICATION_JSON).build();
        }

        if (uploadedInputStream == null || fileDetail == null) {
            // Return JSON error response with 400 status
            String errorMessage = "No file uploaded";
            Result result = new Result(errorMessage, -9);
            return Response.ok(result.toJSON(), MediaType.APPLICATION_JSON).build();
        }
        // endregion

        try {
            String fileName = fileDetail.getFileName();
            String newFileName = fileName.replace(".xml", ".pdf");
            String lowerCaseDecName = decName.toLowerCase();
            ResultWithDate fullResult = ResultWithDate.generateFullFromXMLStream(
                    uploadedInputStream,
                    lowerCaseDecName,
                    userId,
                    rectifiedBool);

            String declarationNameProcessed = decName;
            if (decName.equals("d710")) {
                declarationNameProcessed = "d100_rectified";
            }
            String newFileNameAlt = String.format("%s_%s_%s.pdf", fullResult.year, fullResult.month, declarationNameProcessed);
            String fullPath = String.format("%s/anaf/%s/%s/%s", userId, fullResult.year, fullResult.month, newFileNameAlt);

            Result result = fullResult.result;

            if (sync) {
                if (result.getHashCode() != null) {
                    // Send the resulting file as response body
                    Response.ResponseBuilder response = Response.ok(result.pdfFile);
                    response.header("Content-Disposition", "attachment; filename=\"" + newFileNameAlt + "\"");
                    response.header("X-Validation-Message", result.message);
                    return response.build();
                } else {
                    // Send error response
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                            .entity(result.toJSON())
                            .type(MediaType.APPLICATION_JSON)
                            .build();
                }
            } else {
                if (result.getHashCode() != null) {
                    Result.cacheResult(result);
                }
                // Return JSON response with result data
                return Response.ok(result.toJSON(), MediaType.APPLICATION_JSON)
                        .header("X-Validation-Message", result.message)
                        .build();
            }

        } catch (IOException e) {
            e.printStackTrace();
            Result result = new Result(e.getMessage(), -9);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(result.toJSON())
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
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

    // Helper for JSON error response
    private Response errorResponse(String message) {
        Map<String, Object> error = new HashMap<>();
        error.put("status", "error");
        error.put("message", message);
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(error)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

}
