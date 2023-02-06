package com.prgrms.bdbks.domain.card.repository;

import static org.assertj.core.api.Assertions.*;

import java.io.Serializable;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import com.prgrms.bdbks.CustomDataJpaTest;
import com.prgrms.bdbks.domain.card.entity.Card;
import com.prgrms.bdbks.domain.testutil.UserObjectProvider;
import com.prgrms.bdbks.domain.user.entity.User;
import com.prgrms.bdbks.domain.user.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@CustomDataJpaTest
class CardIdGeneratorTest {

	private final TestRandomGenerator random = new TestRandomGenerator();

	private final CardIdGenerator cardIdGenerator = new CardIdGenerator(random);

	@Autowired
	private UserRepository userRepository;

	@DisplayName("Id Generator가 잘 동작하는지 확인한다.")
	@Test
	public void testNamedGenerator() {
		User user = UserObjectProvider.createUser();

		userRepository.save(user);

		Card card = Card.create(user, "기서카드");

		SharedSessionContractImplementor session = Mockito.mock(SharedSessionContractImplementor.class);
		String id = String.format("%d012", hashUserId(user.getId())) + random.randomNumber();

		Serializable result = cardIdGenerator.generate(session, card);

		assertThat(result).isEqualTo(id);
		assertThat(result.toString().length()).isEqualTo(16);
	}

	private Long hashUserId(Long userId) {
		return userId % 900_000_000L + 100_000_000L;
	}

	private static class TestRandomGenerator implements RandomNumberGenerator {

		@Override
		public int randomNumber() {
			return 1234;
		}
	}

}