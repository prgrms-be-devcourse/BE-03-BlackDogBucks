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
import com.prgrms.bdbks.domain.card.service.CardService;
import com.prgrms.bdbks.domain.payment.dto.PaymentChargeRequest;
import com.prgrms.bdbks.domain.payment.model.PaymentResult;

@ExtendWith(MockitoExtension.class)
class PaymentFacadeServiceTest {

	@Mock
	private PaymentService paymentService;

	@Mock
	private CardService cardService;

	@InjectMocks
	private PaymentFacadeService paymentFacadeService;

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
		PaymentChargeRequest paymentChargeRequest = new PaymentChargeRequest(amount, cardId);

		when(cardService.charge(userId, cardId, amount)).thenThrow(EntityNotFoundException.class);

		//when
		assertThrows(EntityNotFoundException.class, () -> paymentFacadeService.chargePay(userId, paymentChargeRequest));

		//then
		verify(cardService).charge(userId, cardId, amount);

	}

}