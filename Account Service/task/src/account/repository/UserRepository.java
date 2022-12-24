package account.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import account.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM account.entity.User u LEFT JOIN FETCH u.roles WHERE u.email = LOWER(?1)")
    Optional<User> findUserByEmailIgnoreCase(String email);

    boolean existsUserByEmailIgnoreCase(String email);

    @Modifying
    @Query("UPDATE account.entity.User u SET u.failedAttempts = ?1 WHERE u.email = LOWER(?2) ")
    void updateFailedAttempts(int attempts, String email);

    void deleteByEmailIgnoreCase(String email);

}
