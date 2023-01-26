package com.prgrms.bdbks.domain.payment.service;

import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.bdbks.domain.card.service.CardService;
import com.prgrms.bdbks.domain.coupon.entity.Coupon;
import com.prgrms.bdbks.domain.order.entity.Order;
import com.prgrms.bdbks.domain.payment.dto.PaymentChargeRequest;
import com.prgrms.bdbks.domain.payment.dto.PaymentOrderRequest;
import com.prgrms.bdbks.domain.payment.model.PaymentResult;

import lombok.RequiredArgsConstructor;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class PaymentFacadeService {

	private final PaymentService paymentService;
	private final CardService cardService;

	@Transactional
	public PaymentResult orderPay(PaymentOrderRequest paymentOrderRequest, Order order, Coupon coupon) {

		cardService.pay(paymentOrderRequest.getUserId(), paymentOrderRequest.getCardId(),
			paymentOrderRequest.getTotalPrice());

		if (Objects.nonNull(coupon)) {
			coupon.useCoupon();
		}

		return paymentService.orderPay(order, paymentOrderRequest.getCardId(), paymentOrderRequest.getTotalPrice());

	}

	@Transactional
	public PaymentResult chargePay(Long userId, PaymentChargeRequest paymentChargeRequest) {
		cardService.charge(userId, paymentChargeRequest.getCardId(), paymentChargeRequest.getAmount());

		return paymentService.chargePay(paymentChargeRequest.getCardId(), paymentChargeRequest.getAmount());

	}
}
