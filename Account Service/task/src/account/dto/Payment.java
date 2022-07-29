package account.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;

@Entity(name = "payment")
@Getter @Setter
public class Payment {

    @Id
    @SequenceGenerator(
            name = "payment_sequence",
            sequenceName = "payment_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "payment_sequence"
    )
    @Column
    @JsonIgnore
    private Long id;

    @Column
    @Email(regexp = "\\S+@acme.com", message = "email must ends with \"@acme.com\"")
    private String employee;

    @Column
    @Pattern(regexp = "(0[1-9]|1[0-2])-(\\d|[1-9]\\d{1,2}|[12]\\d{3})", message = "Incorrect period format")
    private String period;

    @Column
    @PositiveOrZero(message = "The salary must be non-negative")
    private Long salary;

    public Payment() {
    }

    public Payment(String employee, String period, Long salary) {
        this.employee = employee;
        this.period = period;
        this.salary = salary;
    }
}
