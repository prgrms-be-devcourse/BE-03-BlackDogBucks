package com.prgrms.bdbks.domain.card.converter;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

import com.prgrms.bdbks.domain.card.dto.CardSaveResponse;
import com.prgrms.bdbks.domain.card.dto.CardSearchResponse;
import com.prgrms.bdbks.domain.card.entity.Card;

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE,
	componentModel = "spring")
public interface CardMapper {

	@Mappings({
		@Mapping(source = "chargeCardId", target = "chargeCardId"),
		@Mapping(source = "name", target = "name"),
		@Mapping(source = "amount", target = "amount")
	})
	CardSearchResponse toCardSearchResponse(Card card);

	@Mappings({
		@Mapping(source = "chargeCardId", target = "chargeCardId")
	})
	CardSaveResponse toCardSaveResponse(Card card);
}