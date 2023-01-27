package com.prgrms.bdbks.domain.payment.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.prgrms.bdbks.common.exception.EntityNotFoundException;
import com.prgrms.bdbks.domain.card.dto.CardChargeResponse;
import com.prgrms.bdbks.domain.card.dto.CardPayResponse;
import com.prgrms.bdbks.domain.card.service.CardService;
import com.prgrms.bdbks.domain.order.entity.Order;
import com.prgrms.bdbks.domain.payment.dto.OrderPayment;
import com.prgrms.bdbks.domain.payment.dto.PaymentChargeRequest;
import com.prgrms.bdbks.domain.payment.entity.PaymentType;
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
		Long userId = 1L;
		String cardId = "CardId";
		Order order = OrderObjectProvider.createOrder();

		OrderPayment orderPayment = new OrderPayment(order, cardId, PaymentType.ORDER);

		String paymentId = "PaymentId";
		PaymentResult paymentResult = new PaymentResult(paymentId);
		CardPayResponse cardPayResponse = new CardPayResponse(cardId, order.getTotalPrice());

		when(cardService.pay(userId, cardId, order.getTotalPrice())).thenReturn(cardPayResponse);
		when(paymentService.orderPay(order, cardId, order.getTotalPrice())).thenReturn(paymentResult);

		//when
		PaymentResult result = paymentFacadeService.orderPay(orderPayment);

		//then
		verify(cardService).pay(userId, cardId, order.getTotalPrice());
		verify(paymentService).orderPay(order, cardId, order.getTotalPrice());

		assertThat(result)
			.hasFieldOrPropertyWithValue("paymentId", paymentResult.getPaymentId());
	}

	@DisplayName("orderPay - 미등록 카드로는 결제할 수 없다. - 실패 ")
	@Test
	void orderPay_InvalidCardId_Fail() {
		//given
		Long userId = 1L;
		String cardId = "unKnownId";
		Order order = OrderObjectProvider.createOrder();

		OrderPayment orderPayment = new OrderPayment(order, cardId, PaymentType.ORDER);

		when(cardService.pay(userId, cardId, order.getTotalPrice())).thenThrow(EntityNotFoundException.class);

		//when & then
		assertThrows(EntityNotFoundException.class, () -> {
			paymentFacadeService.orderPay(orderPayment);
		});

		verify(cardService).pay(userId, cardId, order.getTotalPrice());
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

	@DisplayName("chargePay - 카드 충전 결제를 진행할 수 있다. - 실패")
	@Test
	void chargePay_InValidParameters_Fail() {

		//given
		Long userId = 1L;
		String cardId = "cardId";
		int amount = 40000;
		String paymentId = "PaymentId";
		PaymentChargeRequest paymentChargeRequest = new PaymentChargeRequest(amount, cardId);
		PaymentResult paymentResult = new PaymentResult(paymentId);

		when(cardService.charge(userId, cardId, amount)).thenThrow(EntityNotFoundException.class);

		//when
		assertThrows(EntityNotFoundException.class, () -> {
			paymentFacadeService.chargePay(userId, paymentChargeRequest);
		});

		//then
		verify(cardService).charge(userId, cardId, amount);

	}

}