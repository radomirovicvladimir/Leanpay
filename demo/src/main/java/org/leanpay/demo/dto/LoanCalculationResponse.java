package org.leanpay.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanCalculationResponse {
    private BigDecimal amount;
    private BigDecimal annualInterestRate;
    private int numberOfMonths;
    private BigDecimal monthlyInstallment;
    private BigDecimal totalPayment;
    private List<MonthlyPayment> paymentSchedule;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MonthlyPayment {
        private int monthNumber;
        private BigDecimal paymentAmount;
        private BigDecimal principalPaid;
        private BigDecimal interestPaid;
        private BigDecimal remainingBalance;
    }
}
