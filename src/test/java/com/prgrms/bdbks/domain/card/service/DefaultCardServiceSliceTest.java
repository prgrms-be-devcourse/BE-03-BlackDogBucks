package com.prgrms.bdbks.domain.card.service;

import static com.prgrms.bdbks.domain.testutil.CardObjectProvider.*;
import static com.prgrms.bdbks.domain.testutil.UserObjectProvider.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.prgrms.bdbks.domain.card.dto.CardChargeRequest;
import com.prgrms.bdbks.domain.card.dto.CardChargeResponse;
import com.prgrms.bdbks.domain.card.entity.Card;
import com.prgrms.bdbks.domain.card.repository.CardRepository;
import com.prgrms.bdbks.domain.user.entity.User;

@ExtendWith(MockitoExtension.class)
public class DefaultCardServiceSliceTest {

	@Mock
	private CardRepository cardRepository;

	@InjectMocks
	private DefaultCardService defaultCardService;

	@DisplayName("charge - 사용자의 충전카드에 한도 내의 금액을 충전할 수 있다. - 성공")
	@Test
	void charge_validAmount_chargeSuccess() {
		//given
		Long userId = 1L;
		String cardId = UUID.randomUUID().toString();
		int amount = 50000;

		User user = createUser(userId);
		Card card = createCard(user);

		CardChargeRequest cardChargeRequest = new CardChargeRequest(cardId, amount);
		Optional<Card> optionalCard = Optional.of(card);

		when(cardRepository.findById(cardId)).thenReturn(optionalCard);

		//when
		CardChargeResponse response = defaultCardService.charge(userId, cardId, amount);

		//then
		verify(cardRepository).findById(cardId);

		assertThat(response)
			.hasFieldOrPropertyWithValue("cardId", cardChargeRequest.getCardId())
			.hasFieldOrPropertyWithValue("amount", cardChargeRequest.getAmount());
	}
}