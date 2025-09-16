package eu.pickride.utils;

public class PathUtils {

    public static String getAzureXmlPath(String userId, int year, int month, String decNr, boolean isRectified) {
        return getAzurePath(userId, year, month, decNr, isRectified, "xml");
    }

    public static String getAzurePdfPath(String userId, int year, int month, String decNr, boolean isRectified) {
        return getAzurePath(userId, year, month, decNr, isRectified, "pdf");
    }

    /**
     * Unified function: returns Azure storage path for a declaration file.
     * Example: user123/ANAF/declaratii/2025/8/pdf/declaratia_390_rectified.pdf
     *
     * @param userId           user identifier
     * @param year             year of declaration
     * @param month            month of declaration
     * @param decNr            declaration number (e.g. "390")
     * @param isRectified      true if rectified
     * @param extension        "xml" or "pdf"
     * @return                 full Azure blob path
     */
    private static String getAzurePath(String userId, int year, int month, String decNr, boolean isRectified, String extension) {
        return getAzurePathForExtension(userId, year, month, decNr, isRectified, extension);
    }

    private static String getFileRootPath(String userId, int year, int month) {
        return String.format("%s/ANAF/declaratii/%d/%d", userId, year, month);
    }

    private static String getFileName(String decNr, boolean isRectified, String extension) {
        final String rectifiedText = isRectified ? "_rectified" : "";
        return String.format("d%s%s.%s", decNr, rectifiedText, extension);
    }

    private static String getAzurePathForExtension(String userId, int year, int month, String decNr, boolean isRectified, String extension) {
        final String rootPath = getFileRootPath(userId, year, month);
        final String filePath = getFileName(decNr, isRectified, extension);
        return String.format("%s/%s/%s", rootPath, extension, filePath);
    }
}
