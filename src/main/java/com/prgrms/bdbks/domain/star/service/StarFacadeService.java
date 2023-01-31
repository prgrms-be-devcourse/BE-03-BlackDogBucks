package com.prgrms.bdbks.domain.star.service;

import org.springframework.stereotype.Service;

import com.prgrms.bdbks.domain.coupon.service.CouponService;
import com.prgrms.bdbks.domain.star.dto.StarExchangeResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StarFacadeService {

	private final StarService starService;

	private final CouponService couponService;

	private void exchangeCoupon(Long userId) {
		StarExchangeResponse starExchangeResponse = starService.exchangeCoupon(userId);
		couponService.createByStar(starExchangeResponse.getUserId(), starExchangeResponse.isCanExchange());
	}
}
