package com.prgrms.bdbks.domain.card.service;

import static com.prgrms.bdbks.domain.testutil.CardObjectProvider.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.TestConstructor;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.bdbks.common.exception.EntityNotFoundException;
import com.prgrms.bdbks.domain.card.dto.CardChargeResponse;
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

	@DisplayName("getCardList - 로그인한 사용자의 충전카드 목록을 조회할 수 있다. - 성공")
	@Test
	void getCardList_validUser_ReturnCards() {

		//when
		CardSearchResponses responses = defaultCardService.findAll(user.getId());

		List<CardSearchResponse> cardResponseList = responses.getCardSearchResponses();

		//then
		assertThat(cardResponseList.size()).isEqualTo(1);
	}

	@DisplayName("charge - 사용자의 충전카드에 한도 내의 금액을 충전할 수 있다. - 성공")
	@ParameterizedTest
	@ValueSource(ints = {10000, 20000, 50000, 300000, 550000})
	void charge_validAmount_chargeSuccess(int amount) {

		//when
		CardChargeResponse chargeResponse = defaultCardService.charge(user.getId(), card.getId(), amount);

		//then
		assertThat(chargeResponse)
			.hasFieldOrPropertyWithValue("cardId", card.getId())
			.hasFieldOrPropertyWithValue("amount", card.getAmount());
	}

	@DisplayName("charge - 사용자의 충전카드에 한도를 벗어나는 금액은 충전할 수 없다. - 실패")
	@ParameterizedTest
	@ValueSource(ints = {-50000, -2500, 1000, 9999, 550001, 10000000})
	void charge_invalidAmount_exceptionThrown(int amount) {

		//when
		assertThrows(IllegalArgumentException.class,
			() -> defaultCardService.charge(user.getId(), card.getId(), amount));
	}

	@DisplayName("getCard - 카드를 카드 id로 조회할 수 있다. - 성공")
	@Test
	void getCard_validId_FindSuccess() {
		assertDoesNotThrow(() -> defaultCardService.findByCardId(card.getId()));
		Card savedCard = defaultCardService.findByCardId(this.card.getId());

		Assertions.assertThat(savedCard)
			.hasFieldOrPropertyWithValue("id", card.getId())
			.hasFieldOrPropertyWithValue("name", card.getName())
			.hasFieldOrPropertyWithValue("amount", card.getAmount());
	}

	@DisplayName("getCard - 등록되지 않은 카드는 조회할 수 없다. - 실패")
	@ParameterizedTest
	@ValueSource(strings = {"unknownId"})
	void getCard_invalidId_exceptionThrown(String cardId) {
		//when
		assertThrows(EntityNotFoundException.class, () -> defaultCardService.findByCardId(cardId));
	}

	@DisplayName("getCard - 카드id가 null인 경우 카드 조회에 실패한다. - 실패")
	@ParameterizedTest
	@NullSource
	void getCard_NullSource_exceptionThrown(String cardId) {
		//when
		assertThrows(DataAccessException.class, () -> defaultCardService.findByCardId(cardId));
	}
}