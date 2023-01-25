package com.prgrms.bdbks.domain.card.dto;

import java.util.List;

import lombok.Getter;

@Getter
public class CardSearchResponses {
	private final List<CardSearchResponse> cardSearchResponses;

	private CardSearchResponses(List<CardSearchResponse> cardSearchResponses) {
		this.cardSearchResponses = cardSearchResponses;
	}

	public static CardSearchResponses of(List<CardSearchResponse> cardSearchResponses) {
		return new CardSearchResponses(cardSearchResponses);
	}
}