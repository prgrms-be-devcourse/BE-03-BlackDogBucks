package com.prgrms.bdbks.domain.star.dto;

import lombok.Getter;

@Getter
public class StarSearchResponse {

	private Long starId;

	private Long userId;

	private short count;

	public StarSearchResponse(Long starId, Long userId, short count) {
		this.starId = starId;
		this.userId = userId;
		this.count = count;
	}
}