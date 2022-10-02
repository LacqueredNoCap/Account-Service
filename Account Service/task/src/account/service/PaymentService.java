package account.service;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import account.dto.Payment;
import account.dto.User;
import account.payload.response.dto.PaymentResponse;
import account.repository.PaymentRepository;
import account.repository.UserRepository;

@Service
public class PaymentService {

    private static final String SALARY_PATTERN = "%d dollar(s) %d cent(s)";
    private static final String DATE_TIME_PATTERN = "MM-yyyy";

    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;

    public PaymentService(PaymentRepository paymentRepository, UserRepository userRepository) {
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void updatePayment(Payment updatedPayment) {
        Payment payment = paymentRepository.findPaymentByEmployeeIgnoreCaseAndPeriod(
                        updatedPayment.getEmployee(), updatedPayment.getPeriod())
                .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "User or period doesn't exist!"));

        payment.setSalary(updatedPayment.getSalary());
        paymentRepository.save(payment);
    }

    @Transactional
    public void savePayment(Payment payment) {
        validatePayment(payment);
        paymentRepository.save(payment);
    }

    private void validatePayment(Payment payment) {
        if (paymentRepository.existsPaymentByEmployeeIgnoreCaseAndPeriod(
                payment.getEmployee(), payment.getPeriod())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Payment already exists!");
        }
        if (!userRepository.existsUserByEmailIgnoreCase(payment.getEmployee())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "User doesn't exist!");
        }
    }

    @Transactional
    public void saveAllPayments(List<Payment> payments) {
        payments.forEach(this::savePayment);
    }

    public PaymentResponse getPaymentResponseByEmailAndPeriod(String email, String period) {
        Payment payment = this.getPaymentByEmailAndPeriod(email, period);

        return this.paymentMapToPaymentResponse(payment);
    }

    private Payment getPaymentByEmailAndPeriod(String email, String period) {
        return paymentRepository.findPaymentByEmployeeIgnoreCaseAndPeriod(email, period).orElseThrow(() ->
                new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Period has a wrong format!"
                ));
    }

    public List<PaymentResponse> getAllPaymentResponsesByEmail(String email) {
        List<Payment> payments = this.getAllPaymentsByEmail(email);

        return payments.stream()
                //.sorted(Comparator.comparing(Payment::getPeriod).reversed())
                .map(this::paymentMapToPaymentResponse)
                .collect(Collectors.toList());
    }

    private List<Payment> getAllPaymentsByEmail(String username) {
        return paymentRepository.findPaymentsByEmployeeIgnoreCaseOrderByPeriodDesc(username);
    }

    private PaymentResponse paymentMapToPaymentResponse(Payment payment) {
        User user = userRepository.findUserByEmailIgnoreCase(payment.getEmployee())
                .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                "User doesn't exist!")
                );

        DateTimeFormatter datePattern = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN, Locale.ENGLISH);
        YearMonth ym = YearMonth.parse(payment.getPeriod(), datePattern);
        String period = ym.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH) + "-" + ym.getYear();

        return new PaymentResponse(
                user.getName(),
                user.getLastname(),
                period,
                String.format(SALARY_PATTERN,
                        payment.getSalary() / 100, payment.getSalary() % 100
                )
        );
    }
}
