package org.leanpay.demo.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "payment_schedule", schema = "LEANPAY")
public class PaymentSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "loan_calculation_id", nullable = false)
    private LoanCalculation loanCalculation;

    @Column(name = "month_number")
    private int monthNumber;

    @Column(name = "payment_amount")
    private BigDecimal paymentAmount;

    @Column(name = "principal_paid")
    private BigDecimal principalPaid;

    @Column(name = "interest_paid")
    private BigDecimal interestPaid;

    @Column(name = "remaining_balance")
    private BigDecimal remainingBalance;

}
