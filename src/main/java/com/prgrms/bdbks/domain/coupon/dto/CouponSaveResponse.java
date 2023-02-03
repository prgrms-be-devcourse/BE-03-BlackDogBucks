package com.prgrms.bdbks.domain.coupon.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CouponSaveResponse {

	private Long id;

	private Long userId;

	private String name;

	private int price;

	private LocalDateTime expireDate;

}