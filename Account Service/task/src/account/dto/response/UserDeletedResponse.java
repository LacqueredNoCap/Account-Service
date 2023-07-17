package account.dto.response;

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
