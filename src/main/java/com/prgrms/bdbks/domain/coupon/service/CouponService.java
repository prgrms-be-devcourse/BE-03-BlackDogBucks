package com.prgrms.bdbks.domain.coupon.service;

import java.util.Optional;

import com.prgrms.bdbks.domain.coupon.dto.CouponSaveResponse;
import com.prgrms.bdbks.domain.coupon.dto.CouponSearchResponses;
import com.prgrms.bdbks.domain.coupon.entity.Coupon;
import com.prgrms.bdbks.domain.user.entity.User;

public interface CouponService {
	CouponSaveResponse create(Long userId);

	CouponSearchResponses findAllByUserId(Long userId);

	Optional<Coupon> getOptionalCouponByCouponId(Long couponId);

	Coupon getCouponByCouponId(Long couponId);
}
