package com.prgrms.bdbks.domain.order.entity;

import static com.prgrms.bdbks.domain.item.entity.BeverageOption.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import com.prgrms.bdbks.domain.item.entity.OptionPrice;

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

	@DisplayName("생성 - calculateAddCosts() - customOption에 따른 추가요금을 정상 계산한다.")
	@ParameterizedTest
	@CsvSource(value = {
		" , 3, 3, 3, 3, 4, 4, 4, 500, 500, 1500", // espressoShotCount is null
		" , 4, 4, 4, 4, 4, 4, 4, 1000, 100, 0", // espressoShotCount is null
		" , 3, 3, 3, 3, 0, 0, 0, 1000, 100, 0", // espressoShotCount is null
		" , 9, 9, 9, 9, 0, 0, 0, 0, 0, 0", // espressoShotCount is null

		"0, , 0, 0, 0, 0, 8, 8, 10000, 10000, 160000", // vanillaSyrupCount is null
		"5, , 5, 5, 5, 5, 4, 4, 10000, 10000, 0", // vanillaSyrupCount is null
		"0, , 2, 3, 5, 1, 4, 4, 600, 600, 4800", // vanillaSyrupCount is null
		"5, , 5, 5, 9, 5, 9, 9, 600, 600, 7200", // vanillaSyrupCount is null
		"5, , 5, 5, 4, 5, 3, 2, 600, 600, 0", // vanillaSyrupCount is null

		"2, 3, , 5, 2, 2, 2, 2, 200, 200, 0", // classicSyrupCount is null
		"2, 3, , 5, 4, 5, , 2, 400, 400, 1600", // classicSyrupCount is null
		"2, 3, , 5, 3, 8, , 7, 8800, 800, 14400", // classicSyrupCount is null

		"0, 3, 2, , 5, 1, 4, 4, 600, 600, 4200", // hazelnutSyrupCount is null
		"9, 9, 9, , 9, 0, 0, 0, 0, 0, 0", // hazelnutSyrupCount is null
		"0, 0, 0, , 3, 3, 3, 3, 1000, 1000, 9000", // hazelnutSyrupCount is null
		"3, 3, 3, , 2, 2, 2, 2, 600, 600, 0", // hazelnutSyrupCount is null
	}, delimiterString = ",")
	void calculateAddCosts_calculateDefaultNullOption_success(
		Integer defaultEspressoShotCount,
		Integer defaultVanillaSyrupCount,
		Integer defaultClassicSyrupCount,
		Integer defaultHazelnutSyrupCount,
		Integer espressoShotCount, Integer vanillaSyrupCount, Integer classicSyrupCount, Integer hazelnutSyrupCount,

		int espressoPrice, int syrupPrice, Integer expectedCost) {

		//given
		OptionPrice optionPrice = new OptionPrice(espressoPrice, syrupPrice);

		CustomOption customOption = CustomOption.builder()
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

		int totalPrice = customOption.calculateAddCosts(defaultEspressoShotCount, defaultVanillaSyrupCount,
			defaultClassicSyrupCount, defaultHazelnutSyrupCount, optionPrice);

		//then
		assertEquals(expectedCost, totalPrice);
	}

}
