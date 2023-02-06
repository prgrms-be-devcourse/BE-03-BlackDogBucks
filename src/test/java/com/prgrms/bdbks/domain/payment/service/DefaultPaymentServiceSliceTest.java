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

import com.prgrms.bdbks.domain.order.entity.Order;
import com.prgrms.bdbks.domain.payment.entity.Payment;
import com.prgrms.bdbks.domain.payment.entity.PaymentType;
import com.prgrms.bdbks.domain.payment.repository.PaymentRepository;
import com.prgrms.bdbks.domain.testutil.OrderObjectProvider;
import com.prgrms.bdbks.domain.testutil.UserObjectProvider;
import com.prgrms.bdbks.domain.user.entity.User;

@ExtendWith(MockitoExtension.class)
public class DefaultPaymentServiceSliceTest {

	@Mock
	private PaymentRepository paymentRepository;

	@InjectMocks
	private DefaultPaymentService defaultPaymentService;

	@DisplayName("orderPay - 사용자의 충전카드 주문 결제 내역을 만들 수 있다. - 성공")
	@Test
	void orderPay_ValidParameters_Success() {
		Long userId = 1L;
		String cardId = "cardID";
		int totalPrice = 50000;

		User user = UserObjectProvider.createUser(userId);
		Order order = OrderObjectProvider.createOrder();
		Payment payment = createChargePayment(cardId, PaymentType.ORDER, totalPrice, LocalDateTime.now());

		when(paymentRepository.save(any())).thenReturn(payment);

		defaultPaymentService.orderPay(order, cardId, totalPrice);

		verify(paymentRepository).save(any());
	}

	@DisplayName("chargePay - 사용자의 충전카드 충전 결제 내역을 만들 수 있다. - 성공")
	@Test
	void chargePay_ValidParameters_Success() {
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
