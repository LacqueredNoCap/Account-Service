package account.utils;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

@Component
public class PasswordValidator {

    private static final Set<String> BREACHED_PASSWORDS =
            Set.of("PasswordForJanuary", "PasswordForFebruary", "PasswordForMarch",
                    "PasswordForApril", "PasswordForMay", "PasswordForJune",
                    "PasswordForJuly", "PasswordForAugust", "PasswordForSeptember",
                    "PasswordForOctober", "PasswordForNovember", "PasswordForDecember");

    private final PasswordEncoder encoder;

    public PasswordValidator(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    public void validatePassword(String oldPassword, String newPassword) {
        if (newPassword.length() < 12) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Password length must be 12 chars minimum!");
        }
        if (BREACHED_PASSWORDS.contains(newPassword)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "The password is in the hacker's database!");
        }
        if (encoder.matches(newPassword, oldPassword)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "The passwords must be different!");
        }
    }
}
