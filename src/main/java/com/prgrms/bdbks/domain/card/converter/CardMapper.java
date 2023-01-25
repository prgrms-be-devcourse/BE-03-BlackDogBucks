package com.prgrms.bdbks.domain.card.converter;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.prgrms.bdbks.domain.card.dto.CardSearchResponse;
import com.prgrms.bdbks.domain.card.entity.Card;

@Mapper(componentModel = "spring")
public interface CardMapper {

	@Mappings({
		@Mapping(source = "id", target = "cardId"),
		@Mapping(source = "name", target = "name"),
		@Mapping(source = "amount", target = "amount")
	})
	CardSearchResponse toCardSearchResponse(Card card);
}