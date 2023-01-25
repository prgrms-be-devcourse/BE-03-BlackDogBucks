package com.prgrms.bdbks.domain.item.entity;

import java.util.Arrays;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Getter;

@Getter
public enum ItemType {

	BEVERAGE("음료", "Beverage"),
	FOOD("푸드", "Food"),
	PRODUCT("상품", "Product");

	private final String korName;

	private final String englishName;

	ItemType(String korName, String englishName) {
		this.korName = korName;
		this.englishName = englishName;
	}

	@JsonCreator
	public static ItemType of(String typeName) {
		return Arrays.stream(values())
			.filter(itemType -> Objects.equals(itemType.name(), typeName))
			.findFirst().orElseThrow();
	}

}
