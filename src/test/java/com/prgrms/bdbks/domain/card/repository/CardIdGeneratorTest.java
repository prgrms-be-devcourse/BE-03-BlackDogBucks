package com.prgrms.bdbks.domain.card.repository;

import static org.mockito.Mockito.*;

import java.util.Properties;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class CardIdGeneratorTest {

	private final CardIdGenerator cardIdGenerator = new CardIdGenerator();

	@Test
	public void testNamedGenerator() {

		SharedSessionContractImplementor mockSession = mock(SharedSessionContractImplementor.class);

		Object mockObject = mock(Object.class);

		Type type = mock(Type.class);

		Properties params = new Properties();

		params.put("userId", "userId는여기에 넘겨야 한다.");

		ServiceRegistry serviceRegistry = mock(ServiceRegistry.class);

		cardIdGenerator.configure(type, params, serviceRegistry);

		String generatedId = cardIdGenerator.generate(mockSession, mockObject).toString();

		log.info("id = {}", generatedId);

	}

}