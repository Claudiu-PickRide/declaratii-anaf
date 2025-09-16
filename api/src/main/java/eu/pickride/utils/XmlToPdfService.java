package eu.pickride.utils;

import eu.pickride.cdn.AzureBlobUtil;
import ro.incremental.anaf.declaratii.Result;
import ro.incremental.anaf.declaratii.ResultWithDate;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class XmlToPdfService {

    /**
     * Downloads an XML declaration from Azure, generates its PDF,
     * and uploads the PDF back to Azure under the /pdf path.
     *
     * @param userId User ID
     * @param year Year of declaration
     * @param month Month of declaration
     * @param declarationNumber Declaration number (e.g., "100", "390", "301")
     * @param rectified Whether the declaration is rectified
     * @return Azure Blob URL of the uploaded PDF
     */
    public String generatePdfFromAzure(String userId,
                                       int year,
                                       int month,
                                       String declarationNumber,
                                       boolean rectified) throws IOException {

        // 1. Build XML path
        String xmlBlobPath = PathUtils.getAzureXmlPath(userId, year, month, declarationNumber, rectified);

        // 2. Download XML
        try (InputStream xmlStream = AzureBlobUtil.downloadFile(xmlBlobPath)) {

            // 3. Convert XML → PDF
            ResultWithDate resultWithDate = ResultWithDate.generateFullFromXMLStream(
                    xmlStream,
                    declarationNumber.toLowerCase(),
                    userId,
                    rectified
            );

            Result result = resultWithDate.result;
            if (result == null || result.pdfFile == null) {
                throw new IllegalStateException("Failed to generate PDF for: " + xmlBlobPath);
            }

            // 4. Build PDF path
            String pdfBlobPath = PathUtils.getAzurePdfPath(userId, year, month, declarationNumber, rectified);

            // 5. Upload PDF
            final byte[] bytes = readFileToBytes(result.pdfFile);
            try (ByteArrayInputStream pdfStream = new ByteArrayInputStream(bytes)) {
                return AzureBlobUtil.uploadStream(pdfStream, bytes.length, pdfBlobPath);
            }
        }
    }

    public static byte[] readFileToBytes(File file) throws IOException {
        return Files.readAllBytes(file.toPath());
    }
}
