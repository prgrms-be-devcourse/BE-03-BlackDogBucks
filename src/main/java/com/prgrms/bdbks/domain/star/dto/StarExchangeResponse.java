package com.prgrms.bdbks.domain.star.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StarExchangeResponse {

	private Long userId;

	private boolean canExchange;

}
