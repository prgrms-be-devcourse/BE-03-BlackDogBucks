package com.prgrms.bdbks.domain.coupon.entity;

import static com.prgrms.bdbks.domain.testutil.CouponObjectProvider.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import com.prgrms.bdbks.common.exception.CouponAlreadyUsedException;
import com.prgrms.bdbks.common.exception.CouponUnUsedException;

class CouponTest {

	private final Long validId = 1L;
	private final String validName = "쿠폰1";
	private final int validPrice = 10000;
	private final LocalDateTime validExpireDate = LocalDateTime.now();

	@DisplayName("validatePrice() - 쿠폰금액 검증 - 성공")
	@ParameterizedTest
	@ValueSource(ints = {2000, 3000, 5000})
	void validatePrice_ValidPrice_ExceptionDoesNotThrown(int price) {
		assertDoesNotThrow(() -> createCoupon(validId, validName, price, validExpireDate.plusSeconds(1L)));
	}

	@DisplayName("validatePrice() - 쿠폰금액 검증 - 실패")
	@ParameterizedTest
	@ValueSource(ints = {-2000, -3000, -5000})
	void validatePrice_InvalidPrice_ExceptionThrown(int invalidPrice) {
		assertThrows(IllegalArgumentException.class,
			() -> createCoupon(validId, validName, invalidPrice, validExpireDate.plusSeconds(1L)));
	}

	@DisplayName("validateUserId() - 쿠폰 생성 - 성공")
	@Test
	void validateUserId_validUserId_ExceptionDoesNotThrown() {
		assertDoesNotThrow(() -> createCoupon(validId, validName, validPrice, validExpireDate.plusSeconds(1L)));
	}

	@DisplayName("validateUserId() - 쿠폰 생성 - 실패")
	@Test
	void validateUserId_InvalidUserId_ExceptionThrown() {
		Long invalidUserId = null;

		assertThrows(NullPointerException.class,
			() -> createCoupon(invalidUserId, validName, validPrice, validExpireDate.plusSeconds(1L)));
	}

	@DisplayName("validateName() - 쿠폰 생성 - 성공")
	@ParameterizedTest
	@ValueSource(strings = {"고정 바우처", "정률 바우처", "그냥 바우처"})
	void validateName_validName_ExceptionDoesNotThrown(String name) {
		assertDoesNotThrow(() -> createCoupon(validId, name, validPrice, validExpireDate.plusSeconds(1L)));
	}

	@DisplayName("validateName() - 쿠폰 생성 - 실패")
	@ParameterizedTest
	@ValueSource(strings = {"", " ", "바우처바우처바우처바우처바우처"})
	void validateName_InvalidName_ExceptionThrown(String invalidName) {
		assertThrows(IllegalArgumentException.class,
			() -> createCoupon(validId, invalidName, validPrice, validExpireDate.plusSeconds(1L)));
	}

	@DisplayName("validateExpireDate() - 쿠폰 생성 - 성공")
	@Test
	void validateExpireDate_ValidExpireDate_ExceptionDoesNotThrown() {
		assertDoesNotThrow(() -> createCoupon(validId, validName, validPrice, validExpireDate.plusSeconds(1L)));
	}

	@DisplayName("validateExpireDate() - 쿠폰 생성 - 실패")
	@Test
	void validateExpireDate_InValidExpireDate_ExceptionThrown() {
		assertThrows(IllegalArgumentException.class,
			() -> createCoupon(validId, validName, validPrice, validExpireDate.minusSeconds(1L)));
	}

	@DisplayName("validateExpireDate() - 쿠폰 생성 - 실패")
	@ParameterizedTest
	@NullSource
	void validateExpireDate_InValidExpireDate_ExceptionThrown(LocalDateTime expireDate) {
		assertThrows(NullPointerException.class,
			() -> createCoupon(validId, validName, validPrice, expireDate));
	}

	@DisplayName("use() - 쿠폰 사용 처리 - 성공")
	@Test
	void use_Unused_ExceptionDoesNotThrown() {
		Coupon coupon = createCoupon(validId, validName, validPrice, validExpireDate.plusSeconds(1L));
		assertDoesNotThrow(coupon::use);
	}

	@DisplayName("use() - 쿠폰 사용 처리 - 실패")
	@Test
	void use_Used_ExceptionThrown() {
		Coupon coupon = createCoupon(validId, validName, validPrice, validExpireDate.plusSeconds(1L));
		coupon.use();
		assertThrows(CouponAlreadyUsedException.class, coupon::use);
	}

	@DisplayName("refund()- 쿠폰 미사용 처리 - 성공")
	@Test
	void refund_Used_ExceptionDoesNotThrown() {
		Coupon coupon = createCoupon(validId, validName, validPrice, validExpireDate.plusSeconds(1L));
		coupon.use();
		assertDoesNotThrow(coupon::refund);
	}

	@DisplayName("refund() - 쿠폰 미사용 처리 - 실패")
	@Test
	void refund_Unused_ExceptionThrown() {
		Coupon coupon = createCoupon(validId, validName, validPrice, validExpireDate.plusSeconds(1L));
		assertThrows(CouponUnUsedException.class, coupon::refund);
	}
}