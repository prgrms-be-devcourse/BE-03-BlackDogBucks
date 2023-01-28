package com.prgrms.bdbks.domain.coupon.dto;

import java.time.LocalDateTime;

import com.prgrms.bdbks.common.annotation.LocalDateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CouponSearchResponse {

	private Long couponId;

	private Long userId;

	private String name;

	private int price;

	private LocalDateTime expireDate;

	private boolean used;
}