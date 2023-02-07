package com.prgrms.bdbks.domain.item.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.bdbks.common.exception.DuplicateInsertException;
import com.prgrms.bdbks.common.exception.EntityNotFoundException;
import com.prgrms.bdbks.domain.item.dto.ItemCategoryRegisterRequest;
import com.prgrms.bdbks.domain.item.dto.ItemCategoryResponse;
import com.prgrms.bdbks.domain.item.dto.ItemCategoryResponses;
import com.prgrms.bdbks.domain.item.entity.ItemCategory;
import com.prgrms.bdbks.domain.item.entity.ItemType;
import com.prgrms.bdbks.domain.item.repository.ItemCategoryRepository;

import lombok.RequiredArgsConstructor;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class DefaultItemCategoryService implements ItemCategoryService {

	private final ItemCategoryRepository itemCategoryRepository;

	@Transactional
	@Override
	public ItemCategory register(ItemCategoryRegisterRequest request) {

		validateDuplicateCategory(request.getItemType(), request.getName());

		ItemCategory newItemCategory = ItemCategory.builder().itemType(request.getItemType())
			.name(request.getName())
			.englishName(request.getEnglishName())
			.build();

		return itemCategoryRepository.save(newItemCategory);
	}

	@Override
	public ItemCategory findByName(String name) {
		return itemCategoryRepository.findByName(name)
			.orElseThrow(() -> new EntityNotFoundException(ItemCategory.class, name));
	}

	@Transactional(readOnly = true)
	@Override
	public Optional<ItemCategory> findByTypeAndName(ItemType itemType, String categoryName) {
		return itemCategoryRepository.findByItemTypeAndName(itemType, categoryName);
	}

	@Override
	public ItemCategoryResponses findAllByType(ItemType itemType) {
		List<ItemCategoryResponse> categoryResponses = itemCategoryRepository.findByItemTypeOrderById(itemType)
			.stream()
			.map(category -> new ItemCategoryResponse(category.getId(), category.getName(), category.getEnglishName(),
				category.getItemType()))
			.collect(Collectors.toList());

		return new ItemCategoryResponses(categoryResponses);
	}

	protected void validateDuplicateCategory(ItemType itemType, String name) {
		boolean isPresent = findByTypeAndName(itemType, name)
			.isPresent();

		if (isPresent) {
			throw new DuplicateInsertException("이미 저장되어 있는 아이템 카테고리 입니다. " + itemType + "," + name);
		}

	}

}
