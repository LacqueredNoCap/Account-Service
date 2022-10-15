package account.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "payments")
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
    private Long id;

    @Column
    private String employee;

    @Column
    private String period;

    @Column
    private Long salary;

    public Payment() {
    }

    public Payment(String employee, String period, Long salary) {
        this.employee = employee;
        this.period = period;
        this.salary = salary;
    }
}
