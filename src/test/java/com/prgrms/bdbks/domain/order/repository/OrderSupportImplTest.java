package com.prgrms.bdbks.domain.order.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.TestConstructor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.prgrms.bdbks.config.jpa.JpaConfig;
import com.prgrms.bdbks.domain.order.dto.OrderByStoreResponse;
import com.prgrms.bdbks.domain.order.entity.OrderStatus;

import lombok.RequiredArgsConstructor;

@DataJpaTest
@Import(JpaConfig.class)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
class OrderSupportImplTest {

	private final OrderRepository orderRepository;

	@DisplayName("조회 - OrderId로  Order 와 OrderItems 와 CustomOption 과 Item을 조회할 수 있다.")
	@Test
	void findAllByItemTypeAndCategoryName_success() {
		// given
		String orderId = "12340591324812387512375537513713951391329539";

		orderRepository.findByIdWithOrderItemsAndCustomOption(orderId);
	}

	@DisplayName("조회 - StoreId, OrderStatus, PageRequest로 주문 목록을 찾는다.")
	@Test
	void findStoreOrders() throws JsonProcessingException {
		Slice<OrderByStoreResponse> slice = orderRepository.findBy("storeId", OrderStatus.PAYMENT_COMPLETE,
			null,
			PageRequest.of(0, 10, Sort.by(
				Sort.Direction.DESC, "createdAt")));
	}

}