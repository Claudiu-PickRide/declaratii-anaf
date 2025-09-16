package eu.pickride.dto.request;

import jakarta.json.bind.annotation.JsonbProperty;
import java.util.List;

public class PDFGenerationRequestDTO {

    @JsonbProperty("users")
    private List<PDFGenerationRequestUsersDTO> users;

    @JsonbProperty("year")
    private int year;

    @JsonbProperty("month")
    private int month;

    // --- Getters & Setters ---
    public List<PDFGenerationRequestUsersDTO> getUsers() {
        return users;
    }
    public void setUsers(List<PDFGenerationRequestUsersDTO> users) {
        this.users = users;
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
}
