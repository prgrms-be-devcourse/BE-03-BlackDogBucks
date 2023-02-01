package com.prgrms.bdbks.domain.order.dto;

import javax.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderRejectRequest {

	@NotNull(message = "유저 id는 필수 값 입니다.")
	private Long userId;

}
