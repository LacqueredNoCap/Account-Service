package account.dto;

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
    private long id;

    @Column
    private String employee;

    @Column
    private String period;

    @Column
    private long salary;

    public Payment() {
    }

    public Payment(String employee, String period, long salary) {
        this.employee = employee;
        this.period = period;
        this.salary = salary;
    }
}
