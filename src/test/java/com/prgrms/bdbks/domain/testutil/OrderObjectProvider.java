package com.prgrms.bdbks.domain.testutil;

import com.prgrms.bdbks.domain.coupon.entity.Coupon;
import com.prgrms.bdbks.domain.item.entity.BeverageOption;
import com.prgrms.bdbks.domain.order.entity.CustomOption;
import com.prgrms.bdbks.domain.order.entity.Order;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderObjectProvider {

	public static Order createOrder(Coupon coupon, Long userId, String storeId, int totalPrice) {

		return Order.builder()
			.coupon(coupon)
			.userId(userId)
			.storeId(storeId)
			.totalPrice(totalPrice)
			.build();
	}

	public static Order createOrder() {
		return Order.builder()
			.coupon(null)
			.userId(1L)
			.storeId("123456789")
			.totalPrice(45000)
			.build();
	}

	public static CustomOption createCustomOption(Integer espressoShotCount, Integer vanillaSyrupCount,
		Integer classicSyrupCount, Integer hazelnutSyrupCount, BeverageOption.Milk milkType,
		BeverageOption.Coffee espressoType,
		BeverageOption.MilkAmount milkAmount, BeverageOption.Size cupSize, BeverageOption.CupType cupType) {

		return CustomOption.builder()
			.espressoType(espressoType)
			.espressoShotCount(espressoShotCount)
			.vanillaSyrupCount(vanillaSyrupCount)
			.classicSyrupCount(classicSyrupCount)
			.hazelnutSyrupCount(hazelnutSyrupCount)
			.milkAmount(milkAmount)
			.milkType(milkType)
			.cupSize(cupSize)
			.cupType(cupType)
			.build();

	}

	public static CustomOption createCustomOption() {
		return createCustomOption(1, 0, 0, 0,
			BeverageOption.Milk.OAT, BeverageOption.Coffee.DECAFFEINATED,
			BeverageOption.MilkAmount.MEDIUM, BeverageOption.Size.TALL,
			BeverageOption.CupType.STORE);
	}

}
