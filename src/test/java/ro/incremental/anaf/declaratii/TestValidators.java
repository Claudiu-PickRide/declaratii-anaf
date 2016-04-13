package ro.incremental.anaf.declaratii;

import org.junit.Test;
import org.reflections.Reflections;
import pdf.PdfCreation;
import pdf.PdfCreatorRoot;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertTrue;

/**
 * Created by Alex Proca <alex.proca@gmail.com> on 26/03/16.
 */
public class TestValidators {

    @Test
    public void testValidators() throws Exception {

        InputStream packages = Thread.currentThread().getContextClassLoader().getResourceAsStream("packages.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(packages));

        Map<String, PdfCreation> creatorsDictionary = new HashMap<>();
        String line;
        while((line = reader.readLine()) != null) {
            creatorsDictionary.put(line, (PdfCreation)Class.forName(line + ".PdfCreator").newInstance());
        }

        for(String key: creatorsDictionary.keySet()) {
            System.out.println(key + "->" + creatorsDictionary.get(key).toString());
            PdfCreatorRoot creator = (PdfCreatorRoot) creatorsDictionary.get(key);


        }

        boolean containsDec106 = false;
        for(Package p : Package.getPackages()) {

            containsDec106 |= p.getName().contains("d106");

        }

        assertTrue("Anaf libraries are in test classpath", containsDec106);

    }

    private void listClassesInPackage(String packageName) {
        Reflections reflections = new Reflections(packageName);

        Set<Class<? extends PdfCreation>> subTypes =
                reflections.getSubTypesOf(PdfCreation.class);

        for(Class<? extends PdfCreation> pdf : subTypes){

            System.out.println(pdf.getCanonicalName());

        }

    }
}
