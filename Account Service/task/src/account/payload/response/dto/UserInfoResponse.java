package account.payload.response.dto;

import account.dto.User;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.stream.Collectors;

@Getter @Setter
public class UserInfoResponse {

    private final long id;

    private final String name;

    private final String lastname;

    private final String email;

    private final Set<String> roles;

    public UserInfoResponse(User user) {
        Set<String> roles = user.getRoles().stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toSet());

        this.id = user.getId();
        this.name = user.getName();
        this.lastname = user.getLastname();
        this.email = user.getEmail();
        this.roles = roles;
    }
}
