package com.prgrms.bdbks.domain.testutil;

import java.time.LocalDateTime;

import com.prgrms.bdbks.domain.coupon.entity.Coupon;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CouponObjectProvider {

	public static Coupon createCoupon(Long userId, String name, int price, LocalDateTime expireDate) {
		return Coupon.builder().userId(userId)
			.name(name)
			.price(price)
			.expireDate(expireDate)
			.build();
	}
}
