package org.leanpay.demo.persistence.repository;

import org.leanpay.demo.persistence.entity.LoanCalculation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanCalculationRepository extends JpaRepository<LoanCalculation, Long> {
}
