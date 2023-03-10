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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestConstructor;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.prgrms.bdbks.common.exception.EntityNotFoundException;
import com.prgrms.bdbks.domain.item.dto.DefaultOptionCreateRequest;
import com.prgrms.bdbks.domain.item.dto.ItemCreateRequest;
import com.prgrms.bdbks.domain.item.dto.ItemDetailResponse;
import com.prgrms.bdbks.domain.item.dto.ItemResponse;
import com.prgrms.bdbks.domain.item.dto.ItemResponses;
import com.prgrms.bdbks.domain.item.dto.OptionResponse;
import com.prgrms.bdbks.domain.item.entity.BeverageOption;
import com.prgrms.bdbks.domain.item.entity.DefaultOption;
import com.prgrms.bdbks.domain.item.entity.Item;
import com.prgrms.bdbks.domain.item.entity.ItemCategory;
import com.prgrms.bdbks.domain.item.entity.ItemType;
import com.prgrms.bdbks.domain.item.repository.DefaultOptionRepository;
import com.prgrms.bdbks.domain.item.repository.ItemCategoryRepository;
import com.prgrms.bdbks.domain.item.repository.ItemRepository;
import com.prgrms.bdbks.domain.store.service.StoreService;

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

	@MockBean
	private StoreService storeService;

	@DisplayName("?????? - createItem() - Item ????????? ????????????.")
	@Test
	void createItem_success() {
		//given
		ItemType beverage = ItemType.BEVERAGE;
		String categoryName = "???????????????";
		String name = "?????? ???????????????";
		String englishName = "Caffe Americano";
		int price = 4500;
		String image = "http://hkbks.com/url";
		String description = "?????? ?????????????????? ????????? ?????? ?????? ??????????????? ???????????? ????????? ?????????????????? ?????? ???????????? ??? ?????? ??? ?????? ??????";

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

		//when
		Long itemId = defaultItemService.createItem(request);

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
			.hasFieldOrPropertyWithValue("category", itemCategory);

		assertThat(findItem.getDefaultOption())
			.hasFieldOrPropertyWithValue("espressoShotCount", espressoShotCount)
			.hasFieldOrPropertyWithValue("vanillaSyrupCount", vanillaSyrupCount)
			.hasFieldOrPropertyWithValue("classicSyrupCount", classicSyrupCount)
			.hasFieldOrPropertyWithValue("hazelnutSyrupCount", hazelnutSyrupCount)
			.hasFieldOrPropertyWithValue("milkType", null)
			.hasFieldOrPropertyWithValue("espressoType", null)
			.hasFieldOrPropertyWithValue("milkAmount", null);

	}

	@DisplayName("?????? - findAllBy - Item ????????? ????????????.")
	@Test
	void findAllBy_success() {
		//given
		ItemType beverage = ItemType.BEVERAGE;
		String espressoCategoryName = "???????????????";

		String coldBrewCategoryName = "?????? ??????";

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

		itemRepository.saveAll(createItems(List.of("???????????? ?????? ??????", "?????? ???????????????", "?????? ??????"),
			List.of("Starbucks Dolce Latte", "Caffe Americano", "Caffe Latte"),
			List.of(5900, 4500, 5000),
			List.of("http://hkbks.com/url1", "http://hkbks.com/url2,", "http://hkbks.com/url3"),
			espresso,
			defaultOption
		));

		itemRepository.saveAll(createItems(List.of("?????? ??????", "?????? ?????? ??????", "????????? ?????? ?????? ??????", "?????? ?????? ?????? ??????"),
			List.of("Cold Brew", "Dolce Cold Brew", "Vanilla Cream Cold brew", "Cold Brew with Oat Milk"),
			List.of(4900, 6000, 5800, 5800),
			List.of("http://hkbks.com/url1", "http://hkbks.com/url2,", "http://hkbks.com/url3",
				"http://hkbks.com/url4"),
			coldBrew,
			defaultOption
		));

		//when
		ItemResponses itemResponses = defaultItemService.findAllBy(beverage, espressoCategoryName);

		//then
		assertThat(itemResponses.getItems()).hasSize(3);

		assertThat(itemResponses.getItems())
			.extracting(ItemResponse::getType)
			.contains(beverage);

		assertThat(itemResponses.getItems())
			.extracting(ItemResponse::getCategoryName)
			.contains(espressoCategoryName);
	}

	@DisplayName("?????? - findItemDetailBy - Item??? ?????????????????? ??????????????? ????????????. - ??????")
	@Test
	void findItemDetailBy_success() throws JsonProcessingException {
		//given

		ItemType beverage = ItemType.BEVERAGE;
		String categoryName = "???????????????";
		String name = "?????? ???????????????";
		String englishName = "Caffe Americano";
		int price = 4500;
		String image = "http://hkbks.com/url";
		String description = "?????? ?????????????????? ????????? ?????? ?????? ??????????????? ???????????? ????????? ?????????????????? ?????? ???????????? ??? ?????? ??? ?????? ??????";

		Integer espressoShotCount = 2;
		Integer vanillaSyrupCount = 0;
		Integer classicSyrupCount = null;
		Integer hazelnutSyrupCount = 0;

		DefaultOptionCreateRequest defaultOptionCreateRequest = new DefaultOptionCreateRequest(espressoShotCount,
			vanillaSyrupCount, classicSyrupCount, hazelnutSyrupCount, null, BeverageOption.Coffee.ESPRESSO, null);

		ItemCreateRequest request = new ItemCreateRequest(
			beverage, categoryName, name, englishName, price, image, description, defaultOptionCreateRequest
		);

		ItemCategory itemCategory = createItemCategory(categoryName, "espresso", beverage);

		itemCategoryRepository.save(itemCategory);

		DefaultOption defaultOption = createDefaultOption(espressoShotCount, vanillaSyrupCount, classicSyrupCount,
			hazelnutSyrupCount, null, BeverageOption.Coffee.ESPRESSO, null);

		defaultOptionRepository.save(defaultOption);

		Long americanoId = defaultItemService.createItem(request);

		//when
		ItemDetailResponse itemDetailResponse = defaultItemService.findItemDetailBy(americanoId);

		//then
		OptionResponse optionResponse = itemDetailResponse.getOption();
		assertNull(optionResponse.getMilk());

		assertThat(optionResponse.getCoffee())
			.hasFieldOrPropertyWithValue("defaultType", BeverageOption.Coffee.ESPRESSO.getKorName());

		// null ??? ?????? syrup ?????? ?????? ??????
		assertThat(optionResponse.getSyrup())
			.extracting(OptionResponse.SyrupsOption::getSyrupName)
			.contains(BeverageOption.Syrup.VANILLA.getKorName(), BeverageOption.Syrup.HAZELNUT.getKorName())
			.doesNotContain(BeverageOption.Syrup.CLASSIC.getKorName());

		// ?????? ?????? ??? count ????????? ??????
		assertThat(optionResponse.getSyrup())
			.extracting(OptionResponse.SyrupsOption::getDefaultCount)
			.contains(0);

		// ????????? ?????? ?????? ?????? ??????
		assertThat(optionResponse.getSize())
			.extracting(OptionResponse.SizeOption::getName)
			.contains(Arrays.stream(BeverageOption.Size.values())
				.map(BeverageOption.Size::getEnglishName)
				.toArray(String[]::new));

		// ????????? ?????? ??? ?????? ??????
		assertThat(optionResponse.getSize())
			.extracting(OptionResponse.SizeOption::getAmount)
			.contains(Arrays.stream(BeverageOption.Size.values())
				.map(BeverageOption.Size::getAmount)
				.toArray(String[]::new));

		// ??? ?????? ?????? ??????
		assertThat(optionResponse.getCupType())
			.isEqualTo(Arrays.stream(BeverageOption.CupType.values())
				.map(BeverageOption.CupType::getKorName)
				.collect(Collectors.toList()));

		assertThat(optionResponse.getSyrup()).hasSize(2);

		assertThat(itemDetailResponse.getItemId()).isEqualTo(americanoId);
		assertThat(itemDetailResponse.getImage()).isEqualTo(image);
		assertThat(itemDetailResponse.getName()).isEqualTo(name);
		assertThat(itemDetailResponse.getEnglishName()).isEqualTo(englishName);
		assertThat(itemDetailResponse.getPrice()).isEqualTo(price);
		assertThat(itemDetailResponse.getIsBest()).isFalse();
		assertThat(itemDetailResponse.getIsNew()).isFalse();
	}

	@DisplayName("?????? - findItemDetailBy - ???????????? ?????? Item??? ???????????? ????????? ?????????. - ??????")
	@Test
	void findItemDetailBy_fail() {
		// given
		Long id = 0L;

		// when
		assertThrows(EntityNotFoundException.class, () -> defaultItemService.findItemDetailBy(id));
	}

}