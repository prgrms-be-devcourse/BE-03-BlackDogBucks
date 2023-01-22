package com.prgrms.bdbks.domain.testutil;

import com.prgrms.bdbks.domain.item.entity.BeverageOption;
import com.prgrms.bdbks.domain.item.entity.DefaultOption;
import com.prgrms.bdbks.domain.item.entity.ItemCategory;
import com.prgrms.bdbks.domain.item.entity.ItemType;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemObjectProvider {

	public static ItemCategory createItemCategory(String name, String englishName, ItemType itemType) {
		return new ItemCategory(name, englishName, itemType);
	}

	public static DefaultOption createDefaultOption(Integer espressoShotCount, Integer vanillaSyrupCount,
		Integer classicSyrupCount, Integer hazelnutSyrupCount, BeverageOption.Milk milkType,
		BeverageOption.Coffee espressoType, BeverageOption.MilkAmount milkAmount) {

		return DefaultOption.builder()
			.espressoShotCount(espressoShotCount)
			.hazelnutSyrupCount(hazelnutSyrupCount)
			.classicSyrupCount(classicSyrupCount)
			.vanillaSyrupCount(vanillaSyrupCount)
			.milkAmount(milkAmount)
			.espressoType(espressoType)
			.milkType(milkType)
			.build();
	}
}
