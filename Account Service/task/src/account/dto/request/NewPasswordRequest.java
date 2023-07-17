package account.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class NewPasswordRequest {

    @JsonProperty(value = "new_password")
    private String newPassword;
}
