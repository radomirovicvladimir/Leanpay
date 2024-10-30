package org.leanpay.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.leanpay.demo.controller.LoanCalculationController;
import org.leanpay.demo.dto.LoanCalculationRequest;
import org.leanpay.demo.dto.LoanCalculationResponse;
import org.leanpay.demo.service.LoanCalculationService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class LoanCalculatorControllerTest {

    private MockMvc mockMvc;

    @Mock
    private LoanCalculationService loanCalculationService;

    @InjectMocks
    private LoanCalculationController loanCalculatorController;

    @Test
    void testCalculateLoan() throws Exception {
        // Arrange
        mockMvc = MockMvcBuilders.standaloneSetup(loanCalculatorController).build();

        LoanCalculationRequest request = new LoanCalculationRequest();
        request.setAmount(new BigDecimal("1000"));
        request.setAnnualInterestRate(new BigDecimal("5"));
        request.setNumberOfMonths(10);

        LoanCalculationResponse mockResponse = new LoanCalculationResponse();
        mockResponse.setAmount(request.getAmount());
        mockResponse.setAnnualInterestRate(request.getAnnualInterestRate());
        mockResponse.setNumberOfMonths(request.getNumberOfMonths());
        mockResponse.setMonthlyInstallment(new BigDecimal("102.31"));
        mockResponse.setTotalInterest(new BigDecimal("23.08"));
        mockResponse.setTotalPayment(new BigDecimal("1023.08"));
        mockResponse.setPaymentSchedule(Collections.emptyList());

        when(loanCalculationService.calculateLoan(any(LoanCalculationRequest.class)))
                .thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(post("/api/loans/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(1000))
                .andExpect(jsonPath("$.monthlyInstallment").value(102.31));
    }

    // Helper method to convert object to JSON string
    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
