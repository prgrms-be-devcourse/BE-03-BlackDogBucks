package com.prgrms.bdbks.domain.item.dto;

import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemResponses {

	private List<ItemResponse> items;

}
