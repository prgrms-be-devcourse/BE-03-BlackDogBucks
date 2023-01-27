package com.prgrms.bdbks.domain.card.service;

import com.prgrms.bdbks.domain.card.dto.CardChargeResponse;
import com.prgrms.bdbks.domain.card.dto.CardPayResponse;
import com.prgrms.bdbks.domain.card.dto.CardSearchResponses;
import com.prgrms.bdbks.domain.card.entity.Card;

public interface CardService {
	CardChargeResponse charge(Long userid, String cardId, int amount);

	CardSearchResponses findAll(Long userId);

	Card findByCardId(String cardId);

	CardPayResponse pay(Long userId, String cardId, int totalPrice);

}
