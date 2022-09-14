package account.payload.request.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter @Setter
@ToString
public class UserSingUpRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String lastname;

    @NotBlank
    @Email(regexp = "[\\w\\.]+@acme.com", message = "email must ends with \"@acme.com\"")
    private String email;

    @NotBlank
    private String password;

}
