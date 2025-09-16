package eu.pickride.dto.response;

import java.util.List;
import java.util.Map;

public class PDFGenerationResponsesSuccessDTO {

    public PDFGenerationResponsesSuccessDTO(
            Map<String, List<PDFGenerationUserDeclarationInfoDTO>> users,
            int count
    ) {
        this.users = users;
        this.count = count;
    }

    private Map<String, List<PDFGenerationUserDeclarationInfoDTO>> users;
    // key = userId, value = list of successfully generated declarations

    private int count;
    // number of users with at least one success

    public Map<String, List<PDFGenerationUserDeclarationInfoDTO>> getUsers() {
        return users;
    }
    public void setUsers(Map<String, List<PDFGenerationUserDeclarationInfoDTO>> users) {
        this.users = users;
    }

    public int getCount() {
        return count;
    }
    public void setCount(int count) {
        this.count = count;
    }
}
