package account.payload.response;

public class StatusResponse {

    private final String status;

    public StatusResponse(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
