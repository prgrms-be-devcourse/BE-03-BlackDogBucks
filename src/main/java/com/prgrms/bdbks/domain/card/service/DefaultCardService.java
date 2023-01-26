package com.prgrms.bdbks.domain.card.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.bdbks.common.exception.EntityNotFoundException;
import com.prgrms.bdbks.domain.card.converter.CardMapper;
import com.prgrms.bdbks.domain.card.dto.CardChargeResponse;
import com.prgrms.bdbks.domain.card.dto.CardPayResponse;
import com.prgrms.bdbks.domain.card.dto.CardSearchResponse;
import com.prgrms.bdbks.domain.card.dto.CardSearchResponses;
import com.prgrms.bdbks.domain.card.entity.Card;
import com.prgrms.bdbks.domain.card.repository.CardRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DefaultCardService implements CardService {

	private final CardRepository cardRepository;
	private final CardMapper cardMapper;

	@Override
	@Transactional
	public CardChargeResponse charge(Long userId, String cardId, int amount) {
		//TODO 5만원 이상 충전 시 쿠폰 생성로직 추가(FACADE)
		Card card = cardRepository.findById(cardId)
			.orElseThrow(() -> new EntityNotFoundException(Card.class, cardId));

		card.compareUser(userId);
		card.chargeAmount(amount);

		return new CardChargeResponse(cardId, amount);
	}

	@Override
	public CardSearchResponses findAll(Long userId) {
		List<Card> cards = cardRepository.findByUserId(userId);

		List<CardSearchResponse> responses = cards.stream()
			.map(cardMapper::toCardSearchResponse)
			.collect(Collectors.toList());

		return new CardSearchResponses(responses);
	}

	@Override
	public Card findByCardId(String cardId) {
		return cardRepository.findById(cardId)
			.orElseThrow(() -> new EntityNotFoundException(Card.class, cardId));
	}

	@Override
	@Transactional
	public CardPayResponse pay(Long userId, String cardId, int totalPrice) {
		Card card = findByCardId(cardId);
		card.compareUser(userId);
		card.payAmount(totalPrice);

		return new CardPayResponse(cardId, totalPrice);
	}

}