package com.prgrms.bdbks.domain.order.service;

import java.util.List;

import com.prgrms.bdbks.domain.coupon.entity.Coupon;
import com.prgrms.bdbks.domain.item.dto.CustomItem;
import com.prgrms.bdbks.domain.order.entity.Order;

public interface OrderService {

	Order createOrder(Coupon coupon, Long userId, String storeId);

	Order findByIdWithOrderItemsAndCustomOption(String orderId);

	Order createOrder(Coupon coupon, Long userId, String id, List<CustomItem> customItems);

	Order findById(String orderId);
}
