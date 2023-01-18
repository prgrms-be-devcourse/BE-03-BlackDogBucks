package com.prgrms.bdbks.domain.order.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Order 테스트")
class OrderTest {

	private final Long couponId = 2347634L;
	private final Long userId = 439854L;
	private final String storeId = "20304";
	private final int totalPrice = 32500;

	@DisplayName("생성 - Order() - 생성에 성공한다.")
	@Test
	void builder_create_success() {
		//given
		//when & then
		assertDoesNotThrow(() -> {
			Order.builder().coupon(couponId)
				.userId(userId)
				.storeId(storeId)
				.totalPrice(totalPrice)
				.build();
		});
	}

	@DisplayName("생성 - order() - userId가 null 이면 생성에 실패한다.")
	@Test
	void builder_create_userId_null_fail() {
		//given
		Long nullUserId = null;

		//when & then
		assertThrows(NullPointerException.class, () -> {
			Order.builder()
				.userId(nullUserId)
				.coupon(couponId)
				.storeId(storeId)
				.totalPrice(totalPrice)
				.build();
		});
	}

	@DisplayName("생성 - order() - storeId가 null 이면 생성에 실패한다.")
	@Test
	void builder_create_storId_null_fail() {
		//given
		String nullStoreId = null;

		//when & then
		assertThrows(NullPointerException.class, () -> {
			Order.builder()
				.userId(userId)
				.coupon(couponId)
				.storeId(nullStoreId)
				.totalPrice(totalPrice)
				.build();
		});
	}

	@DisplayName("생성 - order() - totalPrice 가 0 보다 작으면 생성에 실패한다.")
	@Test
	void builder_create_totalPrice_less_then0_fail() {
		//given
		int lessThan0TotalPrice = -1;

		//when & then
		assertThrows(IllegalArgumentException.class, () -> {
			Order.builder()
				.userId(userId)
				.coupon(couponId)
				.storeId(storeId)
				.totalPrice(lessThan0TotalPrice)
				.build();
		});
	}

}