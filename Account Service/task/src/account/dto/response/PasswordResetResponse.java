package account.dto.response;

import lombok.Getter;

@Getter
public class PasswordResetResponse {

    private final String email;
    private final String status;

    public PasswordResetResponse(String email, String status) {
        this.email = email;
        this.status = status;
    }
}
