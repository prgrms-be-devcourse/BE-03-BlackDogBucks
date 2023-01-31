package com.prgrms.bdbks.domain.store.converter;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import com.prgrms.bdbks.domain.store.dto.StoreCreateRequest;
import com.prgrms.bdbks.domain.store.entity.Store;

@Mapper(
	unmappedSourcePolicy = ReportingPolicy.IGNORE,
	nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL,
	componentModel = "spring")
public interface StoreMapper {

	Store toEntity(StoreCreateRequest storeCreateRequest);

}
