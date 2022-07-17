package account.payload.response;

public class PasswordResetResponse {

    private final String email;
    private final String status;

    public PasswordResetResponse(String email, String status) {
        this.email = email;
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public String getStatus() {
        return status;
    }
}
