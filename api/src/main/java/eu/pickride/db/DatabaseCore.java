package eu.pickride.db;

public class DatabaseCore {

    private final PdfDocumentRepository pdfDocumentRepository;

    public DatabaseCore() {
        this.pdfDocumentRepository = new PdfDocumentRepository();
    }

    public PdfDocumentRepository pdfDocuments() {
        return pdfDocumentRepository;
    }
}
