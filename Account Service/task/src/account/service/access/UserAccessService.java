package account.service.access;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import account.entity.User;
import account.repository.UserRepository;
import account.service.role.RoleEnum;

@Service
public class UserAccessService {

    @Value("${security.maxFailedAttempts}")
    private int maxFailedAttempts;

    private final UserRepository userRepository;

    public UserAccessService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void provideAccessToUser(String email, String operation) {
        User user = userRepository.findUserByEmailIgnoreCase(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "User doesn't exist!")
                );
        AccessOperation accessOperation = getAccessOperation(operation);

        if (user.getRoles().stream().anyMatch(
                role -> role.getName().equals(RoleEnum.ROLE_ADMINISTRATOR)) &&
                accessOperation.equals(AccessOperation.LOCK)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Can't lock the ADMINISTRATOR!"
            );
        }

        if (accessOperation.equals(AccessOperation.LOCK)) {
            lock(user);
        }
        if (accessOperation.equals(AccessOperation.UNLOCK)) {
            unlock(user);
        }
    }

    @Transactional
    public void onFailLoginAttempt(String username) {
        User user = this.findUserByEmail(username);

        if (user.isLocked()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User is blocked!");
        }

        int failedAttempts = user.getFailedAttempts() + 1;
        if (failedAttempts >= maxFailedAttempts) {
            lock(user);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized!");
        }

        userRepository.updateFailedAttempts(failedAttempts, username);

        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized!");
    }

    @Transactional
    public void onSuccessLoginAttempt(String username) {
        User user = this.findUserByEmail(username);

        if (user.isLocked()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User is blocked!");
        }

        userRepository.updateFailedAttempts(0, user.getEmail());
    }

    private User findUserByEmail(String email) {
        return userRepository.findUserByEmailIgnoreCase(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "Unauthorized!")
                );
    }

    private void lock(User user) {
        user.setLocked(true);
        user.setFailedAttempts(0);
        userRepository.save(user);
    }

    private void unlock(User user) {
        user.setLocked(false);
        user.setFailedAttempts(0);
        userRepository.save(user);
    }

    private static AccessOperation getAccessOperation(String operation) {
        try {
            return AccessOperation.valueOf(operation);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Access operation not found"
            );
        }
    }
}
