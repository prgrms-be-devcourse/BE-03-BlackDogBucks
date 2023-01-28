package com.prgrms.bdbks.domain.card.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CardSearchResponse {

	private String chargeCardId;

	private String name;

	private int amount;
}