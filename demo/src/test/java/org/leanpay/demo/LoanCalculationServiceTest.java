package org.leanpay.demo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.leanpay.demo.dto.LoanCalculationRequest;
import org.leanpay.demo.dto.LoanCalculationResponse;
import org.leanpay.demo.persistence.entity.LoanCalculation;
import org.leanpay.demo.persistence.repository.LoanCalculationRepository;
import org.leanpay.demo.persistence.repository.PaymentScheduleRepository;
import org.leanpay.demo.service.LoanCalculationService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoanCalculationServiceTest {

    @Mock
    private LoanCalculationRepository loanCalculationRepository;

    @Mock
    private PaymentScheduleRepository paymentScheduleRepository;

    @InjectMocks
    private LoanCalculationService loanCalculationService;

    @Test
    void testCalculateLoan() {
        // Arrange
        LoanCalculationRequest request = new LoanCalculationRequest();
        request.setAmount(new BigDecimal("1000").setScale(2, RoundingMode.HALF_UP));
        request.setAnnualInterestRate(new BigDecimal("5").setScale(2, RoundingMode.HALF_UP));
        request.setNumberOfMonths(10);

        LoanCalculation mockLoanCalculation = new LoanCalculation();
        mockLoanCalculation.setId(1L);
        mockLoanCalculation.setAmount(request.getAmount());
        mockLoanCalculation.setAnnualInterestRate(request.getAnnualInterestRate());
        mockLoanCalculation.setNumberOfMonths(request.getNumberOfMonths());
        mockLoanCalculation.setMonthlyInstallment(new BigDecimal("102.31").setScale(2, RoundingMode.HALF_UP));
        mockLoanCalculation.setTotalInterest(new BigDecimal("23.08").setScale(2, RoundingMode.HALF_UP));
        mockLoanCalculation.setTotalPayment(new BigDecimal("1023.08").setScale(2, RoundingMode.HALF_UP));
        mockLoanCalculation.setRequestTimestamp(LocalDateTime.now());

        // Mock repository behavior
        when(loanCalculationRepository.save(any(LoanCalculation.class)))
                .thenReturn(mockLoanCalculation);
        when(paymentScheduleRepository.saveAll(any()))
                .thenReturn(Collections.emptyList());

        // Act
        LoanCalculationResponse response = loanCalculationService.calculateLoan(request);

        // Assert
        assertNotNull(response);
        assertEquals(new BigDecimal("1000.00").setScale(2, RoundingMode.HALF_UP), response.getAmount());
        assertEquals(new BigDecimal("5.00").setScale(2, RoundingMode.HALF_UP), response.getAnnualInterestRate());
        assertEquals(10, response.getNumberOfMonths());
        assertEquals(new BigDecimal("102.31").setScale(2, RoundingMode.HALF_UP), response.getMonthlyInstallment());
        assertEquals(new BigDecimal("23.10").setScale(2, RoundingMode.HALF_UP), response.getTotalInterest());
        assertEquals(new BigDecimal("1023.10").setScale(2, RoundingMode.HALF_UP), response.getTotalPayment());
        assertNotNull(response.getPaymentSchedule());
        assertEquals(10, response.getPaymentSchedule().size());
    }
}