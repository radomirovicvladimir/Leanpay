package org.leanpay.demo.service;

import org.leanpay.demo.dto.LoanCalculationRequest;
import org.leanpay.demo.dto.LoanCalculationResponse;
import org.leanpay.demo.persistence.entity.LoanCalculation;
import org.leanpay.demo.persistence.entity.PaymentSchedule;
import org.leanpay.demo.persistence.repository.LoanCalculationRepository;
import org.leanpay.demo.persistence.repository.PaymentScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class LoanCalculationService {

    @Autowired
    private LoanCalculationRepository loanCalculationRepository;

    @Autowired
    private PaymentScheduleRepository paymentScheduleRepository;

    @Transactional
    public LoanCalculationResponse calculateLoan(LoanCalculationRequest request) {
        BigDecimal monthlyInterestRate = request.getAnnualInterestRate()
                .divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP)
                .divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP);

        BigDecimal monthlyInstallment = request.getAmount()
                .multiply(monthlyInterestRate)
                .multiply(BigDecimal.ONE.add(monthlyInterestRate).pow(request.getNumberOfMonths()))
                .divide(BigDecimal.ONE.add(monthlyInterestRate).pow(request.getNumberOfMonths()).subtract(BigDecimal.ONE), 2, RoundingMode.HALF_UP);

        BigDecimal totalPayment = monthlyInstallment.multiply(BigDecimal.valueOf(request.getNumberOfMonths()));

        LoanCalculation loanCalculation = new LoanCalculation();
        loanCalculation.setAmount(request.getAmount());
        loanCalculation.setAnnualInterestRate(request.getAnnualInterestRate());
        loanCalculation.setNumberOfMonths(request.getNumberOfMonths());
        loanCalculation.setMonthlyInstallment(monthlyInstallment);
        loanCalculation.setTotalPayment(totalPayment);
        loanCalculation.setRequestTimestamp(LocalDateTime.now());

        List<LoanCalculationResponse.MonthlyPayment> paymentSchedule = generatePaymentSchedule(
                request.getAmount(),
                monthlyInterestRate,
                request.getNumberOfMonths(),
                monthlyInstallment
        );

        List<PaymentSchedule> paymentSchedules = new ArrayList<>();
        for (LoanCalculationResponse.MonthlyPayment payment : paymentSchedule) {
            PaymentSchedule schedule = new PaymentSchedule();
            schedule.setLoanCalculation(loanCalculation); // Set the parent LoanCalculation
            schedule.setMonthNumber(payment.getMonthNumber());
            schedule.setPaymentAmount(payment.getPaymentAmount());
            schedule.setPrincipalPaid(payment.getPrincipalPaid());
            schedule.setInterestPaid(payment.getInterestPaid());
            schedule.setRemainingBalance(payment.getRemainingBalance());

            paymentSchedules.add(schedule);
        }

        loanCalculation.setPaymentSchedules(paymentSchedules);
        loanCalculationRepository.save(loanCalculation);

        LoanCalculationResponse response = new LoanCalculationResponse();
        response.setAmount(request.getAmount());
        response.setAnnualInterestRate(request.getAnnualInterestRate());
        response.setNumberOfMonths(request.getNumberOfMonths());
        response.setMonthlyInstallment(monthlyInstallment);
        response.setTotalPayment(totalPayment);
        response.setPaymentSchedule(paymentSchedule);

        return response;
    }


    private List<LoanCalculationResponse.MonthlyPayment> generatePaymentSchedule(BigDecimal principal, BigDecimal monthlyInterestRate,
                                                                                 int numberOfMonths, BigDecimal monthlyInstallment) {
        List<LoanCalculationResponse.MonthlyPayment> schedule = new ArrayList<>();
        BigDecimal remainingBalance = principal;

        for (int month = 1; month <= numberOfMonths; month++) {
            BigDecimal interestPaid = remainingBalance.multiply(monthlyInterestRate).setScale(2, RoundingMode.HALF_UP);
            BigDecimal principalPaid = monthlyInstallment.subtract(interestPaid).setScale(2, RoundingMode.HALF_UP);
            remainingBalance = remainingBalance.subtract(principalPaid).setScale(2, RoundingMode.HALF_UP);

            if (month == numberOfMonths) {
                principalPaid = principalPaid.add(remainingBalance);
                remainingBalance = BigDecimal.ZERO;
            }

            LoanCalculationResponse.MonthlyPayment payment = new LoanCalculationResponse.MonthlyPayment();
            payment.setMonthNumber(month);
            payment.setPaymentAmount(monthlyInstallment);
            payment.setPrincipalPaid(principalPaid);
            payment.setInterestPaid(interestPaid);
            payment.setRemainingBalance(remainingBalance.max(BigDecimal.ZERO));

            schedule.add(payment);
        }

        return schedule;
    }
}
