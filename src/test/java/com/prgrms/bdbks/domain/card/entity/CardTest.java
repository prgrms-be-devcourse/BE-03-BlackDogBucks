package com.prgrms.bdbks.domain.card.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.prgrms.bdbks.domain.payment.entity.User;

class CardTest {

	private final User user = new User();

	private Card createCard(User user) {
		return Card.builder()
			.user(user)
			.build();
	}

	@Test
	@DisplayName("validateUser() - 충전카드 생성 - 성공")
	void validateUser_validUser_ExceptionDoesNotThrown() {

		assertDoesNotThrow(() -> createCard(user));
	}

	@Test
	@DisplayName("validateUser() - 충전카드 생성 - 실패")
	void validateUser_InvalidUser_ExceptionThrown() {

		User invalidUser = null;
		assertThrows(NullPointerException.class, () -> createCard(invalidUser));
	}

	@ParameterizedTest
	@ValueSource(ints = {10000, 550000, 54321, 200000, 12345})
	@DisplayName("chargeAmount() - 카드금액 충전 - 성공")
	void validateAmount_validAmount_ExceptionDoesNotThrown(int amount) {
		Card card = createCard(user);
		assertDoesNotThrow(() -> card.chargeAmount(amount));
	}

	@ParameterizedTest
	@ValueSource(ints = {9999, 550001, 1000, -1000, -20000, -50000})
	@DisplayName("validateAmount() - 충전금액 검증 - 실패")
	void validateAmount_InvalidAmount_ExceptionThrown(int amount) {
		Card card = createCard(user);
		assertThrows(IllegalArgumentException.class, () -> card.chargeAmount(amount));
	}

	@ParameterizedTest
	@ValueSource(ints = {0, 100, 2000, 5, 10000, 200000, 12345})
	@DisplayName("chargeAmount() - 카드금액 충전 - 성공")
	void payAmount_validAmount_ExceptionDoesNotThrown(int amount) {
		Card card = createCard(user);
		card.chargeAmount(300000);
		assertDoesNotThrow(() -> card.payAmount(amount));
	}

	@ParameterizedTest
	@ValueSource(ints = {20000, 30000, 50000})
	@DisplayName("validateAmount() - 충전금액 검증 - 실패")
	void payAmount_InvalidAmount_ExceptionThrown(int amount) {
		Card card = createCard(user);
		card.chargeAmount(10000);
		assertThrows(IllegalArgumentException.class, () -> card.payAmount(amount));
	}
}