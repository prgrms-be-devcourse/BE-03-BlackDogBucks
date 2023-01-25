package com.prgrms.bdbks.domain.item.dto;

import com.prgrms.bdbks.domain.item.entity.ItemType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ItemResponse {

	private Long itemId;

	private String image;

	private String name;

	private String englishName;

	private int price;

	private Boolean isNew;

	private Boolean isBest;

	private ItemType type;

	private String categoryName;

}
