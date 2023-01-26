package com.prgrms.bdbks.domain.order.repository;

import java.util.Optional;

import com.prgrms.bdbks.domain.order.entity.Order;

public interface OrderSupport {

	Optional<Order> findByIdWithOrderItemsAndCustomOption(String orderId);

}
