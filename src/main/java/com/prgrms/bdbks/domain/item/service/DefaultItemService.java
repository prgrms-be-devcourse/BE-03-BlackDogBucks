package com.prgrms.bdbks.domain.item.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.bdbks.common.exception.EntityNotFoundException;
import com.prgrms.bdbks.domain.item.converter.ItemMapper;
import com.prgrms.bdbks.domain.item.dto.CustomItem;
import com.prgrms.bdbks.domain.item.dto.ItemCreateRequest;
import com.prgrms.bdbks.domain.item.dto.ItemDetailResponse;
import com.prgrms.bdbks.domain.item.dto.ItemResponses;
import com.prgrms.bdbks.domain.item.entity.DefaultOption;
import com.prgrms.bdbks.domain.item.entity.Item;
import com.prgrms.bdbks.domain.item.entity.ItemCategory;
import com.prgrms.bdbks.domain.item.entity.ItemType;
import com.prgrms.bdbks.domain.item.repository.DefaultOptionRepository;
import com.prgrms.bdbks.domain.item.repository.ItemCategoryRepository;
import com.prgrms.bdbks.domain.item.repository.ItemRepository;
import com.prgrms.bdbks.domain.order.dto.OrderCreateRequest;
import com.prgrms.bdbks.domain.order.entity.CustomOption;

import lombok.RequiredArgsConstructor;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class DefaultItemService implements ItemService {

	private final ItemRepository itemRepository;

	private final ItemCategoryRepository itemCategoryRepository;

	private final ItemMapper itemMapper;

	private final DefaultOptionRepository defaultOptionRepository;

	@Override
	public Optional<Item> findById(Long itemId) {
		return Optional.empty();
	}

	@Transactional
	@Override
	public Long createItem(ItemCreateRequest request) {

		ItemCategory itemCategory = itemCategoryRepository.findByItemTypeAndName(request.getItemType(),
				request.getCategoryName())
			.orElseThrow(() -> new EntityNotFoundException(ItemCategory.class, request.getItemType(),
				request.getCategoryName()));
		DefaultOption defaultOption = itemMapper.defaultOptionCreateRequestToEntity(request.getDefaultOptionRequest());

		defaultOptionRepository.save(defaultOption);

		Item item = itemMapper.itemCreateRequestToEntity(request, itemCategory, defaultOption);

		return itemRepository.save(item).getId();
	}

	@Override
	public ItemResponses findAllBy(ItemType itemType, String categoryName) {
		return itemMapper.itemsToItemResponses(categoryName,
			itemRepository.findAllByItemTypeAndCategoryName(itemType, categoryName)
				.stream().map(item -> itemMapper.itemToItemResponse(item, itemType, categoryName))
				.collect(Collectors.toList()));

	}

	@Override
	public ItemDetailResponse findItemDetailBy(Long itemId) {
		Item item = itemRepository.findByIdWithOption(itemId)
			.orElseThrow(() -> new EntityNotFoundException(Item.class, itemId));

		return new ItemDetailResponse(item);
	}

	@Override
	public Item findByIdWithDefaultOption(Long itemId) {
		return itemRepository.findByIdWithOption(itemId).orElseThrow(() -> {
			throw new EntityNotFoundException(Item.class, itemId);
		});
	}

	@Transactional(readOnly = true)
	@Override
	public List<CustomItem> customItems(List<OrderCreateRequest.OrderItemRequest> orderItems) {
		return orderItems.stream()
			.map(req -> {
				Item item = findByIdWithDefaultOption(req.getItemId());
				DefaultOption defaultOption = item.getDefaultOption();
				OrderCreateRequest.OrderItemRequest.OrderItemOption customOptionRequest = req.getOption();
				defaultOption.validateOption(customOptionRequest);
				CustomOption customOption = itemMapper.optionRequestToEntity(customOptionRequest);
				return new CustomItem(item, customOption, req.getQuantity());
			}).collect(Collectors.toList());
	}

}