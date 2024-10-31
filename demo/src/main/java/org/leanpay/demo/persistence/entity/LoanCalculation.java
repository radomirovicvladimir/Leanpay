package org.leanpay.demo.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "loan_calculation", schema = "LEANPAY")
public class LoanCalculation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal amount;

    @Column(name = "annual_interest_rate")
    private BigDecimal annualInterestRate;

    @Column(name = "number_of_months")
    private int numberOfMonths;

    @Column(name = "monthly_installment")
    private BigDecimal monthlyInstallment;

    @Column(name = "total_payment")
    private BigDecimal totalPayment;

    @Column(name = "request_timestamp")
    private LocalDateTime requestTimestamp;

    @OneToMany(mappedBy = "loanCalculation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PaymentSchedule> paymentSchedules;

}
