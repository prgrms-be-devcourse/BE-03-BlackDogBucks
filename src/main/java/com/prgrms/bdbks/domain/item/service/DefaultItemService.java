package com.prgrms.bdbks.domain.item.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.bdbks.domain.item.converter.ItemMapper;
import com.prgrms.bdbks.domain.item.dto.ItemCreateRequest;
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

}