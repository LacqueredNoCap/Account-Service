package account.repository;

import java.util.Optional;

import account.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByEmailIgnoreCase(String email);

    boolean existsUserByEmailIgnoreCase(String email);

    //@Query("UPDATE User u SET u.failedAttempts = ?1 WHERE u.email = LOWER(?2) ")
    @Query(value = "UPDATE users u SET u.failed_attempts = ?1 WHERE u.email = LOWER(?2) ", nativeQuery = true)
    @Modifying
    void updateFailedAttempts(int attempts, String email);

    void deleteByEmailIgnoreCase(String email);
}
