package account.dto.response.info;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;

import account.entity.User;

@Getter
public class UserInfoResponse {

    private final long id;
    private final String name;
    private final String lastname;
    private final String email;
    private final List<String> roles;

    public UserInfoResponse(User user) {
        List<String> roles = user.getRoles().stream()
                .map(role -> role.getName().name())
                .sorted(Comparator.naturalOrder())
                .collect(Collectors.toList());

        this.id = user.getId();
        this.name = user.getName();
        this.lastname = user.getLastname();
        this.email = user.getEmail();
        this.roles = roles;
    }
}
