package eu.pickride.dto.response;

import jakarta.json.bind.annotation.JsonbProperty;

public class PDFGenerationUserDeclarationInfoDTO {

    public PDFGenerationUserDeclarationInfoDTO(
            String declarationNo,
            boolean isRectified,
            String validationError
    ) {
        this.declarationNo = declarationNo;
        this.isRectified = isRectified;
        this.validationError = validationError;
    }

    @JsonbProperty("type")
    private String declarationNo;

    @JsonbProperty("is_rectified")
    private boolean isRectified;

    @JsonbProperty("validation_error")
    private String validationError; // nullable (only for errors)

    // --- Getters & Setters ---
    public String getDeclarationNo() {
        return declarationNo;
    }
    public void setDeclarationNo(String declarationNo) {
        this.declarationNo = declarationNo;
    }

    public boolean isRectified() {
        return isRectified;
    }
    public void setRectified(boolean rectified) {
        isRectified = rectified;
    }

    public String getValidationError() {
        return validationError;
    }
    public void setValidationError(String validationError) {
        this.validationError = validationError;
    }
}
