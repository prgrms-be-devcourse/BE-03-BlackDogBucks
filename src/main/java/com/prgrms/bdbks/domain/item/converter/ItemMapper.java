package com.prgrms.bdbks.domain.item.converter;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.prgrms.bdbks.domain.item.dto.DefaultOptionCreateRequest;
import com.prgrms.bdbks.domain.item.dto.ItemCreateRequest;
import com.prgrms.bdbks.domain.item.dto.ItemResponse;
import com.prgrms.bdbks.domain.item.entity.DefaultOption;
import com.prgrms.bdbks.domain.item.entity.Item;
import com.prgrms.bdbks.domain.item.entity.ItemCategory;
import com.prgrms.bdbks.domain.item.entity.ItemType;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ItemMapper {

	DefaultOption defaultOptionCreateRequestToEntity(DefaultOptionCreateRequest request);

	@Mapping(source = "itemCategory", target = "category")
	@Mapping(source = "defaultOption", target = "defaultOption")
	@Mapping(source = "itemCreateRequest.name", target = "name")
	@Mapping(source = "itemCreateRequest.englishName", target = "englishName")
	Item itemCreateRequestToEntity(ItemCreateRequest itemCreateRequest, ItemCategory itemCategory,
		DefaultOption defaultOption);

	@Mapping(source = "item.id", target = "itemId")
	@Mapping(source = "item.englishName", target = "englishName")
	@Mapping(source = "categoryName", target = "categoryName")
	@Mapping(source = "itemType", target = "type")
	ItemResponse itemToItemResponse(Item item, ItemType itemType, String categoryName);

}