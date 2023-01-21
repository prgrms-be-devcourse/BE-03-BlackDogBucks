package com.prgrms.bdbks.domain.testutil;

import com.prgrms.bdbks.domain.item.entity.ItemCategory;
import com.prgrms.bdbks.domain.item.entity.ItemType;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemObjectProvider {

	public static ItemCategory createItemCategory(String name, String englishName, ItemType itemType) {
		return new ItemCategory(name, englishName, itemType);
	}

}
