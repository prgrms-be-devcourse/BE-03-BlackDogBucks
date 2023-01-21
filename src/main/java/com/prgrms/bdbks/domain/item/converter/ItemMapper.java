package com.prgrms.bdbks.domain.item.converter;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.prgrms.bdbks.domain.item.dto.ItemCreateRequest;
import com.prgrms.bdbks.domain.item.entity.Item;
import com.prgrms.bdbks.domain.item.entity.ItemCategory;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ItemMapper {

	@Mapping(source = "itemCategory", target = "category")
	@Mapping(source = "itemCreateRequest.defaultOptionRequest", target = "defaultOption")
	@Mapping(source = "itemCreateRequest.name", target = "name")
	@Mapping(source = "itemCreateRequest.englishName", target = "englishName")
	Item itemCreateRequestToItem(ItemCreateRequest itemCreateRequest, ItemCategory itemCategory);
}
