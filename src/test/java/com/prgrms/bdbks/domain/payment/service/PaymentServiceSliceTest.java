package com.prgrms.bdbks.domain.payment.service;

import static com.prgrms.bdbks.domain.testutil.PaymentObjectProvider.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.prgrms.bdbks.domain.card.entity.Card;
import com.prgrms.bdbks.domain.order.entity.Order;
import com.prgrms.bdbks.domain.payment.entity.Payment;
import com.prgrms.bdbks.domain.payment.entity.PaymentType;
import com.prgrms.bdbks.domain.payment.repository.PaymentRepository;
import com.prgrms.bdbks.domain.testutil.CardObjectProvider;
import com.prgrms.bdbks.domain.testutil.OrderObjectProvider;
import com.prgrms.bdbks.domain.testutil.UserObjectProvider;
import com.prgrms.bdbks.domain.user.entity.User;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceSliceTest {

	@Mock
	private PaymentRepository paymentRepository;

	@InjectMocks
	private DefaultPaymentService defaultPaymentService;

	@DisplayName("orderPay - 사용자의 충전카드로 주문 금액을 결제할 수 있다. - 성공")
	@Test
	void orderPay_ValidParameters_Success() {
		Long userId = 1L;
		String cardId = "cardID";
		int totalPrice = 50000;

		User user = UserObjectProvider.createUser(userId);
		Card card = CardObjectProvider.createCard(user, cardId);
		Order order = OrderObjectProvider.createOrder();
		Payment payment = createChargePayment(cardId, PaymentType.ORDER, totalPrice, LocalDateTime.now());

		when(paymentRepository.save(any())).thenReturn(payment);

		defaultPaymentService.orderPay(order, card, totalPrice);

		verify(paymentRepository).save(any());
	}

	@DisplayName("chargePay - 사용자의 충전카드에 금액을 충전할 수 있다. - 성공")
	@Test
	void chargePay() {

		//given
		String cardId = "cardID";
		int totalPrice = 10000;
		Payment payment = createChargePayment(cardId, PaymentType.CHARGE, totalPrice, LocalDateTime.now());

		when(paymentRepository.save(any())).thenReturn(payment);

		//when
		defaultPaymentService.chargePay(cardId, totalPrice);

		//then
		verify(paymentRepository).save(any());

	}
}
