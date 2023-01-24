package com.prgrms.bdbks.domain.item.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ItemResponses {

	private String categoryName;

	private List<ItemResponse> items;

}
