package com.prgrms.bdbks.domain.item.service;

import static com.prgrms.bdbks.domain.testutil.ItemObjectProvider.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.prgrms.bdbks.common.exception.EntityNotFoundException;
import com.prgrms.bdbks.domain.item.dto.DefaultOptionCreateRequest;
import com.prgrms.bdbks.domain.item.dto.ItemCreateRequest;
import com.prgrms.bdbks.domain.item.entity.DefaultOption;
import com.prgrms.bdbks.domain.item.entity.ItemCategory;
import com.prgrms.bdbks.domain.item.entity.ItemType;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ItemFacadeServiceTest {

	@InjectMocks
	private ItemFacadeService itemFacadeService;

	@Mock
	private ItemService itemService;

	@Mock
	private ItemCategoryService itemCategoryService;

	@Mock
	private ItemOptionService itemOptionService;

	@BeforeAll
	void setUp() {
		itemService = mock(ItemService.class);
		itemCategoryService = mock(ItemCategoryService.class);
		itemOptionService = mock(ItemOptionService.class);
		itemFacadeService = new ItemFacadeService(itemCategoryService, itemService, itemOptionService);
	}

	@DisplayName("생성 - createItem()  item 생성에 성공한다.")
	@Test
	void create_item_success() {
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

		DefaultOption defaultOption = createDefaultOption(espressoShotCount, vanillaSyrupCount, classicSyrupCount,
			hazelnutSyrupCount, null, null, null);

		given(itemCategoryService.findByTypeAndName(request.getItemType(), request.getCategoryName()))
			.willReturn(Optional.of(itemCategory));

		given(itemOptionService.create(request.getDefaultOptionRequest()))
			.willReturn(defaultOption);

		given(itemService.createItem(request, itemCategory, defaultOption))
			.willReturn(1L);
		//when
		Long createItemId = itemFacadeService.createItem(request);
		//then
		assertEquals(1L, createItemId);
		verify(itemCategoryService, atLeastOnce()).findByTypeAndName(request.getItemType(), request.getCategoryName());
		verify(itemOptionService).create(request.getDefaultOptionRequest());
		verify(itemService).createItem(request, itemCategory, defaultOption);
	}

	@DisplayName("생성 - createItem() - category 가 존재하지 않는다면 item 생성에 실패한다.")
	@Test
	void create_item_fail() {
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

		given(itemCategoryService.findByTypeAndName(request.getItemType(), request.getCategoryName()))
			.willReturn(Optional.empty());

		//when
		assertThrows(EntityNotFoundException.class, () -> itemFacadeService.createItem(request));

		//then
		verify(itemCategoryService, atLeastOnce()).findByTypeAndName(request.getItemType(), request.getCategoryName());
	}

}