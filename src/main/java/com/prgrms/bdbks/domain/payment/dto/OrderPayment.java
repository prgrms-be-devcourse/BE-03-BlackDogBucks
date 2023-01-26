package com.prgrms.bdbks.domain.payment.dto;

import static com.google.common.base.Preconditions.*;

import java.util.Objects;

import com.prgrms.bdbks.common.exception.PaymentException;
import com.prgrms.bdbks.domain.order.entity.Order;
import com.prgrms.bdbks.domain.payment.entity.PaymentType;

import lombok.Getter;

@Getter
public class OrderPayment {

	private final Order order;

	private final PaymentType paymentType;

	private final String cardId;

	public OrderPayment(Order order, String cardId, PaymentType paymentType) {
		validateOrder(order);
		validateCardId(cardId);
		validatePaymentType(paymentType);

		this.order = order;
		this.cardId = cardId;
		this.paymentType = paymentType;
	}

	private void validateOrder(Order order) {
		checkNotNull(order, "주문 정보를 입력해주세요.");
	}

	private void validatePaymentType(PaymentType paymentType) {
		if (Objects.isNull(paymentType)) {
			throw new PaymentException("결제 타입을 입력해주세요");
		}
	}

	private void validateCardId(String cardId) {
		if (Objects.isNull(cardId)) {
			throw new PaymentException("카드 아이디를 입력해주세요");
		}
	}
}
