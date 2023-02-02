package com.prgrms.bdbks.domain.coupon.service;

import com.prgrms.bdbks.domain.coupon.dto.CouponRefundResponse;
import com.prgrms.bdbks.domain.coupon.dto.CouponSaveResponse;
import com.prgrms.bdbks.domain.coupon.dto.CouponSearchResponses;
import com.prgrms.bdbks.domain.coupon.entity.Coupon;

public interface CouponService {

	CouponSaveResponse create(Long userId);

	void createByStar(Long userId, boolean canExchange);

	CouponSearchResponses findAllByUserId(Long userId);

	Coupon getCouponByCouponId(Long couponId);

	CouponSearchResponses findUnusedCoupon(Long userId, boolean used);

	CouponRefundResponse refundCoupon(Long couponId);

}
