package com.prgrms.bdbks.domain.card.repository;

import java.io.Serializable;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import com.prgrms.bdbks.domain.card.entity.Card;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CardIdGenerator implements IdentifierGenerator {

	private final RandomNumberGenerator randomNumberGenerator;

	@Override
	public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {

		if (object instanceof Card) {
			Card card = (Card)object;
			Long userId = card.getUser().getId();

			long userIdEncryption = userId % 900_000_000L + 100_000_000L;

			return String.format("%d012", userIdEncryption) + randomNumberGenerator.randomNumber();
		}

		return new IllegalArgumentException("올바른 카드가 아닙니다.");
	}

}
