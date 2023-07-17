package account.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserAccessRequest {

    private String user;
    private String operation;
}
