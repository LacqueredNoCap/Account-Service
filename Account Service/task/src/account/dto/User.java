package account.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Entity(name = "usr")
@Getter @Setter
public class User {

    @Id
    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )
    @Column
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Long id;

    @NotBlank
    @Column
    private String name;
    @NotBlank
    @Column
    private String lastname;

    @NotBlank
    @Email(regexp = "\\S+@acme.com", message = "email must ends with \"@acme.com\"")
    @Column(unique = true)
    private String email;
    @NotBlank
    @Column
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    public User() {}

    public User(
            String name,
            String lastname,
            String email,
            String password) {
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
    }
}
