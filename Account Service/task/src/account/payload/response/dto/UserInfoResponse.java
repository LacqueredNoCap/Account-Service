package account.payload.response.dto;

import java.util.Set;
import java.util.stream.Collectors;

import lombok.Getter;

import account.dto.User;

@Getter
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

    public UserInfoResponse(
            long id,
            String name,
            String lastname,
            String email,
            Set<String> roles) {
        this.id = id;
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.roles = roles;
    }
}
