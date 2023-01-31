package com.prgrms.bdbks.domain.card.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CardSearchResponses {

	private final List<CardSearchResponse> cards;

}