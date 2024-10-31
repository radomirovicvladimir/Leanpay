package org.leanpay.demo.controller;

import org.leanpay.demo.dto.LoanCalculationRequest;
import org.leanpay.demo.dto.LoanCalculationResponse;
import org.leanpay.demo.service.LoanCalculationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/loans")
public class LoanCalculationController {

    @Autowired
    private LoanCalculationService loanCalculationService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/calculate")
    public LoanCalculationResponse calculateLoan(@Valid @RequestBody LoanCalculationRequest request) {
        return loanCalculationService.calculateLoan(request);
    }
}
