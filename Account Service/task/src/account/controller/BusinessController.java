package account.controller;

import account.dto.Payment;
import account.payload.response.StatusResponse;
import account.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Validated
@RestController
@RequestMapping("/api")
public class BusinessController {

    private final PaymentService paymentService;

    public BusinessController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/empl/payment")
    public ResponseEntity<?> payment(
            @RequestParam(value = "period", required = false) String period,
            @AuthenticationPrincipal UserDetails userDetails) {
        if (period == null) {
            return ResponseEntity.ok(
                    paymentService.getAllPaymentResponsesByEmail(userDetails.getUsername()));
        }
        return ResponseEntity.ok(
                paymentService.getPaymentResponseByEmailAndPeriod(userDetails.getUsername(), period));
    }

    @PostMapping("/acct/payments")
    public ResponseEntity<?> uploadPayrolls(@RequestBody @NotEmpty List<@Valid Payment> payments) {
        paymentService.saveAllPayments(payments);
        return ResponseEntity.ok(new StatusResponse("Added successfully!"));
    }

    @PutMapping("/acct/payments")
    public ResponseEntity<?> changeSalary(@RequestBody @Valid Payment payment) {
        paymentService.updatePayment(payment);
        return ResponseEntity.ok(new StatusResponse("Updated successfully!"));
    }
}
