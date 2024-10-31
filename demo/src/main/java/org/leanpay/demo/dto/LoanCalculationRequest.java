package org.leanpay.demo.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanCalculationRequest {

    @NotNull(message = "Loan amount must not be null.")
    @DecimalMin(value = "0.01", message = "Loan amount must be greater than zero.")
    private BigDecimal amount;

    @NotNull(message = "Annual interest rate must not be null.")
    @DecimalMin(value = "0.0", inclusive = false, message = "Annual interest rate must be greater than zero.")
    private BigDecimal annualInterestRate;

    @NotNull(message = "Number of months must not be null.")
    @Positive(message = "Number of months must be greater than zero.")
    private int numberOfMonths;
}
