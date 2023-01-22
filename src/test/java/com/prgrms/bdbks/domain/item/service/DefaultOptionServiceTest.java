package com.prgrms.bdbks.domain.item.service;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.bdbks.domain.item.dto.DefaultOptionCreateRequest;
import com.prgrms.bdbks.domain.item.entity.BeverageOption;
import com.prgrms.bdbks.domain.item.entity.DefaultOption;

import lombok.RequiredArgsConstructor;

@Transactional
@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
class DefaultOptionServiceTest {

	private final DefaultOptionService defaultOptionService;

	@DisplayName("생성 - DefaultOption 을 생성한다.")
	@Test
	void create_success() {
		//given
		DefaultOptionCreateRequest request = new DefaultOptionCreateRequest(0,
			null, 0, null,
			BeverageOption.Milk.NORMAL, BeverageOption.Coffee.ESPRESSO, BeverageOption.MilkAmount.MEDIUM);
		//when
		DefaultOption defaultOption = defaultOptionService.create(request);

		//then
		assertThat(defaultOption)
			.hasFieldOrPropertyWithValue("espressoShotCount", 0)
			.hasFieldOrPropertyWithValue("vanillaSyrupCount", null)
			.hasFieldOrPropertyWithValue("classicSyrupCount", 0)
			.hasFieldOrPropertyWithValue("hazelnutSyrupCount", null)
			.hasFieldOrPropertyWithValue("milkType", BeverageOption.Milk.NORMAL)
			.hasFieldOrPropertyWithValue("espressoType", BeverageOption.Coffee.ESPRESSO)
			.hasFieldOrPropertyWithValue("milkAmount", BeverageOption.MilkAmount.MEDIUM);
	}

	@DisplayName("생성 - 모든 count 가 null 이여도 DefaultOption 을 생성한다.")
	@Test
	void create_all_count_null_success() {
		//given
		DefaultOptionCreateRequest request = new DefaultOptionCreateRequest(null,
			null, null, null,
			BeverageOption.Milk.NORMAL, BeverageOption.Coffee.ESPRESSO, BeverageOption.MilkAmount.MEDIUM);
		//when
		DefaultOption defaultOption = defaultOptionService.create(request);

		//then
		assertThat(defaultOption)
			.hasFieldOrPropertyWithValue("espressoShotCount", null)
			.hasFieldOrPropertyWithValue("vanillaSyrupCount", null)
			.hasFieldOrPropertyWithValue("classicSyrupCount", null)
			.hasFieldOrPropertyWithValue("hazelnutSyrupCount", null)
			.hasFieldOrPropertyWithValue("milkType", BeverageOption.Milk.NORMAL)
			.hasFieldOrPropertyWithValue("espressoType", BeverageOption.Coffee.ESPRESSO)
			.hasFieldOrPropertyWithValue("milkAmount", BeverageOption.MilkAmount.MEDIUM);
	}

}