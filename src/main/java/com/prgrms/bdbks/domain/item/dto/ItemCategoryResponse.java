package com.prgrms.bdbks.domain.item.dto;

import com.prgrms.bdbks.domain.item.entity.ItemType;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ItemCategoryResponse {

	private Long categoryId;

	private String name;

	private String englishName;

	private ItemType type;

}
