package com.prgrms.bdbks.domain.item.entity;

import static com.prgrms.bdbks.domain.testutil.ItemObjectProvider.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Item 테스트")
class ItemTest {

	@DisplayName("생성 - Item() - 생성에 성공한다.")
	@Test
	void constructor_create_success() {
		//given
		ItemCategory itemCategory = createReserveEspressoCategory();

		String name = "아이스 아메리카노";
		String description = "진한 에스프레소에 시원한 정수물과 얼음을 더하여 스타벅스의 깔끔하고 강렬한 에스프레소를 가장 부드럽고 시원하게 즐길 수 있는 커피";
		String englishName = "Iced Caffe Americano";
		int price = 4500;
		String image = "https://hkbks.com/api/image.jpg";
		DefaultOption defaultOption = createDefaultOption(1, 0, 0, 0,
			BeverageOption.Milk.OAT, BeverageOption.Coffee.DECAFFEINATED,
			BeverageOption.MilkAmount.MEDIUM);

		//when & then
		assertDoesNotThrow(() -> {
			Item.builder()
				.name(name)
				.category(itemCategory)
				.englishName(englishName)
				.price(price)
				.image(image)
				.description(description)
				.defaultOption(defaultOption)
				.build();
		});
	}

	@DisplayName("생성 - Item() - 이름이 비어있으면 생성에 실패한다.")
	@Test
	void constructor_nameEmpty_fail() {
		//given
		ItemCategory itemCategory = createReserveEspressoCategory();

		String name = "";
		String description = "진한 에스프레소에 시원한 정수물과 얼음을 더하여 스타벅스의 깔끔하고 강렬한 에스프레소를 가장 부드럽고 시원하게 즐길 수 있는 커피";
		String englishName = "Iced Caffe Americano";
		int price = 4500;
		String image = "https://hkbks.com/api/image.jpg";
		DefaultOption defaultOption = createDefaultOption(1, 0, 0, 0,
			BeverageOption.Milk.OAT, BeverageOption.Coffee.DECAFFEINATED,
			BeverageOption.MilkAmount.MEDIUM);

		//when & then
		assertThrows(IllegalArgumentException.class, () -> {
			Item.builder()
				.name(name)
				.category(itemCategory)
				.englishName(englishName)
				.price(price)
				.image(image)
				.description(description)
				.defaultOption(defaultOption)
				.build();
		});
	}

	@DisplayName("생성 - Item() - 한글명 글자 수가 30자 초과하면 생성에 실패한다.")
	@Test
	void constructor_createWithNameLengthOver30_fail() {
		//given
		ItemCategory itemCategory = createReserveEspressoCategory();

		String name = "세상에서 가장 비싸고 맛있는 커피. 리저브 에스프레소. 엄청 긴 커피.12345678910 이 커피 가격은 999,999,9999,99999원";
		String description = "진한 에스프레소에 시원한 정수물과 얼음을 더하여 스타벅스의 깔끔하고 강렬한 에스프레소를 가장 부드럽고 시원하게 즐길 수 있는 커피";
		String englishName = "Iced Caffe Americano";
		int price = 4500;
		String image = "https://hkbks.com/api/image.jpg";
		DefaultOption defaultOption = createDefaultOption(1, 0, 0, 0,
			BeverageOption.Milk.OAT, BeverageOption.Coffee.DECAFFEINATED,
			BeverageOption.MilkAmount.MEDIUM);

		//when & then
		assertThrows(IllegalArgumentException.class, () -> {
			Item.builder()
				.name(name)
				.category(itemCategory)
				.englishName(englishName)
				.price(price)
				.image(image)
				.description(description)
				.defaultOption(defaultOption)
				.build();
		});
	}

	@DisplayName("생성 - Item() - 카테고리가 Null 이면 생성에 실패한다.")
	@Test
	void constructor_createCategoryEmpty_fail() {
		//given

		String name = "아이스 아메리카노";
		String description = "진한 에스프레소에 시원한 정수물과 얼음을 더하여 스타벅스의 깔끔하고 강렬한 에스프레소를 가장 부드럽고 시원하게 즐길 수 있는 커피";
		String englishName = "Iced Caffe Americano";
		int price = 4500;
		String image = "https://hkbks.com/api/image.jpg";
		DefaultOption defaultOption = createDefaultOption(1, 0, 0, 0,
			BeverageOption.Milk.OAT, BeverageOption.Coffee.DECAFFEINATED,
			BeverageOption.MilkAmount.MEDIUM);

		//when & then
		assertThrows(NullPointerException.class, () -> {
			Item.builder()
				.name(name)
				.englishName(englishName)
				.price(price)
				.image(image)
				.description(description)
				.defaultOption(defaultOption)
				.build();
		});
	}

