package org.leanpay.demo;

import org.junit.jupiter.api.Test;
import org.leanpay.demo.dto.LoanCalculationRequest;
import org.leanpay.demo.dto.LoanCalculationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class LoanCalculatorControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testCalculateLoanIntegration() {
        // Arrange
        LoanCalculationRequest request = new LoanCalculationRequest();
        request.setAmount(new BigDecimal("1000"));
        request.setAnnualInterestRate(new BigDecimal("5"));
        request.setNumberOfMonths(10);

        // Act
        LoanCalculationResponse response = restTemplate.postForObject(
                "/api/loans/calculate", request, LoanCalculationResponse.class);

        // Assert
        assertNotNull(response);
        assertEquals(new BigDecimal("1000"), response.getAmount());
        assertEquals(new BigDecimal("5"), response.getAnnualInterestRate());
        assertEquals(10, response.getNumberOfMonths());
        assertEquals(new BigDecimal("102.31"), response.getMonthlyInstallment());
        assertEquals(new BigDecimal("23.10"), response.getTotalInterest());
        assertEquals(new BigDecimal("1023.10"), response.getTotalPayment());
        assertNotNull(response.getPaymentSchedule());
        assertEquals(10, response.getPaymentSchedule().size());
    }
}
