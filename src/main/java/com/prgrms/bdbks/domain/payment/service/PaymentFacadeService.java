package com.prgrms.bdbks.domain.payment.service;

import java.util.Objects;

import org.springframework.stereotype.Service;

import com.prgrms.bdbks.domain.card.service.CardService;
import com.prgrms.bdbks.domain.order.entity.Order;
import com.prgrms.bdbks.domain.payment.dto.OrderPayment;
import com.prgrms.bdbks.domain.payment.dto.PaymentChargeRequest;
import com.prgrms.bdbks.domain.payment.model.PaymentResult;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentFacadeService {

	private final PaymentService paymentService;

	private final CardService cardService;

	public PaymentResult orderPay(OrderPayment orderPayment) {
		Order order = orderPayment.getOrder();

		cardService.pay(order.getUserId(), orderPayment.getCardId(),
			order.getTotalPrice());

		if (Objects.nonNull(order.getCoupon())) {
			order.getCoupon().useCoupon();
		}

		return paymentService.orderPay(order, orderPayment.getCardId(),
			order.getTotalPrice());
	}

	public PaymentResult chargePay(Long userId, PaymentChargeRequest paymentChargeRequest) {
		cardService.charge(userId, paymentChargeRequest.getCardId(), paymentChargeRequest.getAmount());

		return paymentService.chargePay(paymentChargeRequest.getCardId(), paymentChargeRequest.getAmount());

	}
}
