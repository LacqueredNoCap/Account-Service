package account.service;

import account.dto.user.User;
import account.repository.UserRepository;
import account.utils.PasswordValidator;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {

    private final UserRepository repository;

    private final PasswordEncoder encoder;

    private final PasswordValidator passwordValidator;

    public UserService(UserRepository repository,
                       PasswordEncoder encoder,
                       PasswordValidator passwordValidator) {
        this.repository = repository;
        this.encoder = encoder;
        this.passwordValidator = passwordValidator;
    }

    public User findUserByEmail(String email) {
        return repository.findUserByEmailIgnoreCase(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "User exist!"));
    }

    public void register(User user) {
        if (repository.findUserByEmailIgnoreCase(user.getEmail()).isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "User exist!");
        }
        passwordValidator.validatePassword("", user.getPassword());
        user.setPassword(encoder.encode(user.getPassword()));
        repository.save(user);
    }

    public void changePassword(String newPassword, String email) {
        User user = findUserByEmail(email);
        passwordValidator.validatePassword(user.getPassword(), newPassword);
        user.setPassword(encoder.encode(newPassword));
        repository.save(user);
    }

}
