package com.prgrms.bdbks.domain.coupon.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class CouponTest {

	private final Long validId = 1L;
	private final String validName = "쿠폰1";
	private final int validPrice = 10000;
	private final LocalDateTime validExpireDate = LocalDateTime.now();

	private Coupon createCoupon(Long userId, String name, int price, LocalDateTime expireDate) {
		return Coupon.builder().userId(userId)
			.name(name)
			.price(price)
			.expireDate(expireDate)
			.build();
	}

	@ParameterizedTest
	@ValueSource(ints = {2000, 3000, 5000})
	@DisplayName("validatePrice() - 쿠폰금액 검증 - 성공")
	void validatePrice_ValidPrice_ExceptionDoesNotThrown(int price) {
		assertDoesNotThrow(() -> createCoupon(validId, validName, price, validExpireDate.plusSeconds(1L)));
	}

	@ParameterizedTest
	@ValueSource(ints = {-2000, -3000, -5000})
	@DisplayName("validatePrice() - 쿠폰금액 검증 - 실패")
	void validatePrice_InvalidPrice_ExceptionThrown(int invalidPrice) {
		assertThrows(IllegalArgumentException.class,
			() -> createCoupon(validId, validName, invalidPrice, validExpireDate.plusSeconds(1L)));
	}

	@Test
	@DisplayName("validateUserId() - 쿠폰 생성 - 성공")
	void validateUserId_validUserId_ExceptionDoesNotThrown() {
		assertDoesNotThrow(() -> createCoupon(validId, validName, validPrice, validExpireDate.plusSeconds(1L)));
	}

	@Test
	@DisplayName("validateUserId() - 쿠폰 생성 - 실패")
	void validateUserId_InvalidUserId_ExceptionThrown() {
		Long invalidUserId = null;

		assertThrows(NullPointerException.class,
			() -> createCoupon(invalidUserId, validName, validPrice, validExpireDate.plusSeconds(1L)));
	}

	@ParameterizedTest
	@ValueSource(strings = {"고정 바우처", "정률 바우처", "그냥 바우처"})
	@DisplayName("validateName() - 쿠폰 생성 - 성공")
	void validateName_validName_ExceptionDoesNotThrown(String name) {
		assertDoesNotThrow(() -> createCoupon(validId, name, validPrice, validExpireDate.plusSeconds(1L)));
	}

	@ParameterizedTest
	@ValueSource(strings = {"", " ", "바우처바우처바우처바우처바우처"})
	@DisplayName("validateName() - 쿠폰 생성 - 실패")
	void validateName_InvalidName_ExceptionThrown(String invalidName) {
		assertThrows(IllegalArgumentException.class,
			() -> createCoupon(validId, invalidName, validPrice, validExpireDate.plusSeconds(1L)));
	}

	@Test
	@DisplayName("validateExpireDate() - 쿠폰 생성 - 성공")
	void validateExpireDate_ValidExpireDate_ExceptionDoesNotThrown() {
		assertDoesNotThrow(() -> createCoupon(validId, validName, validPrice, validExpireDate.plusSeconds(1L)));
	}

	@Test
	@DisplayName("validateExpireDate() - 쿠폰 생성 - 실패")
	void validateExpireDate_InValidExpireDate_ExceptionThrown() {
		assertThrows(IllegalArgumentException.class,
			() -> createCoupon(validId, validName, validPrice, validExpireDate.minusSeconds(1L)));
	}

	@ParameterizedTest
	@NullSource
	@DisplayName("validateExpireDate() - 쿠폰 생성 - 실패")
	void validateExpireDate_InValidExpireDate_ExceptionThrown(LocalDateTime expireDate) {
		assertThrows(NullPointerException.class,
			() -> createCoupon(validId, validName, validPrice, expireDate));
	}
}