package com.prgrms.bdbks.domain.star.service;

import org.springframework.stereotype.Service;

import com.prgrms.bdbks.domain.coupon.dto.CouponSaveResponses;
import com.prgrms.bdbks.domain.coupon.service.CouponService;
import com.prgrms.bdbks.domain.star.dto.StarExchangeResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StarFacadeService {

	private final StarService starService;

	private final CouponService couponService;

	private CouponSaveResponses exchangeCoupon(Long userId) {
		StarExchangeResponse starExchangeResponse = starService.exchangeCoupon(userId);

		return couponService.createByStar(starExchangeResponse.getUserId(),
			starExchangeResponse.getCount());
	}
}
