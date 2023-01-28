package com.prgrms.bdbks.domain.payment.entity;

import static com.prgrms.bdbks.domain.testutil.PaymentObjectProvider.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.prgrms.bdbks.common.exception.PaymentException;
import com.prgrms.bdbks.domain.order.entity.Order;
import com.prgrms.bdbks.domain.testutil.OrderObjectProvider;

class PaymentTest {

	private final Order order = OrderObjectProvider.createOrder();
	private final String validCardId = "cardId123";
	private final PaymentType paymentType = PaymentType.ORDER;
	private final int validPrice = 10000;
	private final LocalDateTime validPaymentDateTime = LocalDateTime.now();

	@DisplayName("validatePaymentType() - 결제 타입 검증 - 성공")
	@Test
	void validPaymentType_validPaymentType_ExceptionDoesNotThrown() {
		assertDoesNotThrow(() -> createOrderPayment(order, validCardId, paymentType, validPrice, validPaymentDateTime));
	}

	@DisplayName("validatePaymentType() - 결제 타입 검증 - 실패")
	@Test
	void validPaymentType_InvalidPaymentType_ExceptionThrown() {
		PaymentType invalidPaymentType = null;

		assertThrows(PaymentException.class,
			() -> createOrderPayment(order, validCardId, invalidPaymentType, validPrice, validPaymentDateTime));
	}

	@DisplayName("validatePrice() - 결제금액 검증 - 성공")
	@ParameterizedTest
	@ValueSource(ints = {2000, 3000, 5000})
	void validatePrice_ValidPrice_ExceptionDoesNotThrown(int price) {
		assertDoesNotThrow(
			() -> createOrderPayment(order, validCardId, paymentType, price, validPaymentDateTime));
	}

	@DisplayName("validatePrice() 결제금액 검증 - 실패")
	@ParameterizedTest
	@ValueSource(ints = {-2000, -3000, -5000})
	void validatePrice_InvalidPrice_ExceptionThrown(int price) {
		assertThrows(PaymentException.class,
			() -> createOrderPayment(order, validCardId, paymentType, price, validPaymentDateTime));
	}

	@DisplayName("validateCardId() - 충전카드Id 검증 - 성공")
	@Test
	void validCardId_validCardId_ExceptionDoesNotThrown() {
		PaymentType paymentType = PaymentType.ORDER;

		assertDoesNotThrow(() ->
			createOrderPayment(order, validCardId, paymentType, validPrice, validPaymentDateTime));
	}

	@DisplayName("validateCardId() - 충전카드Id 검증 - 실패")
	@Test
	void validCardId_invalidCardId_ExceptionThrown() {
		String invalidId = null;

		assertThrows(PaymentException.class,
			() -> createOrderPayment(order, invalidId, paymentType, validPrice, validPaymentDateTime));
	}

	@DisplayName("validatePaymentDateTime() - 결제 시간 검증 - 성공")
	@Test
	void validatePaymentDateTime_validPaymentDateTime_ExceptionDoesNotThrown() {
		assertDoesNotThrow(() -> createOrderPayment(order, validCardId, paymentType, validPrice, validPaymentDateTime));
	}

	@DisplayName("validatePaymentDateTime() - 결제 시간 검증 - 실패")
	@Test
	void validatePaymentDateTime_InvalidPaymentDateTime_ExceptionThrown() {
		LocalDateTime paymentDateTime = null;

		assertThrows(PaymentException.class,
			() -> createOrderPayment(order, validCardId, paymentType, validPrice, paymentDateTime));
	}

}