	@DisplayName("생성 - Item() - englishName이 Empty 이면 생성에 실패한다.")
	@Test
	void constructor_createEnglishNameEmpty_fail() {
		//given
		ItemCategory itemCategory = createReserveEspressoCategory();

		String name = "아이스 아메리카노";
		String description = "진한 에스프레소에 시원한 정수물과 얼음을 더하여 스타벅스의 깔끔하고 강렬한 에스프레소를 가장 부드럽고 시원하게 즐길 수 있는 커피";
		String englishName = null;
		int price = 4500;
		String image = "https://hkbks.com/api/image.jpg";
		DefaultOption defaultOption = createDefaultOption(1, 0, 0, 0,
			BeverageOption.Milk.OAT, BeverageOption.Coffee.DECAFFEINATED,
			BeverageOption.MilkAmount.MEDIUM);

		//when & then
		assertThrows(IllegalArgumentException.class, () -> {
			Item.builder()
				.name(name)
				.category(itemCategory)
				.englishName(englishName)
				.price(price)
				.image(image)
				.description(description)
				.defaultOption(defaultOption)
				.build();
		});
	}

	@DisplayName("생성 - Item() - price 가 0보다 작으면 생성에 실패한다.")
	@Test
	void constructor_createPriceLessThen0_fail() {
		//given
		ItemCategory itemCategory = createReserveEspressoCategory();

		String name = "아이스 아메리카노";
		String description = "진한 에스프레소에 시원한 정수물과 얼음을 더하여 스타벅스의 깔끔하고 강렬한 에스프레소를 가장 부드럽고 시원하게 즐길 수 있는 커피";
		String englishName = "Iced Caffe Americano";
		int price = -1;
		String image = "https://hkbks.com/api/image.jpg";
		DefaultOption defaultOption = createDefaultOption(1, 0, 0, 0,
			BeverageOption.Milk.OAT, BeverageOption.Coffee.DECAFFEINATED,
			BeverageOption.MilkAmount.MEDIUM);

		//when & then
		assertThrows(IllegalArgumentException.class, () -> {
			Item.builder()
				.name(name)
				.category(itemCategory)
				.englishName(englishName)
				.price(price)
				.image(image)
				.description(description)
				.defaultOption(defaultOption)
				.build();
		});
	}

	@DisplayName("생성 - Item() - description 이 empty 면 생성에 실패한다.")
	@Test
	void constructor_createDescriptionEmpty_fail() {
		//given
		ItemCategory itemCategory = createReserveEspressoCategory();

		String name = "아이스 아메리카노";
		String description = null;
		String englishName = "Iced Caffe Americano";
		int price = 4500;
		String image = "https://hkbks.com/api/image.jpg";
		DefaultOption defaultOption = createDefaultOption(1, 0, 0, 0,
			BeverageOption.Milk.OAT, BeverageOption.Coffee.DECAFFEINATED,
			BeverageOption.MilkAmount.MEDIUM);

		//when & then
		assertThrows(IllegalArgumentException.class, () -> {
			Item.builder()
				.name(name)
				.category(itemCategory)
				.englishName(englishName)
				.price(price)
				.image(image)
				.description(description)
				.defaultOption(defaultOption)
				.build();
		});
	}

	@DisplayName("생성 - Item() - image 이 empty 거나 url이  아니라면 면 생성에 실패한다.")
	@Test
	void constructor_createImageEmpty_fail() {
		//given
		ItemCategory itemCategory = createReserveEspressoCategory();

		String name = "아이스 아메리카노";
		String description = "진한 에스프레소에 시원한 정수물과 얼음을 더하여 스타벅스의 깔끔하고 강렬한 에스프레소를 가장 부드럽고 시원하게 즐길 수 있는 커피";
		String englishName = "Iced Caffe Americano";
		int price = 4500;
		String image = "";
		DefaultOption defaultOption = createDefaultOption(1, 0, 0, 0,
			BeverageOption.Milk.OAT, BeverageOption.Coffee.DECAFFEINATED,
			BeverageOption.MilkAmount.MEDIUM);

		//when & then
		assertThrows(IllegalArgumentException.class, () -> {
			Item.builder()
				.name(name)
				.category(itemCategory)
				.englishName(englishName)
				.price(price)
				.image(image)
				.description(description)
				.defaultOption(defaultOption)
				.build();
		});
	}

	@DisplayName("생성 - Item() - defaultOption이 Null 이면 생성에 실패한다.")
	@Test
	void constructor_createDefaultOptionNull_fail() {
		//given
		ItemCategory itemCategory = createReserveEspressoCategory();

		String name = "아이스 아메리카노";
		String description = "진한 에스프레소에 시원한 정수물과 얼음을 더하여 스타벅스의 깔끔하고 강렬한 에스프레소를 가장 부드럽고 시원하게 즐길 수 있는 커피";
		String englishName = "Iced Caffe Americano";
		int price = 4500;
		String image = "https://hkbks.com/api/image.jpg";

		//when & then
		assertThrows(NullPointerException.class, () -> {
			Item.builder()
				.name(name)
				.category(itemCategory)
				.englishName(englishName)
				.price(price)
				.image(image)
				.description(description)
				.build();
		});
	}

}