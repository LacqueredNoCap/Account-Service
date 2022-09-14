package account.payload.request.dto;

import account.dto.Payment;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;

@Getter @Setter
public class PaymentRequest {

    @Email(regexp = "[\\w\\.]+@acme.com", message = "email must ends with \"@acme.com\"")
    private String employee;

    @Pattern(regexp = "(0[1-9]|1[0-2])-(\\d|[1-9]\\d{1,2}|[12]\\d{3})", message = "Incorrect period format")
    private String period;

    @PositiveOrZero(message = "The salary must be non-negative")
    private long salary;

}
