package com.prgrms.bdbks.domain.item.dto;

import com.prgrms.bdbks.domain.item.entity.DefaultOption;
import com.prgrms.bdbks.domain.item.entity.Item;

import lombok.Getter;

@Getter
public class ItemDetailResponse {

	private final Long itemId;

	private final String image;

	private final String name;

	private final String englishName;

	private final int price;

	private final Boolean isNew;

	private final Boolean isBest;

	private final OptionResponse option;

	public ItemDetailResponse(Item item) {
		this.itemId = item.getId();
		this.image = item.getImage();
		this.name = item.getName();
		this.englishName = item.getEnglishName();
		this.price = item.getPrice();
		this.isNew = item.getIsNew();
		this.isBest = item.getIsBest();
		DefaultOption defaultOption = item.getDefaultOption();
		this.option = new OptionResponse(defaultOption.getEspressoType(), defaultOption.getEspressoShotCount(),
			defaultOption.getVanillaSyrupCount(), defaultOption.getClassicSyrupCount(),
			defaultOption.getHazelnutSyrupCount(),
			defaultOption.getMilkType(), defaultOption.getMilkAmount());
	}

}
