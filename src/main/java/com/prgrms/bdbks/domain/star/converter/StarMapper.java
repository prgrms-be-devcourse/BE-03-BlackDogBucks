package com.prgrms.bdbks.domain.star.converter;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.prgrms.bdbks.domain.star.dto.StarSearchResponse;
import com.prgrms.bdbks.domain.star.entity.Star;

@Mapper(componentModel = "spring")
public interface StarMapper {
	@Mappings({
		@Mapping(source = "id", target = "starId"),
		@Mapping(source = "user.id", target = "userId"),
		@Mapping(source = "count", target = "count"),
	})
	StarSearchResponse toStarSearchResponse(Star star);
}
