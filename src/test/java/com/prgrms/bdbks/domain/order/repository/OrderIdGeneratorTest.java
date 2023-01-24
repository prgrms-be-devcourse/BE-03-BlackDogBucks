package com.prgrms.bdbks.domain.order.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class OrderIdGeneratorTest {

	private final OrderIdGenerator orderIdGenerator = new OrderIdGenerator();

	@DisplayName("생성 - IdString () - 날짜 + HK + RandomCharacter 형식으로 생성에 성공한다.")
	@Test
	void generate_id_create_success() {
		//given
		LocalDateTime now = LocalDateTime.of(2023, 1, 16, 20, 50, 10, 12345);

		SharedSessionContractImplementor mockSession = mock(SharedSessionContractImplementor.class);

		Object mockObject = mock(Object.class);

		try (MockedStatic<LocalDateTime> mockLocalDateTime = mockStatic(LocalDateTime.class)) {
			given(LocalDateTime.now()).willReturn(now);
			//when
			String generateOrderId = orderIdGenerator.generate(mockSession, mockObject).toString();

			String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));

			//then
			assertTrue(generateOrderId.contains(currentDateTime));
			assertTrue(generateOrderId.contains("HK"));
			log.info(generateOrderId);
			log.info(currentDateTime);
			mockLocalDateTime.verify(LocalDateTime::now, atLeast(2));
			assertEquals(generateOrderId.length(), currentDateTime.length() + 2 + 4);
		}

	}
}