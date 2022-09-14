package account.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserDeletedResponse {

    private final String email;
    private final String status;
}
