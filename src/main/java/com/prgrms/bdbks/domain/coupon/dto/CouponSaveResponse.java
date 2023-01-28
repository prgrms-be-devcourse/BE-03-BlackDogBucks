package com.prgrms.bdbks.domain.coupon.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CouponSaveResponse {

	private Long userId;

	private String name;

	private int price;

	private LocalDateTime expireDate;
}