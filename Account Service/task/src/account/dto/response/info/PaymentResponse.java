package account.dto.response.info;

import lombok.Getter;

@Getter
public class PaymentResponse {

    private final String name;
    private final String lastname;
    private final String period;
    private final String salary;

    public PaymentResponse(String name, String lastname, String period, String salary) {
        this.name = name;
        this.lastname = lastname;
        this.period = period;
        this.salary = salary;
    }
}
