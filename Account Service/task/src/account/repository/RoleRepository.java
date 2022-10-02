package account.repository;

import java.util.Optional;

import account.dto.Role;

import account.service.role.RoleEnum;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findRoleByName(RoleEnum name);

}
