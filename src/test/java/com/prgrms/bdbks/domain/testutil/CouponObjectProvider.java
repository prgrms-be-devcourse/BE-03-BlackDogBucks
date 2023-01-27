package com.prgrms.bdbks.domain.testutil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

	public static List<Coupon> createCoupon(Long userId) {

		return IntStream.range(0, 4)
			.mapToObj(i -> Coupon.builder().userId(userId)
				.name("기본 쿠폰")
				.price(6000)
				.expireDate(LocalDateTime.now().plusMonths(6L))
				.build()).collect(Collectors.toList());
	}

}
