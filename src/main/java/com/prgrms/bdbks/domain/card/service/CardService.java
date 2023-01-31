package com.prgrms.bdbks.domain.card.service;

import com.prgrms.bdbks.domain.card.dto.CardChargeResponse;
import com.prgrms.bdbks.domain.card.dto.CardPayResponse;
import com.prgrms.bdbks.domain.card.dto.CardSaveRequest;
import com.prgrms.bdbks.domain.card.dto.CardSaveResponse;
import com.prgrms.bdbks.domain.card.dto.CardSearchResponse;
import com.prgrms.bdbks.domain.card.dto.CardSearchResponses;

public interface CardService {

	CardChargeResponse charge(Long userid, String cardId, int amount);

	CardSearchResponses findAll(Long userId);

	CardSearchResponse findByCardId(String cardId);

	CardPayResponse pay(Long userId, String cardId, int totalPrice);

	CardSaveResponse create(Long userId, CardSaveRequest cardSaveRequest);

}