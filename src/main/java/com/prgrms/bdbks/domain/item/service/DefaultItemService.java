package com.prgrms.bdbks.domain.item.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.bdbks.domain.item.dto.ItemCreateRequest;
import com.prgrms.bdbks.domain.item.dto.ItemDetailResponse;
import com.prgrms.bdbks.domain.item.dto.ItemResponses;
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

	@Override
	public Optional<Item> findById(Long itemId) {
		return Optional.empty();
	}

	@Override
	public ItemResponses findByCategoryName(ItemType itemType, String categoryName) {
		return null;
	}

	@Transactional
	@Override
	public Long createItem(ItemCreateRequest request, ItemCategory itemCategory, DefaultOption defaultOption) {

		Item item = Item.builder()
			.name(request.getName())
			.englishName(request.getEnglishName())
			.description(request.getDescription())
			.image(request.getImage())
			.price(request.getPrice())
			.category(itemCategory)
			.defaultOption(defaultOption)
			.build();

		return itemRepository.save(item).getId();
	}

	@Override
	public ItemDetailResponse findItemDetail(Long itemId) {
		return null;
	}

}
