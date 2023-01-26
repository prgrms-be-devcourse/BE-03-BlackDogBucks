package com.prgrms.bdbks.domain.card.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CardSearchResponse {

	private String cardId;

	private String name;

	private int amount;
}