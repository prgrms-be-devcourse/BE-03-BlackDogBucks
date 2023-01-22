package com.prgrms.bdbks.domain.item.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.bdbks.common.exception.EntityNotFoundException;
import com.prgrms.bdbks.domain.item.dto.ItemCreateRequest;
import com.prgrms.bdbks.domain.item.entity.DefaultOption;
import com.prgrms.bdbks.domain.item.entity.ItemCategory;

import lombok.RequiredArgsConstructor;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ItemFacadeService {

	private final ItemCategoryService itemCategoryService;

	private final ItemService itemService;

	private final ItemOptionService itemOptionService;

	@Transactional
	public Long createItem(ItemCreateRequest request) {
		ItemCategory itemCategory = itemCategoryService.findByTypeAndName(request.getItemType(),
				request.getCategoryName())
			.orElseThrow(() -> new EntityNotFoundException(ItemCategory.class, request.getItemType(),
				request.getCategoryName()));

		DefaultOption defaultOption = itemOptionService.create(request.getDefaultOptionRequest());

		return itemService.createItem(request, itemCategory, defaultOption);
	}

}