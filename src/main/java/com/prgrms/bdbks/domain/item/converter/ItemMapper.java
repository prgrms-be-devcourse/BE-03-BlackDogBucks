package com.prgrms.bdbks.domain.item.converter;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.prgrms.bdbks.domain.item.dto.DefaultOptionCreateRequest;
import com.prgrms.bdbks.domain.item.dto.ItemCreateRequest;
import com.prgrms.bdbks.domain.item.entity.DefaultOption;
import com.prgrms.bdbks.domain.item.entity.Item;
import com.prgrms.bdbks.domain.item.entity.ItemCategory;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ItemMapper {

	DefaultOption defaultOptionCreateRequestToEntity(DefaultOptionCreateRequest request);

	@Mapping(source = "itemCategory", target = "category")
	@Mapping(source = "defaultOption", target = "defaultOption")
	@Mapping(source = "itemCreateRequest.name", target = "name")
	@Mapping(source = "itemCreateRequest.englishName", target = "englishName")
	Item itemCreateRequestToEntity(ItemCreateRequest itemCreateRequest, ItemCategory itemCategory,
		DefaultOption defaultOption);
}