package com.prgrms.bdbks.domain.payment.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.prgrms.bdbks.domain.payment.PaymentType;

class PaymentTest {

	private final Order order = new Order();
	private final Long validCardId = 1L;
	private final PaymentType paymentType = PaymentType.ORDER;
	private final int validPrice = 10000;
	private final LocalDateTime validPaymentDateTime = LocalDateTime.now();

	private Payment createPayment(Order order, Long cardId, PaymentType paymentType, int price,
		LocalDateTime paymentDateTime) {
		return Payment.builder()
			.order(order)
			.cardId(cardId)
			.paymentType(paymentType)
			.price(price)
			.paymentDateTime(paymentDateTime)
			.build();
	}

	@Test
	@DisplayName("validatePaymentType() - 결제 타입 검증 - 성공")
	void validPaymentType_validPaymentType_ExceptionDoesNotThrown() {
		assertDoesNotThrow(() -> createPayment(order, validCardId, paymentType, validPrice, validPaymentDateTime));
	}

	@Test
	@DisplayName("validatePaymentType() - 결제 타입 검증 - 실패")
	void validPaymentType_InvalidPaymentType_ExceptionThrown() {
		PaymentType invalidPaymentType = null;

		assertThrows(NullPointerException.class,
			() -> createPayment(order, validCardId, invalidPaymentType, validPrice, validPaymentDateTime));
	}

	@ParameterizedTest
	@ValueSource(ints = {2000, 3000, 5000})
	@DisplayName("validatePrice() - 결제금액 검증 - 성공")
	void validatePrice_ValidPrice_ExceptionDoesNotThrown(int price) {
		assertDoesNotThrow(
			() -> createPayment(order, validCardId, paymentType, price, validPaymentDateTime));
	}

	@ParameterizedTest
	@ValueSource(ints = {-2000, -3000, -5000})
	@DisplayName("validatePrice() 결제제겨금액 검증 - 실패")
	void validatePrice_InvalidPrice_ExceptionThrown(int price) {
		assertThrows(IllegalArgumentException.class,
			() -> createPayment(order, validCardId, paymentType, price, validPaymentDateTime));
	}

	@Test
	@DisplayName("validateCardId() - 충전카드Id 검증 - 성공")
	void validCardId_validCardId_ExceptionDoesNotThrown() {
		PaymentType paymentType = PaymentType.ORDER;

		assertDoesNotThrow(() ->
			createPayment(order, validCardId, paymentType, validPrice, validPaymentDateTime));
	}

	@Test
	@DisplayName("validateCardId() - 충전카드Id 검증 - 실패")
	void validCardId_invalidCardId_ExceptionThrown() {
		Long invalidId = null;

		assertThrows(NullPointerException.class,
			() -> createPayment(order, invalidId, paymentType, validPrice, validPaymentDateTime));
	}

	@Test
	@DisplayName("validatePaymentDateTime() - 결제 시간 검증 - 성공")
	void validatePaymentDateTime_validPaymentDateTime_ExceptionDoesNotThrown() {
		assertDoesNotThrow(() -> createPayment(order, validCardId, paymentType, validPrice, validPaymentDateTime));
	}

	@Test
	@DisplayName("validatePaymentDateTime() - 결제 시간 검증 - 실패")
	void validatePaymentDateTime_InvalidPaymentDateTime_ExceptionThrown() {
		LocalDateTime paymentDateTime = null;

		assertThrows(NullPointerException.class,
			() -> createPayment(order, validCardId, paymentType, validPrice, paymentDateTime));
	}

}