package account.repository;

import account.dto.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByEmailIgnoreCase(String email);

    boolean existsUserByEmailIgnoreCase(String email);
}
