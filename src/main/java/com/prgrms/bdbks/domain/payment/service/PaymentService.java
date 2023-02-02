package com.prgrms.bdbks.domain.payment.service;

import com.prgrms.bdbks.domain.order.entity.Order;
import com.prgrms.bdbks.domain.payment.dto.PaymentRefundResult;
import com.prgrms.bdbks.domain.payment.model.PaymentResult;

public interface PaymentService {
	PaymentResult orderPay(Order order, String cardId, int totalPrice);

	PaymentResult chargePay(String cardId, int totalPrice);

	PaymentRefundResult orderPayRefund(String orderId);

}
