package com.prgrms.bdbks.domain.item.service;

import static com.prgrms.bdbks.domain.testutil.ItemObjectProvider.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.prgrms.bdbks.common.exception.DuplicateInsertException;
import com.prgrms.bdbks.common.exception.EntityNotFoundException;
import com.prgrms.bdbks.domain.item.dto.ItemCategoryRegisterRequest;
import com.prgrms.bdbks.domain.item.dto.ItemCategoryResponse;
import com.prgrms.bdbks.domain.item.dto.ItemCategoryResponses;
import com.prgrms.bdbks.domain.item.entity.ItemCategory;
import com.prgrms.bdbks.domain.item.entity.ItemType;
import com.prgrms.bdbks.domain.item.repository.ItemCategoryRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@DisplayName("DefaultItemCategoryService 슬라이스 테스트")
class DefaultItemCategoryServiceTest {

	private DefaultItemCategoryService itemCategoryService;

	private ItemCategoryRepository itemCategoryRepository;

	@BeforeEach
	void setUp() {
		itemCategoryRepository = Mockito.mock(ItemCategoryRepository.class);
		itemCategoryService = new DefaultItemCategoryService(itemCategoryRepository);
	}

	@DisplayName("등록 - itemCategory 를 생성한다.")
	@Test
	void register_success() {
		//given
		String name = "건우의 아메리카노";
		String englishName = "Geonwoo's Americano";
		ItemType beverage = ItemType.BEVERAGE;

		ItemCategoryRegisterRequest request = new ItemCategoryRegisterRequest(name, englishName, beverage);

		given(itemCategoryRepository.findByItemTypeAndName(beverage, name))
			.willReturn(Optional.empty());

		ItemCategory itemCategory = new ItemCategory(name, englishName, beverage);

		given(itemCategoryRepository.save(any()))
			.willReturn(itemCategory);

		//when
		ItemCategory category = itemCategoryService.register(request);

		//then
		assertThat(category.getItemType()).isEqualTo(beverage);
		assertThat(category.getName()).isEqualTo(name);
		assertThat(category.getEnglishName()).isEqualTo(englishName);

		verify(itemCategoryRepository).findByItemTypeAndName(beverage, name);
		verify(itemCategoryRepository).save(any());
	}

	@DisplayName("등록 - itemCategory가 이미 존재할 경우 생성에 실패한다.")
	@Test
	void register_duplicateCategory_fail() {
		//given
		String name = "건우의 아메리카노";
		String englishName = "Geonwoo's Americano";
		ItemType beverage = ItemType.BEVERAGE;

		ItemCategoryRegisterRequest request = new ItemCategoryRegisterRequest(name, englishName, beverage);
		ItemCategory itemCategory = new ItemCategory(name, englishName, beverage);

		given(itemCategoryRepository.findByItemTypeAndName(beverage, name))
			.willReturn(Optional.of(itemCategory));

		//when & then
		assertThrows(DuplicateInsertException.class,
			() -> itemCategoryService.register(request));

		verify(itemCategoryRepository).findByItemTypeAndName(beverage, name);
	}

	@DisplayName("조회 - itemType에 해당하는 itemCategory list를 정상 조회한다.")
	@Test
	void findAllByType_success() {
		//given
		ItemType beverage = ItemType.BEVERAGE;
		List<ItemCategory> itemCategories = List.of(
			createItemCategory("건우의 아메리카노", "Geonwoo's Americano", beverage),
			createItemCategory("영지리카노", "youngjilicano", beverage),
			createItemCategory("리저브", "reserve", beverage)
		);

		given(itemCategoryRepository.findByItemType(beverage))
			.willReturn(itemCategories);

		//when
		ItemCategoryResponses findItemCategories = itemCategoryService.findAllByType(beverage);
		//then
		assertThat(findItemCategories.getCategories()).hasSize(3)
			.extracting(ItemCategoryResponse::getType)
			.contains(beverage);
		verify(itemCategoryRepository).findByItemType(beverage);
	}

	@DisplayName("조회 - itemType에 해당하는 itemCategory가 없을 경우 빈 list를 반환한다.")
	@Test
	void findAllByType_empty_success() {
		//given
		ItemType itemType = ItemType.PRODUCT;
		given(itemCategoryRepository.findByItemType(itemType))
			.willReturn(Collections.emptyList());

		//when
		ItemCategoryResponses categories = itemCategoryService.findAllByType(itemType);

		//then
		assertThat(categories.getCategories().size()).isEqualTo(0);

		verify(itemCategoryRepository).findByItemType(itemType);
	}

	@DisplayName("조회 - name 으로 ItemCategory를 정상 조회한다.")
	@Test
	void findByName_success() {
		//given
		String name = "건우의 아메리카노";
		String englishName = "Geonwoo's Americano";
		ItemType beverage = ItemType.BEVERAGE;

		ItemCategory itemCategory = createItemCategory(name, englishName, beverage);
		given(itemCategoryRepository.findByName(name))
			.willReturn(Optional.of(itemCategory));

		//when
		ItemCategory findItemCategory = itemCategoryService.findByName(name);

		//then
		assertThat(findItemCategory).usingRecursiveComparison().isEqualTo(itemCategory);
		verify(itemCategoryRepository).findByName(name);
	}

	@DisplayName("조회 - 존재하지 않는 ItemCategory name을 조회할 경우 예외를 발생시킨다.")
	@Test
	void findByName_nonExist_fail() {
		//given
		String name = "건우의 아메리카노";
		given(itemCategoryRepository.findByName(name))
			.willReturn(Optional.empty());

		//when
		assertThrows(EntityNotFoundException.class,
			() -> itemCategoryService.findByName(name));

		//then
		verify(itemCategoryRepository).findByName(name);
	}

}