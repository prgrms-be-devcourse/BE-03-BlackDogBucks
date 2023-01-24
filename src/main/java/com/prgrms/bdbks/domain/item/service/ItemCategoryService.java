package com.prgrms.bdbks.domain.item.service;

import java.util.List;
import java.util.Optional;

import com.prgrms.bdbks.domain.item.dto.ItemCategoryRegisterRequest;
import com.prgrms.bdbks.domain.item.entity.ItemCategory;
import com.prgrms.bdbks.domain.item.entity.ItemType;

public interface ItemCategoryService {

	ItemCategory register(ItemCategoryRegisterRequest request);

	List<ItemCategory> findAllByType(ItemType itemType);

	ItemCategory findByName(String name);

	Optional<ItemCategory> findByTypeAndName(ItemType itemType, String categoryName);
}

