package com.prgrms.bdbks.domain.card.service;

import static com.prgrms.bdbks.domain.testutil.CardObjectProvider.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.TestConstructor;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.bdbks.common.exception.EntityNotFoundException;
import com.prgrms.bdbks.domain.card.dto.CardChargeResponse;
import com.prgrms.bdbks.domain.card.dto.CardRefundResponse;
import com.prgrms.bdbks.domain.card.dto.CardSaveRequest;
import com.prgrms.bdbks.domain.card.dto.CardSaveResponse;
import com.prgrms.bdbks.domain.card.dto.CardSearchResponse;
import com.prgrms.bdbks.domain.card.dto.CardSearchResponses;
import com.prgrms.bdbks.domain.card.entity.Card;
import com.prgrms.bdbks.domain.card.repository.CardRepository;
import com.prgrms.bdbks.domain.testutil.UserObjectProvider;
import com.prgrms.bdbks.domain.user.entity.User;
import com.prgrms.bdbks.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Transactional
@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
class DefaultCardServiceIntegrationTest {

	private final UserRepository userRepository;

	private final CardRepository cardRepository;

	private final DefaultCardService defaultCardService;

	private final User user = UserObjectProvider.createUser();

	private Card card;

	@BeforeEach
	void setUp() {
		userRepository.save(user);
		card = cardRepository.save(createCard(user));
	}

	@DisplayName("getCardList - ???????????? ???????????? ???????????? ????????? ????????? ??? ??????. - ??????")
	@Test
	void getCardList_validUser_ReturnCards() {
		//when
		CardSearchResponses responses = defaultCardService.findAll(user.getId());

		List<CardSearchResponse> cardResponseList = responses.getCards();

		//then
		assertThat(cardResponseList.size()).isEqualTo(1);
	}

	@DisplayName("charge - ???????????? ??????????????? ?????? ?????? ????????? ????????? ??? ??????. - ??????")
	@ParameterizedTest
	@ValueSource(ints = {10000, 20000, 50000, 300000, 550000})
	void charge_validAmount_chargeSuccess(int amount) {

		//when
		CardChargeResponse chargeResponse = defaultCardService.charge(user.getId(), card.getId(), amount);

		//then
		assertThat(chargeResponse)
			.hasFieldOrPropertyWithValue("chargeCardId", card.getId())
			.hasFieldOrPropertyWithValue("amount", card.getAmount());
	}

	@DisplayName("charge - ???????????? ??????????????? ????????? ???????????? ????????? ????????? ??? ??????. - ??????")
	@ParameterizedTest
	@ValueSource(ints = {-50000, -2500, 1000, 9999, 550001, 10000000})
	void charge_invalidAmount_exceptionThrown(int amount) {

		//when
		assertThrows(IllegalArgumentException.class,
			() -> defaultCardService.charge(user.getId(), card.getId(), amount));
	}

	@DisplayName("getCard - ????????? ?????? id??? ????????? ??? ??????. - ??????")
	@Test
	void getCard_validId_FindSuccess() {
		assertDoesNotThrow(() -> defaultCardService.findByCardId(card.getId()));
		CardSearchResponse cardSearchResponse = defaultCardService.findByCardId(card.getId());

		assertThat(cardSearchResponse)
			.hasFieldOrPropertyWithValue("chargeCardId", card.getId())
			.hasFieldOrPropertyWithValue("name", card.getName())
			.hasFieldOrPropertyWithValue("amount", card.getAmount());
	}

	@DisplayName("getCard - ???????????? ?????? ????????? ????????? ??? ??????. - ??????")
	@ParameterizedTest
	@ValueSource(strings = {"unknownId"})
	void getCard_invalidId_exceptionThrown(String cardId) {
		//when
		assertThrows(EntityNotFoundException.class, () -> defaultCardService.findByCardId(cardId));
	}

	@DisplayName("getCard - ??????id??? null??? ?????? ?????? ????????? ????????????. - ??????")
	@ParameterizedTest
	@NullSource
	void getCard_NullSource_exceptionThrown(String cardId) {
		//when
		assertThrows(DataAccessException.class, () -> defaultCardService.findByCardId(cardId));
	}

	@DisplayName("create - ???????????? id??? ?????? ????????? ???????????? ????????? ????????????. - ??????")
	@Test
	void create_validParameter_createSuccess() {
		//given
		String name = "????????????";
		CardSaveRequest cardSaveRequest = new CardSaveRequest(name);
		//when
		CardSaveResponse cardSaveResponse = defaultCardService.create(user.getId(), cardSaveRequest);

		//then
		Optional<Card> optionalCard = cardRepository.findById(cardSaveResponse.getChargeCardId());
		assertThat(optionalCard).isPresent();
		Card card = optionalCard.get();
		assertThat(card.getName()).isEqualTo(name);
	}

	@DisplayName("create - ??????????????? ????????? ????????? ?????? ???????????? ????????? ????????????. - ??????")
	@ParameterizedTest
	@NullAndEmptySource
	void create_InvalidParameters_Fail(String name) {
		//given
		CardSaveRequest cardSaveRequest = new CardSaveRequest(name);

		//when & then
		assertThrows(IllegalArgumentException.class, () -> defaultCardService.create(user.getId(), cardSaveRequest));
	}

	@DisplayName("create - ????????? ????????? ???????????? ?????? ?????? ???????????? ????????? ????????????. - ??????")
	@Test
	void create_ValidParameters_Fail() {
		//given
		String name = "?????? ??????";
		Long unknownUserId = 100L;
		CardSaveRequest cardSaveRequest = new CardSaveRequest(name);

		//when & then
		assertThrows(EntityNotFoundException.class, () -> defaultCardService.create(unknownUserId, cardSaveRequest));
	}

	@DisplayName("refund - 0??? ????????? ?????? ?????? ????????? ????????????. - ??????")
	@ParameterizedTest
	@ValueSource(ints = {10000, 20000, 50000, 300000, 650000})
	void refund_validCardId_Success(int amount) {
		CardRefundResponse cardRefundResponse = defaultCardService.refund(card.getId(), amount);

		assertThat(cardRefundResponse)
			.hasFieldOrPropertyWithValue("chargeCardId", card.getId())
			.hasFieldOrPropertyWithValue("refundPrice", amount)
			.hasFieldOrPropertyWithValue("rest", card.getAmount());

	}

	@DisplayName("refund - 0??? ????????? ?????? ?????? ????????? ????????????. - ??????")
	@ParameterizedTest
	@ValueSource(ints = {-10000, -20000, -50000, -300000, -650000})
	void refund_inValidCardId_Success(int amount) {
		assertThrows(IllegalArgumentException.class, () -> defaultCardService.refund(card.getId(), amount));
	}
}
