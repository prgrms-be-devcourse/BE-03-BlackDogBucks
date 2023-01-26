package com.prgrms.bdbks.domain.payment.service;

import com.prgrms.bdbks.domain.card.entity.Card;
import com.prgrms.bdbks.domain.order.entity.Order;
import com.prgrms.bdbks.domain.payment.dto.PaymentChargeResponse;
import com.prgrms.bdbks.domain.payment.dto.PaymentOrderResponse;

public interface PaymentService {
	PaymentOrderResponse orderPay(Order order, Card card, int totalPrice);

	PaymentChargeResponse chargePay(String cardId, int totalPrice);
}
