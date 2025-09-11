package eu.pickride.db;

import eu.pickride.db.repo.PdfDocumentRepository;

public class DatabaseCore {

    private final PdfDocumentRepository pdfDocumentRepository;

    public DatabaseCore() {
        this.pdfDocumentRepository = new PdfDocumentRepository();
    }

    public PdfDocumentRepository pdfDocuments() {
        return pdfDocumentRepository;
    }
}
