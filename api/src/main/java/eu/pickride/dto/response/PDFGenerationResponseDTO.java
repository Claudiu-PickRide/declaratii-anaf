package eu.pickride.dto.response;

import jakarta.json.bind.annotation.JsonbProperty;
import java.util.List;
import java.util.Map;

public class PDFGenerationResponseDTO {

    public PDFGenerationResponseDTO(
            int totalCount,
            int year,
            int month,
            PDFGenerationResponsesSuccessDTO success,
            Map<String, List<PDFGenerationUserDeclarationInfoDTO>> error
    ) {
        this.totalCount = totalCount;
        this.year = year;
        this.month = month;
        this.success = success;
        this.error = error;
    }

    @JsonbProperty("totalCount")
    private int totalCount;

    @JsonbProperty("year")
    private int year;

    @JsonbProperty("month")
    private int month;

    @JsonbProperty("success")
    private PDFGenerationResponsesSuccessDTO success;

    @JsonbProperty("error")
    private Map<String, List<PDFGenerationUserDeclarationInfoDTO>> error;
    // key = userId, value = list of declarations with errors

    // --- Getters & Setters ---
    public int getTotalCount() {
        return totalCount;
    }
    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getYear() {
        return year;
    }
    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }
    public void setMonth(int month) {
        this.month = month;
    }

    public PDFGenerationResponsesSuccessDTO getSuccess() {
        return success;
    }
    public void setSuccess(PDFGenerationResponsesSuccessDTO success) {
        this.success = success;
    }

    public Map<String, List<PDFGenerationUserDeclarationInfoDTO>> getError() {
        return error;
    }
    public void setError(Map<String, List<PDFGenerationUserDeclarationInfoDTO>> error) {
        this.error = error;
    }
}
