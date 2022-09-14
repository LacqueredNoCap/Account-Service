package account.controller;

import account.dto.Payment;
import account.payload.request.dto.PaymentRequest;
import account.payload.response.StatusResponse;
import account.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.stream.Collectors;

@Validated
@RestController
@RequestMapping("/api")
public class BusinessController {

    private final PaymentService paymentService;

    public BusinessController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/empl/payment")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ACCOUNTANT')")
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
    @PreAuthorize("hasRole('ROLE_ACCOUNTANT')")
    public ResponseEntity<?> uploadPayrolls(@RequestBody @NotEmpty List<@Valid PaymentRequest> paymentsRequest) {
        List<Payment> payments = paymentsRequest.stream()
                .map(paymentRequest -> new Payment(
                        paymentRequest.getEmployee(),
                        paymentRequest.getPeriod(),
                        paymentRequest.getSalary()))
                .collect(Collectors.toList());
        paymentService.saveAllPayments(payments);
        return ResponseEntity.ok(new StatusResponse("Added successfully!"));
    }

    @PutMapping("/acct/payments")
    @PreAuthorize("hasRole('ROLE_ACCOUNTANT')")
    public ResponseEntity<?> changeSalary(@RequestBody @Valid Payment payment) {
        paymentService.updatePayment(payment);
        return ResponseEntity.ok(new StatusResponse("Updated successfully!"));
    }
}
