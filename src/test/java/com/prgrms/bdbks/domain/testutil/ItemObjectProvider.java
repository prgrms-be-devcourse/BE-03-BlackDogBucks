package com.prgrms.bdbks.domain.testutil;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.prgrms.bdbks.domain.item.entity.BeverageOption;
import com.prgrms.bdbks.domain.item.entity.DefaultOption;
import com.prgrms.bdbks.domain.item.entity.Item;
import com.prgrms.bdbks.domain.item.entity.ItemCategory;
import com.prgrms.bdbks.domain.item.entity.ItemType;
import com.prgrms.bdbks.domain.order.dto.OrderCreateRequest;
import com.prgrms.bdbks.domain.order.entity.CustomOption;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemObjectProvider {

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
		DefaultOption defaultOption = createDefaultOption(1, 0, 0, 0,
			BeverageOption.Milk.OAT, BeverageOption.Coffee.DECAFFEINATED,
			BeverageOption.MilkAmount.MEDIUM);

		String image = "https://hkbks.com/api/image.jpg";

		return com.prgrms.bdbks.domain.item.entity.Item.builder()
			.description(description)
			.name(name)
			.category(itemCategory)
			.englishName(englishName)
			.price(price)
			.image(image)
			.defaultOption(defaultOption)
			.build();
	}

	public static Item createIcedAmericano(ItemCategory itemCategory) {

		String name = "아이스 아메리카노";
		String description = "진한 에스프레소에 시원한 정수물과 얼음을 더하여 스타벅스의 깔끔하고 강렬한 에스프레소를 가장 부드럽고 시원하게 즐길 수 있는 커피";
		String englishName = "Iced Caffe Americano";
		Integer price = 4500;
		DefaultOption defaultOption = createDefaultOption(1, 0, 0, 0,
			BeverageOption.Milk.OAT, BeverageOption.Coffee.DECAFFEINATED,
			BeverageOption.MilkAmount.MEDIUM);

		String image = "https://hkbks.com/api/image.jpg";

		return com.prgrms.bdbks.domain.item.entity.Item.builder()
			.description(description)
			.name(name)
			.category(itemCategory)
			.englishName(englishName)
			.price(price)
			.image(image)
			.defaultOption(defaultOption)
			.build();
	}

	public static Item createCaffeLatte() {
		ItemCategory itemCategory = createReserveEspressoCategory();
		String name = "카페 라떼";
		String description = "풍부하고 진한 에스프레소가 신선한 스팀 밀크를 만나 부드러워진 커피 위에 우유 거품을 살짝 얹은 대표적인 커피 라떼";
		String englishName = "Iced Caffe Americano";
		Integer price = 5000;
		DefaultOption defaultOption = createDefaultOption(1, 0, 0, 0,
			BeverageOption.Milk.NORMAL, BeverageOption.Coffee.ESPRESSO,
			BeverageOption.MilkAmount.MEDIUM);

		String image = "https://hkbks.com/api/image.jpg";

		return com.prgrms.bdbks.domain.item.entity.Item.builder()
			.description(description)
			.name(name)
			.category(itemCategory)
			.englishName(englishName)
			.price(price)
			.image(image)
			.defaultOption(defaultOption)
			.build();
	}

	public static Item createCaffeLatte(ItemCategory itemCategory) {
		String name = "카페 라떼";
		String description = "풍부하고 진한 에스프레소가 신선한 스팀 밀크를 만나 부드러워진 커피 위에 우유 거품을 살짝 얹은 대표적인 커피 라떼";
		String englishName = "Iced Caffe Americano";
		Integer price = 5000;
		DefaultOption defaultOption = createDefaultOption(1, 0, 0, 0,
			BeverageOption.Milk.NORMAL, BeverageOption.Coffee.ESPRESSO,
			BeverageOption.MilkAmount.MEDIUM);

		String image = "https://hkbks.com/api/image.jpg";

		return com.prgrms.bdbks.domain.item.entity.Item.builder()
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

		return com.prgrms.bdbks.domain.item.entity.Item.builder()
			.name(name)
			.category(category)
			.englishName(englishName)
			.price(price)
			.image(image)
			.description(description)
			.build();
	}

	public static Item createItem(String name, ItemCategory category, String englishName, int price, String image,
		String description, DefaultOption defaultOption) {

		return com.prgrms.bdbks.domain.item.entity.Item.builder()
			.name(name)
			.category(category)
			.englishName(englishName)
			.price(price)
			.image(image)
			.description(description)
			.defaultOption(defaultOption)
			.build();
	}

	public static List<Item> createItems(List<String> names, List<String> englishNames, List<Integer> prices,
		List<String> images, ItemCategory category, DefaultOption defaultOption) {
		return IntStream.range(0, names.size())
			.mapToObj(
				value -> com.prgrms.bdbks.domain.item.entity.Item.builder()
					.name(names.get(value))
					.category(category)
					.englishName(englishNames.get(value))
					.price(prices.get(value))
					.image(images.get(value))
					.description("description")
					.defaultOption(defaultOption)
					.build()
			).collect(Collectors.toList());
	}

	public static ItemCategory createItemCategory(String name, String englishName, ItemType itemType) {
		return new ItemCategory(name, englishName, itemType);
	}

	public static List<ItemCategory> createItemCategoriesByType(int size, ItemType itemType, List<String> names,
		List<String> englishNames) {

		return IntStream.range(0, size)
			.mapToObj(value -> createItemCategory(names.get(value), englishNames.get(value), itemType))
			.collect(Collectors.toList());
	}

	public static DefaultOption createDefaultOption(Integer espressoShotCount, Integer vanillaSyrupCount,
		Integer classicSyrupCount, Integer hazelnutSyrupCount, BeverageOption.Milk milkType,
		BeverageOption.Coffee espressoType, BeverageOption.MilkAmount milkAmount) {

		return DefaultOption.builder()
			.espressoShotCount(espressoShotCount)
			.hazelnutSyrupCount(hazelnutSyrupCount)
			.classicSyrupCount(classicSyrupCount)
			.vanillaSyrupCount(vanillaSyrupCount)
			.milkAmount(milkAmount)
			.espressoType(espressoType)
			.milkType(milkType)
			.build();
	}

	public static CustomOption createCustomOption(OrderCreateRequest.Item.Option option) {
		return CustomOption.builder()
			.espressoType(option.getEspressoType())
			.espressoShotCount(option.getEspressoShotCount())
			.vanillaSyrupCount(option.getVanillaSyrupCount())
			.classicSyrupCount(option.getClassicSyrupCount())
			.hazelnutSyrupCount(option.getHazelnutSyrupCount())
			.cupSize(option.getCupSize())
			.cupType(option.getCupType())
			.build();
	}

}