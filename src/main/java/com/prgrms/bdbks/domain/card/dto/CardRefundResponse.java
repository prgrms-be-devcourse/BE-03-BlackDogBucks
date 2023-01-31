package com.prgrms.bdbks.domain.card.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CardRefundResponse {

	private final String chargeCardId;

	private final int refundPrice;

	private final int rest;
}
