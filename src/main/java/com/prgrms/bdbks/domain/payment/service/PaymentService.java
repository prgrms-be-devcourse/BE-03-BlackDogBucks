package com.prgrms.bdbks.domain.payment.service;

import com.prgrms.bdbks.domain.card.entity.Card;
import com.prgrms.bdbks.domain.order.entity.Order;

public interface PaymentService {
	String orderPay(Order order, Card card, int totalPrice);

	String chargePay(String cardId, int totalPrice);
}
