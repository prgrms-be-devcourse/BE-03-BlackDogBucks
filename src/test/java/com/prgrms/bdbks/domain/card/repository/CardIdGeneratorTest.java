package com.prgrms.bdbks.domain.card.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class CardIdGeneratorTest {

	private final CardIdGenerator cardIdGenerator = new CardIdGenerator();

	@Test
	public void testNamedGenerator() {

		int idLength = 23;

		SharedSessionContractImplementor mockSession = mock(SharedSessionContractImplementor.class);

		Object mockObject = mock(Object.class);

		String generatedId = this.cardIdGenerator.generate(mockSession, mockObject).toString();

		log.info("id = {}", generatedId);

		assertEquals(idLength, generatedId.length());
		assertTrue(generatedId.contains("HK"));
	}

}