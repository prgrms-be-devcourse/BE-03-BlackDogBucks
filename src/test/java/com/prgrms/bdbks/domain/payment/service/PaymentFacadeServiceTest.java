package com.prgrms.bdbks.domain.payment.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.prgrms.bdbks.domain.card.dto.CardChargeResponse;
import com.prgrms.bdbks.domain.card.dto.CardPayResponse;
import com.prgrms.bdbks.domain.card.service.CardService;
import com.prgrms.bdbks.domain.order.entity.Order;
import com.prgrms.bdbks.domain.payment.dto.PaymentChargeRequest;
import com.prgrms.bdbks.domain.payment.dto.PaymentOrderRequest;
import com.prgrms.bdbks.domain.payment.model.PaymentResult;
import com.prgrms.bdbks.domain.testutil.OrderObjectProvider;

@ExtendWith(MockitoExtension.class)
class PaymentFacadeServiceTest {

	@Mock
	private PaymentService paymentService;

	@Mock
	private CardService cardService;

	@InjectMocks
	private PaymentFacadeService paymentFacadeService;

	@DisplayName("orderPay - 주문에 대한 결제를 처리할 수 있다. - 성공 ")
	@Test
	void orderPay_ValidParameters_Success() {
		//given
		int totalPrice = 20000;
		Long userId = 1L;
		String cardId = "CardId";
		boolean couponUsed = false;
		int count = 3;

		Order order = OrderObjectProvider.createOrder();

		PaymentOrderRequest paymentOrderRequest = new PaymentOrderRequest(totalPrice, userId, cardId, couponUsed,
			count);

		String paymentId = "PaymentId";
		PaymentResult paymentResult = new PaymentResult(paymentId);
		CardPayResponse cardPayResponse = new CardPayResponse(cardId, totalPrice);

		when(cardService.pay(userId, cardId, totalPrice)).thenReturn(cardPayResponse);
		when(paymentService.orderPay(order, cardId, totalPrice)).thenReturn(paymentResult);

		//when
		PaymentResult result = paymentFacadeService.orderPay(paymentOrderRequest, order, null);

		//then
		verify(cardService).pay(userId, cardId, totalPrice);
		verify(paymentService).orderPay(order, cardId, totalPrice);

		assertThat(result)
			.hasFieldOrPropertyWithValue("paymentId", paymentResult.getPaymentId());
	}

	@DisplayName("chargePay - 카드 충전 결제를 진행할 수 있다. - 성공")
	@Test
	void chargePay_ValidParameters_Success() {

		//given
		Long userId = 1L;
		String cardId = "cardId";
		int amount = 40000;
		String paymentId = "PaymentId";
		CardChargeResponse cardChargeResponse = new CardChargeResponse(cardId, amount);
		PaymentChargeRequest paymentChargeRequest = new PaymentChargeRequest(amount, cardId);
		PaymentResult paymentResult = new PaymentResult(paymentId);

		when(cardService.charge(userId, cardId, amount)).thenReturn(cardChargeResponse);
		when(paymentService.chargePay(cardId, amount)).thenReturn(paymentResult);

		//when
		PaymentResult result = paymentFacadeService.chargePay(userId, paymentChargeRequest);

		//then
		verify(cardService).charge(userId, cardId, amount);
		verify(paymentService).chargePay(cardId, amount);

		assertThat(result)
			.hasFieldOrPropertyWithValue("paymentId", paymentResult.getPaymentId());

	}
}