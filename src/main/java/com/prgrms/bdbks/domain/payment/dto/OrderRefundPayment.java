package com.prgrms.bdbks.domain.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderRefundPayment {

	private final String orderId;

	private Long couponId;

}
