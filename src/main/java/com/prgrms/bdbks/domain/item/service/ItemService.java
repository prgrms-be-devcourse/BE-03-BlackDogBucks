package com.prgrms.bdbks.domain.item.service;

import java.util.List;
import java.util.Optional;

import com.prgrms.bdbks.domain.item.dto.CustomItem;
import com.prgrms.bdbks.domain.item.dto.ItemCreateRequest;
import com.prgrms.bdbks.domain.item.dto.ItemDetailResponse;
import com.prgrms.bdbks.domain.item.dto.ItemResponses;
import com.prgrms.bdbks.domain.item.entity.Item;
import com.prgrms.bdbks.domain.item.entity.ItemType;
import com.prgrms.bdbks.domain.order.dto.OrderCreateRequest;

public interface ItemService {

	Optional<Item> findById(Long itemId);

	Long createItem(ItemCreateRequest request);

	ItemResponses findAllBy(ItemType itemType, String categoryName);

	ItemDetailResponse findItemDetailBy(Long itemId);

	Item findByIdWithDefaultOption(Long itemId);

	List<CustomItem> customItems(List<OrderCreateRequest.OrderItemRequest> orderItems);
}