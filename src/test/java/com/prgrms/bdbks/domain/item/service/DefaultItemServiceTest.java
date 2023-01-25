package com.prgrms.bdbks.domain.item.service;

import static com.prgrms.bdbks.domain.testutil.ItemObjectProvider.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.bdbks.common.exception.EntityNotFoundException;
import com.prgrms.bdbks.domain.item.dto.DefaultOptionCreateRequest;
import com.prgrms.bdbks.domain.item.dto.ItemCreateRequest;
import com.prgrms.bdbks.domain.item.dto.ItemDetailResponse;
import com.prgrms.bdbks.domain.item.dto.ItemResponse;
import com.prgrms.bdbks.domain.item.entity.BeverageOption;
import com.prgrms.bdbks.domain.item.entity.DefaultOption;
import com.prgrms.bdbks.domain.item.entity.Item;
import com.prgrms.bdbks.domain.item.entity.ItemCategory;
import com.prgrms.bdbks.domain.item.entity.ItemType;
import com.prgrms.bdbks.domain.item.repository.DefaultOptionRepository;
import com.prgrms.bdbks.domain.item.repository.ItemCategoryRepository;
import com.prgrms.bdbks.domain.item.repository.ItemRepository;
import com.prgrms.bdbks.domain.order.dto.OptionResponse;

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

	private final ObjectMapper objectMapper;

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

	@DisplayName("조회 - findItemDetailBy - Item을 상세조회하고 옵션목록을 보여준다. - 성공")
	@Test
	void findItemDetailBy_success() throws JsonProcessingException {
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
		Integer classicSyrupCount = null;
		Integer hazelnutSyrupCount = 0;

		DefaultOptionCreateRequest defaultOptionCreateRequest = new DefaultOptionCreateRequest(espressoShotCount,
			vanillaSyrupCount, classicSyrupCount, hazelnutSyrupCount, null, null, null);

		ItemCreateRequest request = new ItemCreateRequest(
			beverage, categoryName, name, englishName, price, image, description, defaultOptionCreateRequest
		);

		ItemCategory itemCategory = createItemCategory(categoryName, "espresso", beverage);

		itemCategoryRepository.save(itemCategory);

		DefaultOption defaultOption = createDefaultOption(espressoShotCount, vanillaSyrupCount, classicSyrupCount,
			hazelnutSyrupCount, null, BeverageOption.Coffee.ESPRESSO, null);

		defaultOptionRepository.save(defaultOption);

		Long americanoId = defaultItemService.createItem(request, itemCategory, defaultOption);

		//when
		ItemDetailResponse itemDetailResponse = defaultItemService.findItemDetailBy(americanoId);

		//then
		OptionResponse optionResponse = itemDetailResponse.getOption();
		assertNull(optionResponse.getMilk());

		assertThat(optionResponse.getCoffee())
			.hasFieldOrPropertyWithValue("defaultType", BeverageOption.Coffee.ESPRESSO.getKorName());

		// null 이 아닌 syrup 옵션 포함 여부
		assertThat(optionResponse.getSyrup())
			.extracting(OptionResponse.SyrupsOption::getSyrupName)
			.contains(BeverageOption.Syrup.VANILLA.getKorName(), BeverageOption.Syrup.HAZELNUT.getKorName())
			.doesNotContain(BeverageOption.Syrup.CLASSIC.getKorName());

		// 시럽 옵션 샷 count 같은지 여부
		assertThat(optionResponse.getSyrup())
			.extracting(OptionResponse.SyrupsOption::getDefaultCount)
			.contains(0);

		// 사이즈 옵션 이름 포함 여부
		assertThat(optionResponse.getSize())
			.extracting(OptionResponse.SizeOption::getName)
			.contains(Arrays.stream(BeverageOption.Size.values())
				.map(BeverageOption.Size::getEnglishName)
				.toArray(String[]::new));

		// 사이즈 옵션 양 포함 여부
		assertThat(optionResponse.getSize())
			.extracting(OptionResponse.SizeOption::getAmount)
			.contains(Arrays.stream(BeverageOption.Size.values())
				.map(BeverageOption.Size::getAmount)
				.toArray(String[]::new));

		// 컵 타입 포함 여부
		assertThat(optionResponse.getCupType())
			.isEqualTo(Arrays.stream(BeverageOption.CupType.values())
				.map(BeverageOption.CupType::getKoreaName)
				.collect(Collectors.toList()));

		assertThat(optionResponse.getSyrup()).hasSize(2);

		assertThat(itemDetailResponse.getItemId()).isEqualTo(americanoId);
		assertThat(itemDetailResponse.getImage()).isEqualTo(image);
		assertThat(itemDetailResponse.getName()).isEqualTo(name);
		assertThat(itemDetailResponse.getEnglishName()).isEqualTo(englishName);
		assertThat(itemDetailResponse.getPrice()).isEqualTo(price);
		assertThat(itemDetailResponse.getIsBest()).isFalse();
		assertThat(itemDetailResponse.getIsNew()).isFalse();

		System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(itemDetailResponse));

	}

	@DisplayName("조회 - findItemDetailBy - 존재하지 않는 Item을 조회하면 예외를 던진다. - 실패")
	@Test
	void findItemDetailBy_fail() {
		// given
		Long id = 0L;

		// when
		assertThrows(EntityNotFoundException.class, () -> defaultItemService.findItemDetailBy(id));
	}

}