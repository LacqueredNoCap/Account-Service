package account.user;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return repository.findUserByEmailIgnoreCase(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format("User with email \"%s\" not found", email)
                ));
    }

    public User findUserByEmail(String email) {
        return repository.findUserByEmailIgnoreCase(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "User exist!"));
    }

    public void save(User user) {
        if (repository.findUserByEmailIgnoreCase(user.getEmail()).isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "User exist!");
        }
        repository.save(user);
    }
}
