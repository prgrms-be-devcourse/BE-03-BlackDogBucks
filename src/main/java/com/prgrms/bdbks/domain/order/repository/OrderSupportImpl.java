package com.prgrms.bdbks.domain.order.repository;

import static com.prgrms.bdbks.domain.item.entity.QItem.*;
import static com.prgrms.bdbks.domain.order.entity.QCustomOption.*;
import static com.prgrms.bdbks.domain.order.entity.QOrder.*;
import static com.prgrms.bdbks.domain.order.entity.QOrderItem.*;
import static com.prgrms.bdbks.domain.user.entity.QUser.*;
import static com.querydsl.core.group.GroupBy.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;

import com.prgrms.bdbks.common.util.SliceUtil;
import com.prgrms.bdbks.domain.order.dto.OrderByStoreResponse;
import com.prgrms.bdbks.domain.order.entity.Order;
import com.prgrms.bdbks.domain.order.entity.OrderStatus;
import com.prgrms.bdbks.domain.order.entity.QOrder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OrderSupportImpl implements OrderSupport {

	private final JPAQueryFactory query;

	@Override
	public Optional<Order> findByIdWithOrderItemsAndCustomOption(String orderId) {

		Order order = query.selectFrom(QOrder.order)
			.leftJoin(QOrder.order.orderItems, orderItem)
			.fetchJoin()
			.leftJoin(orderItem.customOption, customOption)
			.fetchJoin()
			.leftJoin(orderItem.item, item)
			.fetchJoin()
			.where(QOrder.order.id.eq(orderId))
			.fetchOne();

		return Optional.ofNullable(order);
	}

	@Override
	public Slice<OrderByStoreResponse> findBy(String storeId, OrderStatus orderStatus, String cursorOrderId,
		Pageable pageable) {

		List<OrderByStoreResponse> result = query.select(order)
			.from(order)
			.leftJoin(order.orderItems, orderItem)
			.leftJoin(orderItem.item, item)
			.leftJoin(orderItem.customOption, customOption)
			.leftJoin(user).on(user.id.eq(order.userId))
			.where(
				generateCursorId(cursorOrderId, pageable.getSort()),
				order.storeId.eq(storeId),
				order.orderStatus.eq(orderStatus)
			)
			.limit(pageable.getPageSize() + 1)
			.orderBy(getOrder(pageable))
			.orderBy(order.id.desc())
			.transform(groupBy(order.id).list(
					Projections.fields(OrderByStoreResponse.class,
						order.id.as("orderId"),
						order.orderStatus.as("orderStatus"),
						user.nickname.as("nickname"),
						order.storeId.as("storeId"),
						list(Projections.list(Projections.fields(
							OrderByStoreResponse.OrderItem.class,
							item.name.as("itemName"),
							orderItem.quantity.as("quantity"),
							customOption.espressoShotCount.as("espressoShotCount"),
							customOption.vanillaSyrupCount.as("vanillaSyrupCount"),
							customOption.classicSyrupCount.as("classicSyrupCount"),
							customOption.hazelnutSyrupCount.as("hazelnutSyrupCount"),
							customOption.milkType.as("milkType"),
							customOption.espressoType.as("espressoType"),
							customOption.milkAmount.as("milkAmount"),
							customOption.cupSize.as("cupSize"),
							customOption.cupType.as("cupType")
						))).as("items")
					)
				)
			);

		return SliceUtil.toSlice(result, pageable);
	}

	private BooleanExpression generateCursorId(String cursorOrderId, Sort sort) {
		if (cursorOrderId == null) {
			return null;
		}

		return order.id.lt(cursorOrderId);
	}

	private OrderSpecifier[] getOrder(Pageable pageable) {
		List<OrderSpecifier> orders = new ArrayList<>();

		orders.add(order.id.desc());

		for (Sort.Order o : pageable.getSort()) {

			PathBuilder<Object> orderByExpression = new PathBuilder<Object>(Order.class, order.getMetadata());

			orders.add(new OrderSpecifier(
				o.isAscending() ? com.querydsl.core.types.Order.ASC : com.querydsl.core.types.Order.DESC,
				orderByExpression.get(o.getProperty())));
		}

		return orders.toArray(OrderSpecifier[]::new);
	}

}
