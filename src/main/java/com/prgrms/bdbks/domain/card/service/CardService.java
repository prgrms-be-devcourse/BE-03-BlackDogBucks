package com.prgrms.bdbks.domain.card.service;

import com.prgrms.bdbks.domain.card.dto.CardChargeResponse;
import com.prgrms.bdbks.domain.card.dto.CardSearchResponses;
import com.prgrms.bdbks.domain.card.entity.Card;
import com.prgrms.bdbks.domain.user.entity.User;

public interface CardService {
	CardChargeResponse charge(Long userid, String cardId, int amount);

	CardSearchResponses getCardList(User user);

	Card getCard(String cardId);
}
