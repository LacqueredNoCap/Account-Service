package account.repository;

import java.util.Optional;

import account.dto.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByEmailIgnoreCase(String email);

    boolean existsUserByEmailIgnoreCase(String email);

    void deleteByEmailIgnoreCase(String email);
}
