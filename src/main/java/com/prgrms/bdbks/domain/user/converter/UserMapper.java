package com.prgrms.bdbks.domain.user.converter;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import com.prgrms.bdbks.domain.user.dto.UserCreateRequest;
import com.prgrms.bdbks.domain.user.dto.UserFindResponse;
import com.prgrms.bdbks.domain.user.entity.User;

@Mapper(
	unmappedTargetPolicy = ReportingPolicy.IGNORE,
	nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL,
	componentModel = "spring")
public interface UserMapper {

	User createRequestToEntity(UserCreateRequest userCreateRequest);

	UserFindResponse entityToFindResponse(User user);

}
