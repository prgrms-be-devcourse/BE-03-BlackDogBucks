package com.prgrms.bdbks.domain.card.entity;

import static com.prgrms.bdbks.domain.testutil.CardObjectProvider.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.prgrms.bdbks.domain.user.entity.User;
import com.prgrms.bdbks.domain.user.role.Role;

class CardTest {

	private final User user = User.builder()
		.id(1L)
		.loginId("loginUser1")
		.password("password1")
		.nickname("nickname1")
		.email("mailmail@naver.com")
		.birthDate(LocalDate.now().minusYears(10L))
		.phone("01012341234")
		.role(Role.USER)
		.build();

	@DisplayName("validateUser() - 유저의 정보가 유효하면 충전카드가 생성된다. - 성공")
	@Test
	void validateUser_validUser_ExceptionDoesNotThrown() {

		assertDoesNotThrow(() -> createCard(user));
	}

	@DisplayName("validateUser() - 유저 객체가 null인 경우 충전카드를 생성할 수 없다. - 실패")
	@Test
	void validateUser_InvalidUser_ExceptionThrown() {

		User invalidUser = null;
		assertThrows(NullPointerException.class, () -> createCard(invalidUser));
	}

	@DisplayName("chargeAmount() - 10000원 ~ 550000원 사이의 금액을 충전할 수 있다. - 성공")
	@ParameterizedTest
	@ValueSource(ints = {10000, 550000, 54321, 200000, 12345})
	void validateAmount_validAmount_ExceptionDoesNotThrown(int amount) {
		Card card = createCard(user);
		assertDoesNotThrow(() -> card.chargeAmount(amount));
	}

	@DisplayName("validateAmount() - 10000원 ~ 550000원의 범위를 벗어난 금액은 충전할 수 없다. - 실패")
	@ParameterizedTest
	@ValueSource(ints = {9999, 550001, 1000, -1000, -20000, -50000})
	void validateAmount_InvalidAmount_ExceptionThrown(int amount) {
		Card card = createCard(user);
		assertThrows(IllegalArgumentException.class, () -> card.chargeAmount(amount));
	}

	@DisplayName("chargeAmount() - 보유한 금액보다 적은 금액을 결제할 경우 결제할 수 있다. - 성공")
	@ParameterizedTest
	@ValueSource(ints = {0, 100, 2000, 5, 10000, 200000, 12345})
	void payAmount_validAmount_ExceptionDoesNotThrown(int amount) {
		Card card = createCard(user);
		card.chargeAmount(300000);
		assertDoesNotThrow(() -> card.payAmount(amount));
	}

	@DisplayName("validateAmount() - 보유한 금액보다 더 많은 금액을 결제할 경우 결제에 실패한다. - 실패")
	@ParameterizedTest
	@ValueSource(ints = {20000, 30000, 50000})
	void payAmount_InvalidAmount_ExceptionThrown(int amount) {
		Card card = createCard(user);
		card.chargeAmount(10000);
		assertThrows(IllegalArgumentException.class, () -> card.payAmount(amount));
	}
}