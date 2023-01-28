package com.prgrms.bdbks.domain.order.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestConstructor;

import com.prgrms.bdbks.CustomDataJpaTest;
import com.prgrms.bdbks.config.jpa.JpaConfig;

import lombok.RequiredArgsConstructor;

@CustomDataJpaTest
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

}