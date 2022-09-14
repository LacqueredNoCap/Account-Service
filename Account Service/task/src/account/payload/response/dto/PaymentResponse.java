package account.payload.response.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor
@Builder
public class PaymentResponse {

    private final String name;
    private final String lastname;
    private final String period;
    private final String salary;
}
