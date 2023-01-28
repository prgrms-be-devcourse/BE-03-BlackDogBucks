package com.prgrms.bdbks.domain.order.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.bdbks.common.exception.EntityNotFoundException;
import com.prgrms.bdbks.domain.coupon.entity.Coupon;
import com.prgrms.bdbks.domain.item.dto.CustomItem;
import com.prgrms.bdbks.domain.item.entity.OptionPrice;
import com.prgrms.bdbks.domain.order.entity.Order;
import com.prgrms.bdbks.domain.order.entity.OrderItem;
import com.prgrms.bdbks.domain.order.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultOrderService implements OrderService {

	private final OrderRepository orderRepository;

	private final OptionPrice optionPrice;

	@Transactional
	@Override
	public Order createOrder(Coupon coupon, Long userId, String storeId) {
		Order newOrder = Order.create(coupon, userId, storeId);
		return orderRepository.save(newOrder);
	}

	@Transactional(readOnly = true)
	@Override
	public Order findByIdWithOrderItemsAndCustomOption(String orderId) {
		return orderRepository.findByIdWithOrderItemsAndCustomOption(orderId)
			.orElseThrow(() -> new EntityNotFoundException(Order.class, orderId));
	}

	@Transactional
	@Override
	public Order createOrder(Coupon coupon, Long userId, String storeId, List<CustomItem> customItems) {
		Order newOrder = Order.create(coupon, userId, storeId);
		customItems.forEach(it ->
			OrderItem.create(newOrder, it.getItem(), it.getCustomOption(), it.getQuantity(), optionPrice));

		return orderRepository.save(newOrder);
	}

}
