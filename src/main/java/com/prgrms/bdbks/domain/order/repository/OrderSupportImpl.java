package com.prgrms.bdbks.domain.order.repository;

import java.util.Optional;

import com.prgrms.bdbks.domain.item.entity.QItem;
import com.prgrms.bdbks.domain.order.entity.Order;
import com.prgrms.bdbks.domain.order.entity.QCustomOption;
import com.prgrms.bdbks.domain.order.entity.QOrder;
import com.prgrms.bdbks.domain.order.entity.QOrderItem;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OrderSupportImpl implements OrderSupport {

	private final JPAQueryFactory query;

	@Override
	public Optional<Order> findByIdWithOrderItemsAndCustomOption(String orderId) {

		Order order = query.selectFrom(QOrder.order)
			.leftJoin(QOrder.order.orderItems, QOrderItem.orderItem)
			.fetchJoin()
			.leftJoin(QOrderItem.orderItem.customOption, QCustomOption.customOption)
			.fetchJoin()
			.leftJoin(QOrderItem.orderItem.item, QItem.item)
			.fetchJoin()
			.where(QOrder.order.id.eq(orderId))
			.fetchOne();

		return Optional.ofNullable(order);
	}

}
