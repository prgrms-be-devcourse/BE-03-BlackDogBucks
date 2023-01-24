package com.prgrms.bdbks.domain.order.entity;

import static com.prgrms.bdbks.domain.testutil.ItemObjectProvider.*;
import static com.prgrms.bdbks.domain.testutil.OrderObjectProvider.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.prgrms.bdbks.domain.item.entity.Item;

@DisplayName("OrderItem 테스트")
class OrderItemTest {

	private final Integer quantity = 3;

	private final Item item = createIcedAmericano();

	private final Order order = createOrder();

	private final CustomOption customOption = createCustomOption();

	@DisplayName("생성 - OrderItem() - 생성에 성공한다.")
	@Test
	void builder_create_success() {
		// given & when & then
		assertDoesNotThrow(() -> {
			OrderItem.builder()
				.order(order)
				.item(item)
				.customOption(customOption)
				.quantity(quantity)
				.build();
		});
	}

	@DisplayName("생성 - OrderItem() - count가 0개 이하면 생성에 실패한다.")
	@ParameterizedTest
	@ValueSource(ints = {-1, -2, -3, -4, -99, 0})
	void builder_create_count_less_then0_fail(int lessThen0Count) {
		//given & when & then
		assertThrows(IllegalArgumentException.class, () -> {
			OrderItem.builder()
				.order(order)
				.item(item)
				.customOption(customOption)
				.quantity(lessThen0Count)
				.build();
		});
	}

	@DisplayName("생성 - OrderItem() - order가 null 이면 생성에 실패한다.")
	@Test
	void builder_create_order_null__fail() {
		//given & when & then
		assertThrows(NullPointerException.class, () -> {
			OrderItem.builder()
				.order(null)
				.item(item)
				.customOption(customOption)
				.quantity(quantity)
				.build();
		});
	}

	@DisplayName("생성 - OrderItem() - item 이 null 이면 생성에 실패한다.")
	@Test
	void builder_create_item_null__fail() {
		//given & when & then
		assertThrows(NullPointerException.class, () -> {
			OrderItem.builder()
				.order(order)
				.item(null)
				.customOption(customOption)
				.quantity(quantity)
				.build();
		});
	}

	@DisplayName("생성 - OrderItem() - customOption 이 null 이면 생성에 실패한다.")
	@Test
	void builder_create_custom_option_null__fail() {
		//given & when & then
		assertThrows(NullPointerException.class, () -> {
			OrderItem.builder()
				.order(order)
				.item(item)
				.customOption(null)
				.quantity(quantity)
				.build();
		});
	}

}