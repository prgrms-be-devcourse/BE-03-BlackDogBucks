package com.prgrms.bdbks.domain.coupon.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CouponSearchResponses {

	private List<CouponSearchResponse> couponSearchResponses;
}