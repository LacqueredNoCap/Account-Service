package account.dto;

import account.service.role.RoleEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity(name = "roles")
@Getter @Setter
@ToString
public class Role {

    @Id
//    @SequenceGenerator(
//            name = "role_sequence",
//            sequenceName = "role_sequence",
//            allocationSize = 1
//    )
//    @GeneratedValue(
//            strategy = GenerationType.SEQUENCE,
//            generator = "role_sequence"
//    )
    @Column
    private long id;

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
