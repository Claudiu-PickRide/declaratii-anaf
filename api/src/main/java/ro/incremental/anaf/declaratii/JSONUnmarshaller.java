package ro.incremental.anaf.declaratii;

import org.json.JSONObject;
import org.json.JSONTokener;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.MessageBodyReader;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * Created by Alex Proca <alex.proca@gmail.com> on 18/03/16.
 */

@Provider
@Consumes(MediaType.APPLICATION_JSON)
public class JSONUnmarshaller implements MessageBodyReader<JSONObject> {
    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return true;
    }

    @Override
    public JSONObject readFrom(Class<JSONObject> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException, WebApplicationException {

        return new JSONObject(new JSONTokener(entityStream));
    }
}
