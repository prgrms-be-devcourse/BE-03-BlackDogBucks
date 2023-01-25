package com.prgrms.bdbks.domain.payment.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentOrderRequest {

	@Positive(message = "올바르지 않은 결제 금액입니다.")
	private int totalPrice;

	@NotBlank(message = "올바르지 않는 결제 유저 ID입니다.")
	private Long userId;

	@NotBlank(message = "올바르지 않는 결제 카드 ID입니다.")
	private String cardId;

	@NotNull(message = "쿠폰 사용 여부를 입력해주세요")
	private Boolean couponUsed;

	@Positive(message = "구매 수량은 0보다 커야 합니다.")
	private int count;
}
