package com.prgrms.bdbks.domain.testutil;

import java.time.LocalDateTime;

import com.prgrms.bdbks.domain.order.entity.Order;
import com.prgrms.bdbks.domain.payment.entity.Payment;
import com.prgrms.bdbks.domain.payment.entity.PaymentType;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentObjectProvider {

	public static Payment createPayment(Order order, String cardId, PaymentType paymentType, int price,
		LocalDateTime paymentDateTime) {
		return Payment.builder()
			.order(order)
			.cardId(cardId)
			.paymentType(paymentType)
			.price(price)
			.paymentDateTime(paymentDateTime)
			.build();
	}
}
