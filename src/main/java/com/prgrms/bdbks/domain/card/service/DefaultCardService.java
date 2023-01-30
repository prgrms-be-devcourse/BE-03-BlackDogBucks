package com.prgrms.bdbks.domain.card.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.bdbks.common.exception.EntityNotFoundException;
import com.prgrms.bdbks.domain.card.converter.CardMapper;
import com.prgrms.bdbks.domain.card.dto.CardChargeResponse;
import com.prgrms.bdbks.domain.card.dto.CardPayResponse;
import com.prgrms.bdbks.domain.card.dto.CardSaveRequest;
import com.prgrms.bdbks.domain.card.dto.CardSaveResponse;
import com.prgrms.bdbks.domain.card.dto.CardSearchResponse;
import com.prgrms.bdbks.domain.card.dto.CardSearchResponses;
import com.prgrms.bdbks.domain.card.entity.Card;
import com.prgrms.bdbks.domain.card.repository.CardRepository;
import com.prgrms.bdbks.domain.user.entity.User;
import com.prgrms.bdbks.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DefaultCardService implements CardService {

	private final CardRepository cardRepository;

	private final UserRepository userRepository;

	private final CardMapper cardMapper;

	@Override
	@Transactional
	public CardSaveResponse create(Long userId, CardSaveRequest cardSaveRequest) {
		User findUser = userRepository.findById(userId)
			.orElseThrow(() -> new EntityNotFoundException(User.class, userId));

		Card card = Card.createCard(findUser, cardSaveRequest.getName());

		cardRepository.save(card);

		return cardMapper.toCardSaveResponse(card);
	}

	@Override
	@Transactional
	public CardChargeResponse charge(Long userId, String cardId, int amount) {
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
	public CardSearchResponse findByCardId(String cardId) {
		Card card = cardRepository.findById(cardId)
			.orElseThrow(() -> new EntityNotFoundException(Card.class, cardId));

		return cardMapper.toCardSearchResponse(card);
	}

	@Override
	@Transactional
	public CardPayResponse pay(Long userId, String cardId, int totalPrice) {
		Card card = cardRepository.findById(cardId)
			.orElseThrow(() -> new EntityNotFoundException(Card.class, cardId));

		card.compareUser(userId);
		card.payAmount(totalPrice);

		return new CardPayResponse(cardId, totalPrice);
	}

}