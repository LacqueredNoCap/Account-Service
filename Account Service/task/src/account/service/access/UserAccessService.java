package account.service.access;

import account.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import account.entity.User;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserAccessService {

    private final UserRepository userRepository;

    public UserAccessService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findUserByEmail(String email) {
        return userRepository.findUserByEmailIgnoreCase(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "User doesn't exist!"));
    }

    public void provideAccessToUser(String email, String operation) {
        User user = this.findUserByEmail(email);
        AccessOperation accessOperation = getAccessOperation(operation);

        if (accessOperation.equals(AccessOperation.LOCK)) {
            lock(user);
        }
        if (accessOperation.equals(AccessOperation.UNLOCK)) {
            unlock(user);
        }
    }

    public void increaseFailedAttempts(User user) {
        int failedAttempts = user.getFailedAttempts();
        userRepository.updateFailedAttempts(failedAttempts + 1, user.getEmail());
    }

    public void resetFailedAttempts(User user) {
        userRepository.updateFailedAttempts(0, user.getEmail());
    }

    public void lock(User user) {
        user.setNotLocked(false);
        user.setFailedAttempts(0);
        userRepository.save(user);
    }

    public void unlock(User user) {
        user.setNotLocked(true);
        user.setFailedAttempts(0);
        userRepository.save(user);
    }

    private static AccessOperation getAccessOperation(String operation) {
        try {
            return AccessOperation.valueOf(operation);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Access operation not found");
        }
    }
}
