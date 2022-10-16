package account.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserLockRequest {

    private String user;
    private String roleOperation;
}
