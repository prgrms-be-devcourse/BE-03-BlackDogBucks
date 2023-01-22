package com.prgrms.bdbks.domain.item.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("DefaultOption 테스트")
class DefaultOptionTest {

	@DisplayName("생성 - Item() - 모든 shotCount가 0보다 작거나 9보다 크면 생성에 실패한다")
	@ParameterizedTest
	@ValueSource(ints = {-1, -2, 10, 11, 100, 999, -3, -555})
	void constructor_create_count_over0_and_less_then0_fail(int count) {
		// given & when & then
		assertThrows(IllegalArgumentException.class, () -> DefaultOption.builder()
			.classicSyrupCount(count)
			.espressoShotCount(count)
			.vanillaSyrupCount(count)
			.hazelnutSyrupCount(count)
			.build());
	}

	@DisplayName("생성 - Item() - 모든 shotCount가 null이여도 생성에 성공한다. ")
	@Test
	void constructor_create_all_null_success() {
		// given & when & then
		Integer count = null;
		BeverageOption.Milk milk = BeverageOption.Milk.NORMAL;

		assertDoesNotThrow(() -> DefaultOption.builder()
			.classicSyrupCount(count)
			.espressoShotCount(count)
			.vanillaSyrupCount(count)
			.hazelnutSyrupCount(count)
			.milkType(milk)
			.build());
	}
	
}