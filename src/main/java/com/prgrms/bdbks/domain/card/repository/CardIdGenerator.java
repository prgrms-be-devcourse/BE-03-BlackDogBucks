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

			int randomNumber = randomNumberGenerator.getRandom();

			long userIdEncryption = userId % 9000 + 1000;

			return userIdEncryption + "-" + randomNumber;
		}

		return new IllegalArgumentException("올바른 Id Generator가 아닙니다.");
	}

}
