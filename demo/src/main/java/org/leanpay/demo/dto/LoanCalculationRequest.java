package org.leanpay.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanCalculationRequest {
    private BigDecimal amount;
    private BigDecimal annualInterestRate;
    private int numberOfMonths;
}
