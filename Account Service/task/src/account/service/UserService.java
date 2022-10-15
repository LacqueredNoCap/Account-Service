package account.service;

import account.entity.Role;
import account.entity.User;
import account.dto.request.info.UserSingUpRequest;
import account.repository.RoleRepository;
import account.repository.UserRepository;
import account.service.role.RoleEnum;
import account.security.PasswordValidator;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final PasswordValidator passwordValidator;

    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder encoder,
                       PasswordValidator passwordValidator) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.passwordValidator = passwordValidator;
    }

    public User findUserByEmail(String email) {
        return userRepository.findUserByEmailIgnoreCase(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "User doesn't exist!"));
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Transactional(rollbackFor = ResponseStatusException.class)
    public void signup(UserSingUpRequest newUser) {
        if (userRepository.existsUserByEmailIgnoreCase(newUser.getEmail())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "User exist!");
        }

        User user = new User(
                newUser.getName(),
                newUser.getLastname(),
                newUser.getEmail(),
                newUser.getPassword()
        );

        if (userRepository.count() == 0) {
            user.addRole(roleRepository.findRoleByName(RoleEnum.ROLE_ADMINISTRATOR)
                    .orElseThrow(() -> new ResponseStatusException(
                                    HttpStatus.NOT_FOUND,
                                    "Role not found!"
                            )
                    ));
        } else {
            user.addRole(roleRepository.findRoleByName(RoleEnum.ROLE_USER)
                    .orElseThrow(() -> new ResponseStatusException(
                                    HttpStatus.NOT_FOUND,
                                    "Role not found!"
                            )
                    ));
        }

        this.save(user);
    }

    private void save(User user) {
        user.setEmail(user.getEmail().toLowerCase(Locale.ENGLISH));
        passwordValidator.validatePassword("", user.getPassword());
        user.setPassword(encoder.encode(user.getPassword()));

        userRepository.save(user);
    }

    @Transactional(rollbackFor = ResponseStatusException.class)
    public void changeUserPassword(String email, String newPassword) {
        User user = this.findUserByEmail(email);
        passwordValidator.validatePassword(user.getPassword(), newPassword);
        user.setPassword(encoder.encode(newPassword));
        userRepository.save(user);
    }

    @Transactional(rollbackFor = ResponseStatusException.class)
    public void deleteUserByEmail(String email) {
        if (!userRepository.existsUserByEmailIgnoreCase(email)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "User not found!");
        }
        if (userRepository.findUserByEmailIgnoreCase(email)
                .map(User::getRoles)
                .orElse(Collections.emptySet())
                .stream()
                .map(Role::getName)
                .anyMatch(role -> role.equals(RoleEnum.ROLE_ADMINISTRATOR))) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Can't remove ADMINISTRATOR role!");
        }

        userRepository.deleteByEmailIgnoreCase(email);
    }
}
