package com.prgrms.bdbks.domain.item.dto;

import com.prgrms.bdbks.domain.item.entity.Item;
import com.prgrms.bdbks.domain.order.entity.CustomOption;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomItem {

	private final Item item;

	private final CustomOption customOption;

	private int quantity;

}
