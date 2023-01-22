package com.prgrms.bdbks.domain.testutil;

import com.prgrms.bdbks.domain.item.entity.BeverageOption;
import com.prgrms.bdbks.domain.item.entity.DefaultOption;
import com.prgrms.bdbks.domain.item.entity.Item;
import com.prgrms.bdbks.domain.item.entity.ItemCategory;
import com.prgrms.bdbks.domain.item.entity.ItemType;
import com.prgrms.bdbks.domain.order.entity.CustomOption;
import com.prgrms.bdbks.domain.order.entity.Order;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderObjectProvider {

	public static ItemCategory createReserveEspressoCategory() {
		ItemType beverage = ItemType.BEVERAGE;
		String itemCategoryName = "리저브 에스프레소";
		String itemCategoryEnglishName = "Reserve Espresso";

		return ItemCategory.builder()
			.name(itemCategoryName)
			.englishName(itemCategoryEnglishName)
			.itemType(beverage)
			.build();
	}

	public static Item createIcedAmericano() {
		ItemCategory itemCategory = createReserveEspressoCategory();
		String name = "아이스 아메리카노";
		String description = "진한 에스프레소에 시원한 정수물과 얼음을 더하여 스타벅스의 깔끔하고 강렬한 에스프레소를 가장 부드럽고 시원하게 즐길 수 있는 커피";
		String englishName = "Iced Caffe Americano";
		Integer price = 4500;
		DefaultOption defaultOption = ItemObjectProvider.createDefaultOption(1, 0, 0, 0,
			BeverageOption.Milk.OAT, BeverageOption.Coffee.DECAFFEINATED,
			BeverageOption.MilkAmount.MEDIUM);

		String image = "https://hkbks.com/api/image.jpg";

		return Item.builder()
			.description(description)
			.name(name)
			.category(itemCategory)
			.englishName(englishName)
			.price(price)
			.image(image)
			.defaultOption(defaultOption)
			.build();
	}

	public static Item createItem(String name, ItemCategory category, String englishName, int price, String image,
		String description) {

		return Item.builder()
			.name(name)
			.category(category)
			.englishName(englishName)
			.price(price)
			.image(image)
			.build();
	}

	public static Order createOrder(Long coupon, Long userId, String storeId, int totalPrice) {

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
