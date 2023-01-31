package com.prgrms.bdbks.domain.order.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.prgrms.bdbks.common.dto.SliceResponse;
import com.prgrms.bdbks.domain.coupon.entity.Coupon;
import com.prgrms.bdbks.domain.item.dto.CustomItem;
import com.prgrms.bdbks.domain.order.dto.OrderByStoreResponse;
import com.prgrms.bdbks.domain.order.entity.Order;
import com.prgrms.bdbks.domain.order.entity.OrderStatus;

public interface OrderService {

	Order createOrder(Coupon coupon, Long userId, String storeId);

	Order findByIdWithOrderItemsAndCustomOption(String orderId);

	Order createOrder(Coupon coupon, Long userId, String id, List<CustomItem> customItems);

	SliceResponse<OrderByStoreResponse> findAllStoreOrdersBy(String storeId, OrderStatus orderStatus,
		String cursorOrderId,
		Pageable pageable);

}
