package com.prgrms.bdbks.domain.user.converter;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.ValueMapping;

import com.prgrms.bdbks.domain.user.dto.UserCreateRequest;
import com.prgrms.bdbks.domain.user.dto.UserFindResponse;
import com.prgrms.bdbks.domain.user.entity.User;

@Mapper(
	unmappedSourcePolicy = ReportingPolicy.IGNORE,
	nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL,
	componentModel = "spring")
public interface UserMapper {

	@Mapping(target = "role", source = "userCreateRequest.role")
	@ValueMapping(source = "USER", target = "Role.USER")
	@ValueMapping(source = "ADMIN", target = "Role.ADMIN")
	User createRequestToEntity(UserCreateRequest userCreateRequest);

	UserFindResponse entityToFindResponse(User user);
}
