package com.prgrms.bdbks.domain.star.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class StarTest {

	private Star createStar(short count) {
		return Star.builder()
			.count(count)
			.build();
	}

	@ParameterizedTest
	@ValueSource(shorts = {0, 1, 5})
	@DisplayName("validateCount() - 별 생성 - 성공")
	void validateCount_validNumber_ExceptionDoesNotThrown(short count) {
		assertDoesNotThrow(() -> createStar(count));
	}

	@ParameterizedTest
	@ValueSource(shorts = {-1, -2, -5})
	@DisplayName("validateCount() - 별 생성 - 실패")
	void validateCount_InvalidNumber_ExceptionThrown(short count) {
		assertThrows(IllegalArgumentException.class, () -> createStar(count));
	}

}