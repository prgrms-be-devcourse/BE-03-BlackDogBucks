package com.prgrms.bdbks.domain.item.service;

import java.util.List;
import java.util.Optional;

import com.prgrms.bdbks.domain.item.dto.ItemCreateRequest;
import com.prgrms.bdbks.domain.item.entity.DefaultOption;
import com.prgrms.bdbks.domain.item.entity.Item;
import com.prgrms.bdbks.domain.item.entity.ItemCategory;
import com.prgrms.bdbks.domain.item.entity.ItemType;

public interface ItemService {

	Optional<Item> findById(Long itemId);

	List<Item> findByCategoryName(ItemType itemType, String categoryName);

	Long createItem(ItemCreateRequest request, ItemCategory itemCategory, DefaultOption defaultOption);

}