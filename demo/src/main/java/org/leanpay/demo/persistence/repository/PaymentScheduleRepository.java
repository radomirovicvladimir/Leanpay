package org.leanpay.demo.persistence.repository;

import org.leanpay.demo.persistence.entity.PaymentSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentScheduleRepository extends JpaRepository<PaymentSchedule, Long> {
}