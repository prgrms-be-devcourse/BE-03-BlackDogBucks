package com.prgrms.bdbks.domain.order.entity;

import static com.prgrms.bdbks.domain.testutil.ItemObjectProvider.*;
import static com.prgrms.bdbks.domain.testutil.OrderObjectProvider.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.prgrms.bdbks.domain.coupon.entity.Coupon;
import com.prgrms.bdbks.domain.item.entity.Item;
import com.prgrms.bdbks.domain.item.entity.OptionPrice;

@DisplayName("Order 테스트")
class OrderTest {

	private final Long userId = 439854L;
	private final Coupon coupon = Coupon.createCoupon(userId);
	private final String storeId = "20304";
	private final Item item = createIcedAmericano();
	private final CustomOption customOption = createCustomOption();
	private final OptionPrice optionPrice = new OptionPrice();

	@DisplayName("생성 - create() - 생성에 성공한다.")
	@Test
	void create_success() {
		//given
		//when & then
		assertDoesNotThrow(() -> {
			Order.create(coupon, userId, storeId);
		});
	}

	@DisplayName("생성 - create() - userId가 null 이면 생성에 실패한다.")
	@Test
	void create_userIdNull_fail() {
		//given
		Long nullUserId = null;

		//when & then
		assertThrows(NullPointerException.class, () ->
			Order.create(coupon, nullUserId, storeId));
	}

	@DisplayName("생성 - create() - storeId가 null 이면 생성에 실패한다.")
	@Test
	void create_storeIdNull_fail() {
		//given
		String nullStoreId = null;

		//when & then
		assertThrows(NullPointerException.class, () ->
			Order.create(coupon, userId, nullStoreId));
	}

	@DisplayName("addOrderItem() - orderItem이 추가된다.")
	@Test
	void addOrderItem_success() {
		//given & when
		Order order = Order.create(coupon, userId, storeId);
		OrderItem orderItem = OrderItem.create(order, item, customOption, 1, optionPrice);

		//when
		order.addOrderItem(orderItem);

		//then
		assertEquals(order, orderItem.getOrder());
		assertTrue(order.getOrderItems().contains(orderItem));
	}

	@DisplayName("addOrderItem() - orderItem null이면 예외를 던지고 추가될 수 없다. ")
	@Test
	void addOrderItem_nullOrderItem_fail() {
		//given
		Order order = Order.create(coupon, userId, storeId);

		//when
		assertThrows(NullPointerException.class, () -> order.addOrderItem(null));
	}

	@DisplayName("calculateTotalPrice() - order의 totalPrice 를 정상 계산한다.")
	@Test
	void calculateTotalPrice_success() {
		//given
		Order order = Order.create(coupon, userId, storeId);

		//when
		int americanoQuantity = 3;
		OrderItem americanoOrderItem = OrderItem.create(order, createIcedAmericano(), customOption, americanoQuantity,
			optionPrice);

		assertEquals(americanoOrderItem.getTotalPrice() - coupon.getPrice(), order.getTotalPrice());

		int caffeLatteQuantity = 10;
		OrderItem cafeLatteOrderItem = OrderItem.create(order, createCaffeLatte(), customOption, caffeLatteQuantity,
			optionPrice);

		assertEquals(americanoOrderItem.getTotalPrice() + cafeLatteOrderItem.getTotalPrice()
				- coupon.getPrice(),
			order.getTotalPrice());
	}

}