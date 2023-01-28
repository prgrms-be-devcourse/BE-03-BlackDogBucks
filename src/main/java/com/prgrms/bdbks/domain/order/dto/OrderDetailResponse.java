package com.prgrms.bdbks.domain.order.dto;

import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class OrderDetailResponse {

	private String orderId;

	private int orderCount;

	private String orderStatus;

	private List<OrderItemResponse> orderItems;

	private String storeName;

	private String nickname;

	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	@AllArgsConstructor
	public static class OrderItemResponse {

		private String itemName;

		private String cupSize;

		private String cupType;

		private String image;

	}

}
