package eu.pickride.dto.request;

import jakarta.json.bind.annotation.JsonbProperty;
import java.util.List;

public class PDFGenerationRequestUsersDTO {

    @JsonbProperty("user_id")
    private String userId;

    @JsonbProperty("rectificative")
    private List<String> rectificative;

    // --- Getters & Setters ---
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getRectificative() {
        return rectificative;
    }
    public void setRectificative(List<String> rectificative) {
        this.rectificative = rectificative;
    }
}
