package ro.incremental.anaf.declaratii;

import org.junit.Test;
import org.reflections.Reflections;
import pdf.PdfCreation;

import java.util.Set;

/**
 * Created by Alex Proca <alex.proca@gmail.com> on 26/03/16.
 */
public class TestValidators {

    @Test
    public void testValidators() throws Exception {

        Reflections reflections = new Reflections("");

        Set<Class<? extends PdfCreation>> subTypes =
                reflections.getSubTypesOf(PdfCreation.class);

        for(Class<? extends PdfCreation> pdf : subTypes){

            System.out.println(pdf.getCanonicalName());

        }

    }
}
