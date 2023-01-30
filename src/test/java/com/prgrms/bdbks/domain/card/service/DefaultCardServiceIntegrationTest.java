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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.TestConstructor;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.bdbks.common.exception.EntityNotFoundException;
import com.prgrms.bdbks.domain.card.dto.CardChargeResponse;
import com.prgrms.bdbks.domain.card.dto.CardSaveRequest;
import com.prgrms.bdbks.domain.card.dto.CardSaveResponse;
import com.prgrms.bdbks.domain.card.dto.CardSearchResponse;
import com.prgrms.bdbks.domain.card.dto.CardSearchResponses;
import com.prgrms.bdbks.domain.card.entity.Card;
import com.prgrms.bdbks.domain.card.repository.CardRepository;
import com.prgrms.bdbks.domain.store.service.StoreService;
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

	@MockBean
	private StoreService storeService;
	
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
		CardChargeResponse chargeResponse = defaultCardService.charge(user.getId(), card.getChargeCardId(), amount);

		//then
		assertThat(chargeResponse)
			.hasFieldOrPropertyWithValue("chargeCardId", card.getChargeCardId())
			.hasFieldOrPropertyWithValue("amount", card.getAmount());
	}

	@DisplayName("charge - 사용자의 충전카드에 한도를 벗어나는 금액은 충전할 수 없다. - 실패")
	@ParameterizedTest
	@ValueSource(ints = {-50000, -2500, 1000, 9999, 550001, 10000000})
	void charge_invalidAmount_exceptionThrown(int amount) {

		//when
		assertThrows(IllegalArgumentException.class,
			() -> defaultCardService.charge(user.getId(), card.getChargeCardId(), amount));
	}

	@DisplayName("getCard - 카드를 카드 id로 조회할 수 있다. - 성공")
	@Test
	void getCard_validId_FindSuccess() {
		assertDoesNotThrow(() -> defaultCardService.findByCardId(card.getChargeCardId()));
		CardSearchResponse cardSearchResponse = defaultCardService.findByCardId(this.card.getChargeCardId());

		assertThat(cardSearchResponse)
			.hasFieldOrPropertyWithValue("chargeCardId", card.getChargeCardId())
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

	@DisplayName("create - 사용자의 id와 카드 이름을 입력받아 카드를 생성한다. - 성공")
	@Test
	void create_validParameter_createSuccess() {
		//given
		String name = "카드이름";
		CardSaveRequest cardSaveRequest = new CardSaveRequest(name);
		//when
		CardSaveResponse cardSaveResponse = defaultCardService.create(user.getId(), cardSaveRequest);

		//then
		Optional<Card> optionalCard = cardRepository.findById(cardSaveResponse.getChargeCardId());
		assertThat(optionalCard).isPresent();
		Card card = optionalCard.get();
		assertThat(card.getName()).isEqualTo(name);
	}

	@DisplayName("create - 충전카드의 이름이 누락된 경우 충전카드 등록에 실패한다. - 실패")
	@ParameterizedTest
	@NullAndEmptySource
	void create_InvalidParameters_Fail(String name) {
		//given
		CardSaveRequest cardSaveRequest = new CardSaveRequest(name);

		//when & then
		assertThrows(IllegalArgumentException.class, () -> defaultCardService.create(user.getId(), cardSaveRequest));
	}

	@DisplayName("create - 사용자 정보가 올바르지 않은 경우 충전카드 등록에 실패한다. - 실패")
	@Test
	void create_ValidParameters_Fail() {
		//given
		String name = "기본 카드";
		Long unknownUserId = 100L;
		CardSaveRequest cardSaveRequest = new CardSaveRequest(name);

		//when & then
		assertThrows(EntityNotFoundException.class, () -> defaultCardService.create(unknownUserId, cardSaveRequest));
	}
}