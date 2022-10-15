package account.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserRoleChangeRequest {

    private String user;
    private String operation;
    private String role;

}
