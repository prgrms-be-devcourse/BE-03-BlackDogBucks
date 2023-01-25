package com.prgrms.bdbks.domain.card.dto;

import lombok.Getter;

@Getter
public class CardChargeResponse {

	private final String cardId;

	private final int amount;

	private CardChargeResponse(String cardId, int amount) {
		this.cardId = cardId;
		this.amount = amount;
	}

	public static CardChargeResponse of(String cardId, int amount) {
		return new CardChargeResponse(cardId, amount);
	}
}