package com.prgrms.bdbks.domain.coupon.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CouponSearchResponses {

	private List<CouponSearchResponse> coupons;

}