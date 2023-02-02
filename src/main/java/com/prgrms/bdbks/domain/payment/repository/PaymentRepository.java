package com.prgrms.bdbks.domain.payment.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.prgrms.bdbks.domain.payment.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, String> {

	Optional<Payment> findByOrderId(@Param("orderId") String orderId);

}