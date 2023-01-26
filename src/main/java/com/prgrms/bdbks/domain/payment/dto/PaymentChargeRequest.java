package com.prgrms.bdbks.domain.payment.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentChargeRequest {

	@Min(value = 10000, message = "최소 충전 금액은 10000원 입니다.")
	@Max(value = 550000, message = "최대 충전 금액은 550000원 입니다.")
	private int amount;

	@NotBlank(message = "올바르지 않는 충전 카드 ID입니다.")
	private String cardId;
}