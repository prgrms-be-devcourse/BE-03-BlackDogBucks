package com.prgrms.bdbks.domain.testutil;

import com.prgrms.bdbks.domain.card.dto.CardChargeRequest;
import com.prgrms.bdbks.domain.card.entity.Card;
import com.prgrms.bdbks.domain.user.entity.User;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CardObjectProvider {

	public static Card createCard(User user) {
		return Card.builder()
			.user(user)
			.name("카드카드카드")
			.build();
	}

	public static Card createCard(User user, String id) {
		return Card.builder()
			.id(id)
			.user(user)
			.name("카드카드카드")
			.build();
	}

	public static CardChargeRequest createCardRequest(String id, int amount) {
		return new CardChargeRequest(id, amount);
	}
}
