package account.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class UserDeletedResponse {

    private final String user;
    private final String status;

    public UserDeletedResponse(String user, String status) {
        this.user = user;
        this.status = status;
    }
}
