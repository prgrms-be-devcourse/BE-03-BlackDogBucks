package com.prgrms.bdbks.domain.card.converter;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.prgrms.bdbks.domain.card.dto.CardSaveResponse;
import com.prgrms.bdbks.domain.card.dto.CardSearchResponse;
import com.prgrms.bdbks.domain.card.entity.Card;

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE,
	componentModel = "spring")
public interface CardMapper {

	CardSearchResponse toCardSearchResponse(Card card);

	CardSaveResponse toCardSaveResponse(Card card);

}