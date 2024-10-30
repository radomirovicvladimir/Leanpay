package org.leanpay.demo.controller;

import org.leanpay.demo.dto.LoanCalculationRequest;
import org.leanpay.demo.dto.LoanCalculationResponse;
import org.leanpay.demo.service.LoanCalculationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/loans")
public class LoanCalculationController {

    @Autowired
    private LoanCalculationService loanCalculationService;

    @PostMapping("/calculate")
    public LoanCalculationResponse calculateLoan(@RequestBody LoanCalculationRequest request) {
        validateRequest(request);
        return loanCalculationService.calculateLoan(request);
    }

    private void validateRequest(LoanCalculationRequest request) {
        if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Loan amount must be greater than zero.");
        }
        if (request.getAnnualInterestRate() == null || request.getAnnualInterestRate().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Annual interest rate cannot be negative.");
        }
        if (request.getNumberOfMonths() <= 0) {
            throw new IllegalArgumentException("Number of months must be greater than zero.");
        }
    }
}
