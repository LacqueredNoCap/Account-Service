package account.repository;

import java.util.List;
import java.util.Optional;

import account.dto.Payment;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    boolean existsPaymentByEmployeeIgnoreCaseAndPeriod(String employee, String period);

    Optional<Payment> findPaymentByEmployeeIgnoreCaseAndPeriod(String employee, String period);

    List<Payment> findPaymentsByEmployeeIgnoreCaseOrderByPeriodDesc(String employee);

}
