package com.prgrms.bdbks.domain.order.entity;

import static com.prgrms.bdbks.domain.item.entity.BeverageOption.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("CustomOption 테스트")
class CustomOptionTest {

	private final Integer espressoShotCount = 2;

	private final Integer vanillaSyrupCount = 1;

	private final Integer classicSyrupCount = 3;

	private final Integer hazelnutSyrupCount = 4;

	private final Milk milkType = Milk.OAT;

	private final Coffee espressoType = Coffee.DECAFFEINATED;

	private final MilkAmount milkAmount = MilkAmount.MEDIUM;

	private final Size cupSize = Size.VENTI;

	private final CupType cupType = CupType.DISPOSABLE;

	@DisplayName("생성 - CustomOption() - 생성에 성공한다.")
	@Test
	void builder_create_success() {
		// given & when & then
		assertDoesNotThrow(() -> {
			CustomOption.builder()
				.espressoType(espressoType)
				.espressoShotCount(espressoShotCount)
				.vanillaSyrupCount(vanillaSyrupCount)
				.hazelnutSyrupCount(hazelnutSyrupCount)
				.classicSyrupCount(classicSyrupCount)
				.milkType(milkType)
				.milkAmount(milkAmount)
				.cupType(cupType)
				.cupSize(cupSize)
				.build();
		});
	}

	@DisplayName("생성 - CustomOption() - cupType가 null 이면 생성에 실패한다.")
	@Test
	void builder_create_cupType_null_fail() {
		//given & when & then
		assertThrows(NullPointerException.class, () -> {
			CustomOption.builder()
				.espressoType(espressoType)
				.espressoShotCount(espressoShotCount)
				.vanillaSyrupCount(vanillaSyrupCount)
				.hazelnutSyrupCount(hazelnutSyrupCount)
				.classicSyrupCount(classicSyrupCount)
				.milkType(milkType)
				.milkAmount(milkAmount)
				.cupType(null)
				.cupSize(cupSize)
				.build();
		});
	}

	@DisplayName("생성 - CustomOption() - cupSize가 null 이면 생성에 실패한다.")
	@Test
	void builder_create_cupSize_null_fail() {
		//given & when & then
		assertThrows(NullPointerException.class, () -> {
			CustomOption.builder()
				.espressoType(espressoType)
				.espressoShotCount(espressoShotCount)
				.vanillaSyrupCount(vanillaSyrupCount)
				.hazelnutSyrupCount(hazelnutSyrupCount)
				.classicSyrupCount(classicSyrupCount)
				.milkType(milkType)
				.milkAmount(milkAmount)
				.cupType(cupType)
				.cupSize(null)
				.build();
		});
	}

	@DisplayName("생성 - CustomOption() - 모든 optin의 개수는 0보다 작거나 9보다 크면 생성에 실패한다")
	@ParameterizedTest
	@ValueSource(ints = {-1, -2, 10, 11, 100, 999, -3, -555})
	void constructor_create_count_over0_and_less_then0_fail(int count) {
		// given & when & then
		assertThrows(IllegalArgumentException.class, () -> CustomOption.builder()
			.classicSyrupCount(count)
			.espressoShotCount(count)
			.vanillaSyrupCount(count)
			.hazelnutSyrupCount(count)
			.cupType(cupType)
			.cupSize(cupSize)
			.build());
	}

}
