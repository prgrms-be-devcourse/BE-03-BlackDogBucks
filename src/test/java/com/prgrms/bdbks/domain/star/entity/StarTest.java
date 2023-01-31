package com.prgrms.bdbks.domain.star.entity;

import static com.prgrms.bdbks.domain.testutil.StarObjectProvider.*;
import static com.prgrms.bdbks.domain.testutil.UserObjectProvider.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.prgrms.bdbks.domain.user.entity.User;

class StarTest {

	User user = createUser();

	@ParameterizedTest
	@ValueSource(ints = {0, 1, 5})
	@DisplayName("validateCount() - 별 생성 - 성공")
	void validateCount_validNumber_ExceptionDoesNotThrown(int count) {
		assertDoesNotThrow(() -> createStar(user, count));
	}

	@ParameterizedTest
	@ValueSource(ints = {-1, -2, -5})
	@DisplayName("validateCount() - 별 생성 - 실패")
	void validateCount_InvalidNumber_ExceptionThrown(int count) {
		assertThrows(IllegalArgumentException.class, () -> createStar(user, count));
	}

	@DisplayName("increaseCount() - 별 추가 - 성공")
	@Test
	void increaseCount_InvalidItemCount_ExceptionThrown() {
		short count = 5;
		Star star = createStar(user, count);
		int expectCount = count + 1;
		star.increaseCount();

		assertEquals(expectCount, star.getCount());
	}
	//
	// @ParameterizedTest
	// @ValueSource(shorts = {12, 24, 55, 120, 240})
	// @DisplayName("exchangeCoupon() - 별을 쿠폰으로 교환 - 성공")
	// void exchangeCoupon_CountOver12_Success(short count) {
	// 	Star star = createStar(user, count);
	// 	int exchangeCount = star.exchangeCoupon();
	//
	// 	int expectCount = count / 12;
	// 	int restCount = count % 12;
	//
	// 	assertEquals(expectCount, exchangeCount);
	// 	assertEquals(restCount, star.getCount());
	// }

}