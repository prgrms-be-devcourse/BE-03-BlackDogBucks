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
class ItemRepositoryTest {

	private final ItemRepository itemRepository;

	@DisplayName("조회 - itemType과 CategoryName으로 item list를 조회할 수 있다.")
	@Test
	void findAllByItemTypeAndCategoryName_success() {
		ItemType beverage = ItemType.BEVERAGE;
		String categoryName = "에스프레소";

		itemRepository.findAllByItemTypeAndCategoryName(beverage, categoryName);
	}

	@DisplayName("조회 - Item과 defaultOption을 함께 조회 할 수 있다.")
	@Test
	void findByIdWithOption_success() {
		Long itemId = 1L;

		itemRepository.findByIdWithOption(itemId);
	}

}