package account.service.role;

import account.dto.Role;
import account.repository.RoleRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class RoleLoader implements ApplicationRunner{

    private final RoleRepository roleRepository;

    public RoleLoader(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        for (RoleEnum role : RoleEnum.values()) {
            roleRepository.save(new Role(role.ordinal() + 1, role));
        }
    }
}
