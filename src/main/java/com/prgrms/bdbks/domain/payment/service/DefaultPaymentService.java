package com.prgrms.bdbks.domain.payment.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.bdbks.common.exception.EntityNotFoundException;
import com.prgrms.bdbks.domain.card.entity.Card;
import com.prgrms.bdbks.domain.card.repository.CardRepository;
import com.prgrms.bdbks.domain.order.entity.Order;
import com.prgrms.bdbks.domain.payment.dto.PaymentRefundResult;
import com.prgrms.bdbks.domain.payment.entity.Payment;
import com.prgrms.bdbks.domain.payment.model.PaymentResult;
import com.prgrms.bdbks.domain.payment.repository.PaymentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DefaultPaymentService implements PaymentService {

	private final PaymentRepository paymentRepository;
	private final CardRepository cardRepository;

	@Override
	@Transactional
	public PaymentResult orderPay(Order order, String cardId, int totalPrice) {
		Payment payment = Payment.createOrderPayment(order, cardId, totalPrice);
		paymentRepository.save(payment);

		return new PaymentResult(payment.getId());
	}

	@Override
	@Transactional
	public PaymentResult chargePay(String cardId, int totalPrice) {
		Payment payment = Payment.createChargePayment(cardId, totalPrice);
		paymentRepository.save(payment);

		return new PaymentResult(payment.getId());
	}

	@Override
	@Transactional
	public PaymentRefundResult orderPayRefund(String orderId) {

		Payment payment = paymentRepository.findByOrderId(orderId)
			.orElseThrow(() -> new EntityNotFoundException(Payment.class, orderId));

		Card card = cardRepository.findById(payment.getChargeCardId())
			.orElseThrow(() -> new EntityNotFoundException(Card.class, payment.getChargeCardId()));

		card.refundAmount(payment.getPrice());

		payment.refund();

		return new PaymentRefundResult(payment.getId(), payment.getChargeCardId(), payment.getPrice());

	}
}