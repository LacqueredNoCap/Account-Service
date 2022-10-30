package account.service.access;

import account.service.event.EventEnum;
import account.service.event.EventService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import account.entity.User;
import account.repository.UserRepository;
import account.service.role.RoleEnum;

import java.util.Locale;

@Service
public class UserAccessService {

    @Value("${security.maxFailedAttempts}")
    private int maxFailedAttempts;

    private final EventService eventService;
    private final UserRepository userRepository;

    public UserAccessService(EventService eventService, UserRepository userRepository) {
        this.eventService = eventService;
        this.userRepository = userRepository;
    }

    @Transactional
    public void provideAccessToUser(String email, String operation) {
        User user = this.findUserByEmail(email);
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
    public void onFailLoginAttempt(String username, String requestURI) {
        User user = this.findUserByEmail(username);

        eventService.makeEvent(
                EventEnum.LOGIN_FAILED,
                username.toLowerCase(Locale.ENGLISH),
                requestURI,
                requestURI
        );

        if (user.isLocked()) {
            return;
        }

        int failedAttempts = user.getFailedAttempts() + 1;
        if (failedAttempts >= maxFailedAttempts) {
            eventService.makeEvent(
                    EventEnum.BRUTE_FORCE,
                    username.toLowerCase(Locale.ENGLISH),
                    requestURI,
                    requestURI
            );

            if (user.getRoles().stream().anyMatch(
                    role -> role.getName().equals(RoleEnum.ROLE_ADMINISTRATOR))) {
                return;
            }

            lock(user);

            eventService.makeEvent(
                    EventEnum.LOCK_USER,
                    username.toLowerCase(Locale.ENGLISH),
                    "Lock user " + username.toLowerCase(Locale.ENGLISH),
                    requestURI
            );

            return;
        }

        userRepository.updateFailedAttempts(failedAttempts, username);
        System.out.println("FAIL LOGIN: " + username +
                ", attempts remaining: " + (maxFailedAttempts - failedAttempts)
        );
    }

    @Transactional
    public void onSuccessLoginAttempt(String username) {
        User user = this.findUserByEmail(username);

        if (user.isLocked()) {
            return;
        }

        userRepository.updateFailedAttempts(0, user.getEmail());
        System.out.println("SUCCESS LOGIN: " + username);
    }

    private User findUserByEmail(String email) {
        return userRepository.findUserByEmailIgnoreCase(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "User doesn't exist!")
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
