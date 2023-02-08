package com.prgrms.bdbks.domain.item.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.TestConstructor;

import com.prgrms.bdbks.CustomDataJpaTest;
import com.prgrms.bdbks.domain.item.entity.ItemType;

import lombok.RequiredArgsConstructor;

@CustomDataJpaTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
class ItemCategoryRepositoryTest {

	private final ItemCategoryRepository itemCategoryRepository;

	@DisplayName("조회 - itemType과 name으로 카테고리를 조회할 수 있다.")
	@Test
	void findByItemTypeAndName_query_success() {
		ItemType beverage = ItemType.BEVERAGE;
		String name = "에스프레소";

		itemCategoryRepository.findByItemTypeAndName(beverage, name);
	}

	@DisplayName("조회 - itemType으로 카테고리 List를 조회할 수 있다.")
	@Test
	void findByItemType_success() {
		ItemType itemType = ItemType.BEVERAGE;

		itemCategoryRepository.findByItemTypeOrderById(itemType);
	}

	@DisplayName("조회 - name으로 카테고리를 조회할 수 있다.")
	@Test
	void findByName_success() {

		String name = "에스프레소";

		itemCategoryRepository.findByName(name);
	}
}