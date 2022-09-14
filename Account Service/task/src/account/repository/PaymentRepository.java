package account.repository;

import account.dto.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    boolean existsPaymentByEmployeeIgnoreCaseAndPeriod(String employee, String period);

    Optional<Payment> findPaymentByEmployeeIgnoreCaseAndPeriod(String employee, String period);

    List<Payment> findPaymentsByEmployeeIgnoreCaseOrderByPeriodDesc(String employee);

}
