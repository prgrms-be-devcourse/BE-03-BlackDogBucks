package com.prgrms.bdbks.domain.item.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.bdbks.domain.item.dto.ItemCreateRequest;
import com.prgrms.bdbks.domain.item.dto.ItemDetailResponse;
import com.prgrms.bdbks.domain.item.dto.ItemResponses;
import com.prgrms.bdbks.domain.item.entity.Item;
import com.prgrms.bdbks.domain.item.entity.ItemCategory;
import com.prgrms.bdbks.domain.item.entity.ItemType;
import com.prgrms.bdbks.domain.item.repository.ItemRepository;

import lombok.RequiredArgsConstructor;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class DefaultItemService implements ItemService {

	private final ItemRepository itemRepository;

	@Override
	public Optional<Item> findById(Long itemId) {
		return Optional.empty();
	}

	@Override
	public ItemResponses findByCategoryName(ItemType itemType, String categoryName) {
		return null;
	}

	@Override
	public Long createItem(ItemCreateRequest request, ItemCategory itemCategory) {

		Item.builder().name(request.getCategoryName());

		return null;
	}

	@Override
	public ItemDetailResponse findItemDetail(Long itemId) {
		return null;
	}

}
