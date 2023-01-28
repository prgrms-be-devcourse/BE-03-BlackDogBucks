package com.prgrms.bdbks.domain.payment.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.bdbks.domain.card.entity.Card;
import com.prgrms.bdbks.domain.order.entity.Order;
import com.prgrms.bdbks.domain.payment.dto.PaymentChargeResponse;
import com.prgrms.bdbks.domain.payment.dto.PaymentOrderResponse;
import com.prgrms.bdbks.domain.payment.entity.Payment;
import com.prgrms.bdbks.domain.payment.model.PaymentResult;
import com.prgrms.bdbks.domain.payment.repository.PaymentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DefaultPaymentService implements PaymentService {

	private final PaymentRepository paymentRepository;

	@Override
	public PaymentResult orderPay(Order order, String cardId, int totalPrice) {
		Payment payment = Payment.createOrderPayment(order, cardId, totalPrice);
		paymentRepository.save(payment);

		return new PaymentResult(payment.getId());
	}

	@Override
	public PaymentResult chargePay(String cardId, int totalPrice) {
		Payment payment = Payment.createChargePayment(cardId, totalPrice);
		paymentRepository.save(payment);

		return new PaymentResult(payment.getId());
	}
}