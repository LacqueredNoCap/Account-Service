package account.payload.response;

import lombok.Getter;

@Getter
public class StatusResponse {

    private final String status;

    public StatusResponse(String status) {
        this.status = status;
    }
}
