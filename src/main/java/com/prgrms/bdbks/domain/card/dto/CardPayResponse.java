package com.prgrms.bdbks.domain.card.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CardPayResponse {

	private final String chargeCardId;

	private final int amount;

}