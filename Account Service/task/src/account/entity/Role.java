package account.entity;

import account.service.role.RoleEnum;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "roles")
@Getter @Setter
public class Role {

    @Id
    @Column
    private Long id;

    @Column(unique = true, nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleEnum name;

    public Role() {
    }

    public Role(long id, RoleEnum name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role)) return false;

        Role role = (Role) o;

        return name.equals(role.getName());
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
