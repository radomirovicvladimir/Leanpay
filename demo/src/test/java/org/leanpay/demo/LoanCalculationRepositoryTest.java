package org.leanpay.demo;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.leanpay.demo.persistence.entity.LoanCalculation;
import org.leanpay.demo.persistence.repository.LoanCalculationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class LoanCalculationRepositoryTest {

    @Autowired
    private LoanCalculationRepository loanCalculationRepository;

    private LoanCalculation loanCalculation;

    @BeforeEach
    void setUp() {
        loanCalculation = new LoanCalculation();
        loanCalculation.setAmount(new BigDecimal("1000"));
        loanCalculation.setAnnualInterestRate(new BigDecimal("5"));
        loanCalculation.setNumberOfMonths(10);
        loanCalculation.setMonthlyInstallment(new BigDecimal("102.31"));
        loanCalculation.setTotalInterest(new BigDecimal("23.08"));
        loanCalculation.setTotalPayment(new BigDecimal("1023.08"));
        loanCalculation.setRequestTimestamp(LocalDateTime.now());

        loanCalculation = loanCalculationRepository.save(loanCalculation);
    }

    @AfterEach
    void tearDown() {
        loanCalculationRepository.deleteById(loanCalculation.getId());
    }

    @Test
    void testSaveAndFindLoanCalculation() {
        LoanCalculation foundCalculation = loanCalculationRepository.findById(loanCalculation.getId()).orElse(null);

        assertNotNull(foundCalculation);
        assertEquals(loanCalculation.getId(), foundCalculation.getId());
        assertEquals(new BigDecimal("1000.00").setScale(2), foundCalculation.getAmount());
    }
}
