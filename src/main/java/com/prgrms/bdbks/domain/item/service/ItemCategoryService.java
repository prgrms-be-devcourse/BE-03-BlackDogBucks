package com.prgrms.bdbks.domain.item.service;

import java.util.Optional;

import com.prgrms.bdbks.domain.item.dto.ItemCategoryRegisterRequest;
import com.prgrms.bdbks.domain.item.dto.ItemCategoryResponses;
import com.prgrms.bdbks.domain.item.entity.ItemCategory;
import com.prgrms.bdbks.domain.item.entity.ItemType;

public interface ItemCategoryService {

	ItemCategory register(ItemCategoryRegisterRequest request);

	ItemCategory findByName(String name);

	Optional<ItemCategory> findByTypeAndName(ItemType itemType, String categoryName);

	ItemCategoryResponses findAllByType(ItemType itemType);

}

