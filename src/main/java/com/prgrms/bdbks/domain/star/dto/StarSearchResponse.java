package com.prgrms.bdbks.domain.star.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StarSearchResponse {

	private Long starId;

	private Long userId;

	private short count;

}