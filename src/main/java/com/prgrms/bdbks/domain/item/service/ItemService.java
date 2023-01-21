package com.prgrms.bdbks.domain.item.service;

import java.util.Optional;

import com.prgrms.bdbks.domain.item.dto.ItemCreateRequest;
import com.prgrms.bdbks.domain.item.dto.ItemDetailResponse;
import com.prgrms.bdbks.domain.item.dto.ItemResponses;
import com.prgrms.bdbks.domain.item.entity.Item;
import com.prgrms.bdbks.domain.item.entity.ItemCategory;
import com.prgrms.bdbks.domain.item.entity.ItemType;

public interface ItemService {

	Optional<Item> findById(Long itemId);

	ItemResponses findByCategoryName(ItemType itemType, String categoryName);

	Long createItem(ItemCreateRequest request, ItemCategory itemCategory);

	ItemDetailResponse findItemDetail(Long itemId);

}
