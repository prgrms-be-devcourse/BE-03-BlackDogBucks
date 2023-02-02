package com.prgrms.bdbks.domain.payment.service;

import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.bdbks.domain.card.service.CardService;
import com.prgrms.bdbks.domain.coupon.service.CouponService;
import com.prgrms.bdbks.domain.order.entity.Order;
import com.prgrms.bdbks.domain.payment.dto.OrderPayment;
import com.prgrms.bdbks.domain.payment.dto.OrderRefundPayment;
import com.prgrms.bdbks.domain.payment.dto.PaymentChargeRequest;
import com.prgrms.bdbks.domain.payment.dto.PaymentRefundResult;
import com.prgrms.bdbks.domain.payment.model.PaymentResult;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentFacadeService {

	private final PaymentService paymentService;

	private final CardService cardService;

	private final CouponService couponService;

	@Transactional
	public PaymentResult orderPay(OrderPayment orderPayment) {
		Order order = orderPayment.getOrder();

		cardService.pay(order.getUserId(), orderPayment.getCardId(),
			order.getTotalPrice());

		if (Objects.nonNull(order.getCoupon())) {
			order.getCoupon().use();
		}

		return paymentService.orderPay(order, orderPayment.getCardId(),
			order.getTotalPrice());
	}

	@Transactional
	public PaymentResult chargePay(Long userId, PaymentChargeRequest paymentChargeRequest) {
		cardService.charge(userId, paymentChargeRequest.getCardId(), paymentChargeRequest.getAmount());

		return paymentService.chargePay(paymentChargeRequest.getCardId(), paymentChargeRequest.getAmount());

	}

	@Transactional
	public PaymentRefundResult orderPayRefund(OrderRefundPayment orderRefundPayment) {

		Long couponId = orderRefundPayment.getCouponId();

		if (Objects.nonNull(couponId)) {
			couponService.refundCoupon(couponId);
		}

		return paymentService.orderPayRefund(orderRefundPayment.getOrderId());

}
