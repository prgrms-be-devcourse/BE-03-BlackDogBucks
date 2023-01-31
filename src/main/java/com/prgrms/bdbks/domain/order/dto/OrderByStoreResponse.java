package com.prgrms.bdbks.domain.order.dto;

import java.util.List;

import com.prgrms.bdbks.domain.item.entity.BeverageOption;
import com.prgrms.bdbks.domain.order.entity.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderByStoreResponse {

	private String orderId;

	private String storeId;

	private OrderStatus orderStatus;

	private String nickname;

	private List<OrderItem> items;

	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class OrderItem {

		private String itemName;

		private Integer quantity;

		private Integer espressoShotCount;

		private Integer vanillaSyrupCount;

		private Integer classicSyrupCount;

		private Integer hazelnutSyrupCount;

		private BeverageOption.Milk milkType = null;

		private BeverageOption.Coffee espressoType = null;

		private BeverageOption.MilkAmount milkAmount = null;

		private BeverageOption.Size cupSize;

		private BeverageOption.CupType cupType;
	}
	
}
