package com.prgrms.bdbks.domain.item.service;

import static com.prgrms.bdbks.domain.testutil.ItemObjectProvider.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.bdbks.domain.item.dto.DefaultOptionCreateRequest;
import com.prgrms.bdbks.domain.item.dto.ItemCreateRequest;
import com.prgrms.bdbks.domain.item.dto.ItemResponse;
import com.prgrms.bdbks.domain.item.entity.DefaultOption;
import com.prgrms.bdbks.domain.item.entity.Item;
import com.prgrms.bdbks.domain.item.entity.ItemCategory;
import com.prgrms.bdbks.domain.item.entity.ItemType;
import com.prgrms.bdbks.domain.item.repository.DefaultOptionRepository;
import com.prgrms.bdbks.domain.item.repository.ItemCategoryRepository;
import com.prgrms.bdbks.domain.item.repository.ItemRepository;

import lombok.RequiredArgsConstructor;

@Transactional
@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
class DefaultItemServiceTest {

	private final DefaultItemService defaultItemService;

	private final ItemRepository itemRepository;

	private final ItemCategoryRepository itemCategoryRepository;

	private final DefaultOptionRepository defaultOptionRepository;

	@DisplayName("생성 - createItem() - Item 생성에 성공한다.")
	@Test
	void createItem_success() {
		//given
		ItemType beverage = ItemType.BEVERAGE;
		String categoryName = "에스프레소";
		String name = "카페 아메리카노";
		String englishName = "Caffe Americano";
		int price = 4500;
		String image = "http://hkbks.com/url";
		String description = "진한 에스프레소와 뜨거운 물을 섞어 스타벅스의 깔끔하고 강렬한 에스프레소를 가장 부드럽게 잘 느낄 수 있는 커피";

		Integer espressoShotCount = 2;
		Integer vanillaSyrupCount = 0;
		Integer classicSyrupCount = 0;
		Integer hazelnutSyrupCount = 0;

		DefaultOptionCreateRequest defaultOptionCreateRequest = new DefaultOptionCreateRequest(espressoShotCount,
			vanillaSyrupCount, classicSyrupCount, hazelnutSyrupCount, null, null, null);

		ItemCreateRequest request = new ItemCreateRequest(
			beverage, categoryName, name, englishName, price, image, description, defaultOptionCreateRequest
		);

		ItemCategory itemCategory = createItemCategory(categoryName, "espresso", beverage);

		itemCategoryRepository.save(itemCategory);

		DefaultOption defaultOption = createDefaultOption(espressoShotCount, vanillaSyrupCount, classicSyrupCount,
			hazelnutSyrupCount, null, null, null);

		defaultOptionRepository.save(defaultOption);

		//when
		Long itemId = defaultItemService.createItem(request, itemCategory, defaultOption);

		//then
		Item findItem = itemRepository.findById(itemId).get();

		assertThat(findItem)
			.hasFieldOrPropertyWithValue("id", itemId)
			.hasFieldOrPropertyWithValue("name", name)
			.hasFieldOrPropertyWithValue("englishName", englishName)
			.hasFieldOrPropertyWithValue("price", price)
			.hasFieldOrPropertyWithValue("image", image)
			.hasFieldOrPropertyWithValue("isBest", false)
			.hasFieldOrPropertyWithValue("isNew", false)
			.hasFieldOrPropertyWithValue("description", description)
			.hasFieldOrPropertyWithValue("category", itemCategory)
			.hasFieldOrPropertyWithValue("defaultOption", defaultOption);
	}

	@DisplayName("생성 - findAllBy - Item 조회에 성공한다.")
	@Test
	void findAllBy_success() {
		//given
		ItemType beverage = ItemType.BEVERAGE;
		String espressoCategoryName = "에스프레소";

		String coldBrewCategoryName = "콜드 브루";

		Integer espressoShotCount = 2;
		Integer vanillaSyrupCount = 0;
		Integer classicSyrupCount = 0;
		Integer hazelnutSyrupCount = 0;

		ItemCategory espresso = createItemCategory(espressoCategoryName, "espresso", beverage);

		itemCategoryRepository.save(espresso);

		ItemCategory coldBrew = createItemCategory(coldBrewCategoryName, "cold brew", beverage);

		itemCategoryRepository.save(coldBrew);

		DefaultOption defaultOption = createDefaultOption(espressoShotCount, vanillaSyrupCount, classicSyrupCount,
			hazelnutSyrupCount, null, null, null);

		defaultOptionRepository.save(defaultOption);

		itemRepository.saveAll(createItems(List.of("스타벅스 돌체 라떼", "카페 아메리카노", "카페 라떼"),
			List.of("Starbucks Dolce Latte", "Caffe Americano", "Caffe Latte"),
			List.of(5900, 4500, 5000),
			List.of("http://hkbks.com/url1", "http://hkbks.com/url2,", "http://hkbks.com/url3"),
			espresso,
			defaultOption
		));

		itemRepository.saveAll(createItems(List.of("콜드 브루", "돌체 콜드 브루", "바닐라 크림 콜드 브루", "콜드 브루 오트 라떼"),
			List.of("Cold Brew", "Dolce Cold Brew", "Vanilla Cream Cold brew", "Cold Brew with Oat Milk"),
			List.of(4900, 6000, 5800, 5800),
			List.of("http://hkbks.com/url1", "http://hkbks.com/url2,", "http://hkbks.com/url3",
				"http://hkbks.com/url4"),
			coldBrew,
			defaultOption
		));

		//when
		List<ItemResponse> itemResponses = defaultItemService.findAllBy(beverage, espressoCategoryName);

		//then
		assertThat(itemResponses).hasSize(3);

		assertThat(itemResponses)
			.extracting(ItemResponse::getType)
			.contains(beverage);

		assertThat(itemResponses)
			.extracting(ItemResponse::getCategoryName)
			.contains(espressoCategoryName);
	}

}