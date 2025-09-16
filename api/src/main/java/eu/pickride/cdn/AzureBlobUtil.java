package eu.pickride.cdn;

import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.storage.blob.*;
import com.azure.storage.blob.models.*;

import java.io.*;

/**
 * Utility for working with Azure Blob Storage (backed by Azure CDN).
 */
public class AzureBlobUtil {

    private static final BlobServiceClient blobServiceClient;
    private static final BlobContainerClient containerClient;

    static {
        String connectionString = System.getenv("AZURE_STORAGE_CONN");
        String account = System.getenv("AZURE_STORAGE_ACCOUNT");
        String container = System.getenv("AZURE_STORAGE_CONTAINER");

        if (container == null || container.isEmpty()) {
            throw new IllegalStateException("Missing environment variable AZURE_STORAGE_CONTAINER");
        }

        if (connectionString != null && !connectionString.isEmpty()) {
            // Local/dev with connection string
            blobServiceClient = new BlobServiceClientBuilder()
                    .connectionString(connectionString)
                    .buildClient();
        } else if (account != null && !account.isEmpty()) {
            // Production: use Managed Identity
            blobServiceClient = new BlobServiceClientBuilder()
                    .endpoint("https://" + account + ".blob.core.windows.net")
                    .credential(new DefaultAzureCredentialBuilder().build())
                    .buildClient();
        } else {
            throw new IllegalStateException("No Azure Storage authentication found (AZURE_STORAGE_CONN or AZURE_STORAGE_ACCOUNT).");
        }

        containerClient = blobServiceClient.getBlobContainerClient(container);
        if (!containerClient.exists()) {
            containerClient.create();
        }
    }

    /**
     * Uploads a file to the container under the given path.
     * Example: uploads/d100/2025/7/myfile.pdf
     */
    public static String uploadFile(File file, String blobPath) throws IOException {
        BlobClient blobClient = containerClient.getBlobClient(blobPath);

        try (InputStream inputStream = new FileInputStream(file)) {
            blobClient.upload(inputStream, file.length(), true);
        }

        return blobClient.getBlobUrl(); // Blob URL (CDN will map this if configured)
    }

    /**
     * Uploads arbitrary data from an InputStream (e.g., generated PDF in-memory).
     */
    public static String uploadStream(InputStream inputStream, long length, String blobPath) {
        BlobClient blobClient = containerClient.getBlobClient(blobPath);
        blobClient.upload(inputStream, length, true);
        return blobClient.getBlobUrl();
    }

    /**
     * Downloads a blob and returns its InputStream.
     * Caller is responsible for closing the stream.
     */
    public static InputStream downloadFile(String blobPath) {
        BlobClient blobClient = containerClient.getBlobClient(blobPath);
        if (!blobClient.exists()) {
            throw new IllegalArgumentException("Blob does not exist: " + blobPath);
        }
        return blobClient.openInputStream();
    }

    /**
     * Deletes a blob from the container.
     */
    public static void deleteFile(String blobPath) {
        BlobClient blobClient = containerClient.getBlobClient(blobPath);
        blobClient.deleteIfExists();
    }
}
