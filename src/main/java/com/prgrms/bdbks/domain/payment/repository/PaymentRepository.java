package com.prgrms.bdbks.domain.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prgrms.bdbks.domain.payment.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, String> {
}