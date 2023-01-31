package com.prgrms.bdbks.domain.order.repository;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.prgrms.bdbks.domain.order.dto.OrderByStoreResponse;
import com.prgrms.bdbks.domain.order.entity.Order;
import com.prgrms.bdbks.domain.order.entity.OrderStatus;

public interface OrderSupport {

	Optional<Order> findByIdWithOrderItemsAndCustomOption(String orderId);

	Slice<OrderByStoreResponse> findBy(String storeId, OrderStatus orderStatus, String cursorOrderId,
		Pageable pageRequest);
}
