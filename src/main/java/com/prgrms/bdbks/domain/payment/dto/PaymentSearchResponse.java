package com.prgrms.bdbks.domain.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaymentSearchResponse {

	private final String id;

	private final String chargeCardId;

	private final int price;

}
