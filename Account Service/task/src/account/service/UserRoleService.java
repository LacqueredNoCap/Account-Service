package account.service;

import account.dto.Role;
import account.dto.User;
import account.payload.request.UserRoleChangeRequest;
import account.repository.RoleRepository;
import account.repository.UserRepository;
import account.service.role.Operation;
import account.service.role.RoleEnum;
import account.service.role.RoleType;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserRoleService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserRoleService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public void changeUserRole(UserRoleChangeRequest roleChange) {
        User user = userRepository.findUserByEmailIgnoreCase(roleChange.getUser())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found!"));

        Operation operation = getOperation(roleChange.getOperation());
        Role role = this.getRoleByRoleEnum(getRole("ROLE_" + roleChange.getRole()));

        if (operation.equals(Operation.GRANT)) {
            this.grantRoleToUser(user, role);
        }
        if (operation.equals(Operation.REMOVE)) {
            this.removeRoleFromUser(user, role);
        }

        userRepository.save(user);
    }

    private Role getRoleByRoleEnum(RoleEnum role) {
        return roleRepository.findRoleByName(role).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found!"));
    }

    private void grantRoleToUser(User user, Role role) {
        if (!RoleType.isCompatibleTypes(
                user.getRoles().iterator().next().getName().getType(),
                role.getName().getType())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "The user cannot combine administrative and business roles!");
        }
        user.addRole(role);
    }

    private void removeRoleFromUser(User user, Role role) {
        if (role.getName().equals(RoleEnum.ROLE_ADMINISTRATOR)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Can't remove ADMINISTRATOR role!");
        }
        if (user.getRoles().contains(role) && user.getRoles().size() == 1) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "The user must have at least one role!");
        }
        if (!user.getRoles().contains(role)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "The user does not have a role!");
        }

        user.removeRole(role);
    }

    private static Operation getOperation(String operation) {
        try {
            return Operation.valueOf(operation);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Operation not found!");
        }
    }

    private static RoleEnum getRole(String role) {
        try {
            return RoleEnum.valueOf(role);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found!");
        }
    }
}
