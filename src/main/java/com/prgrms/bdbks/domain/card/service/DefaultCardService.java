package com.prgrms.bdbks.domain.card.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.bdbks.common.exception.EntityNotFoundException;
import com.prgrms.bdbks.domain.card.converter.CardMapper;
import com.prgrms.bdbks.domain.card.dto.CardChargeResponse;
import com.prgrms.bdbks.domain.card.dto.CardSearchResponse;
import com.prgrms.bdbks.domain.card.dto.CardSearchResponses;
import com.prgrms.bdbks.domain.card.entity.Card;
import com.prgrms.bdbks.domain.card.repository.CardRepository;
import com.prgrms.bdbks.domain.user.entity.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DefaultCardService implements CardService {

	private final CardRepository cardRepository;
	private final CardMapper cardMapper;

	@Override
	@Transactional
	public CardChargeResponse charge(Long userid, String cardId, int amount) {
		//TODO 5만원 이상 충전 시 쿠폰 생성로직 추가(FACADE)
		Card card = cardRepository.findById(cardId)
			.orElseThrow(() -> new EntityNotFoundException(Card.class, cardId));

		card.compareUser(userid);
		card.chargeAmount(amount);

		return CardChargeResponse.of(card.getId(), card.getAmount());
	}

	@Override
	public CardSearchResponses getCardList(User user) {
		List<Card> cards = cardRepository.findByUserId(Objects.requireNonNull(user).getId());

		List<CardSearchResponse> responses = cards.stream()
			.map(cardMapper::toCardSearchResponse)
			.collect(Collectors.toList());

		return CardSearchResponses.of(responses);
	}

	@Override
	public Card getCard(String cardId) {
		return cardRepository.findById(cardId)
			.orElseThrow(() -> new EntityNotFoundException(Card.class, cardId));
	}

}