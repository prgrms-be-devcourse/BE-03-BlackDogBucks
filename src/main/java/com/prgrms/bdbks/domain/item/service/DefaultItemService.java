package com.prgrms.bdbks.domain.item.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.bdbks.common.exception.EntityNotFoundException;
import com.prgrms.bdbks.domain.item.converter.ItemMapper;
import com.prgrms.bdbks.domain.item.dto.ItemCreateRequest;
import com.prgrms.bdbks.domain.item.dto.ItemDetailResponse;
import com.prgrms.bdbks.domain.item.dto.ItemResponse;
import com.prgrms.bdbks.domain.item.entity.DefaultOption;
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

	private final ItemMapper itemMapper;

	@Override
	public Optional<Item> findById(Long itemId) {
		return Optional.empty();
	}

	@Override
	public List<Item> findByCategoryName(ItemType itemType, String categoryName) {
		return null;
	}

	@Transactional
	@Override
	public Long createItem(ItemCreateRequest request, ItemCategory itemCategory, DefaultOption defaultOption) {

		Item item = itemMapper.itemCreateRequestToEntity(request, itemCategory, defaultOption);

		return itemRepository.save(item).getId();
	}

	@Override
	public List<ItemResponse> findAllBy(ItemType itemType, String categoryName) {
		return itemRepository.findAllByItemTypeAndCategoryName(itemType, categoryName)
			.stream().map(item -> itemMapper.itemToItemResponse(item, itemType, categoryName))
			.collect(Collectors.toList());
	}

	@Override
	public ItemDetailResponse findItemDetailBy(Long itemId) {
		Item item = itemRepository.findByIdWithOption(itemId)
			.orElseThrow(() -> new EntityNotFoundException(Item.class, itemId));

		return new ItemDetailResponse(item);
	}

}