package com.prgrms.bdbks.domain.order.entity;

import static com.prgrms.bdbks.domain.testutil.ItemObjectProvider.*;
import static com.prgrms.bdbks.domain.testutil.OrderObjectProvider.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import com.prgrms.bdbks.domain.item.entity.BeverageOption;
import com.prgrms.bdbks.domain.item.entity.DefaultOption;
import com.prgrms.bdbks.domain.item.entity.Item;
import com.prgrms.bdbks.domain.item.entity.ItemCategory;
import com.prgrms.bdbks.domain.item.entity.OptionPrice;

@DisplayName("OrderItem 테스트")
class OrderItemTest {

	private final Integer quantity = 3;

	private final Item item = createIcedAmericano();

	private final Order order = createOrder();

	private final CustomOption customOption = createCustomOption();

	private final OptionPrice optionPrice = new OptionPrice();

	@DisplayName("생성 - OrderItem() - 생성에 성공한다.")
	@Test
	void create_success() {
		//given
		Long userId = 1L;
		String storeId = "storeId";
		Order order = Order.create(null, userId, storeId);

		//when&then
		assertDoesNotThrow(() -> {
			OrderItem.create(order, item, customOption, 1, optionPrice);
		});
	}

	@DisplayName("생성 - OrderItem() - quantity가 0개 이하면 생성에 실패한다.")
	@ParameterizedTest
	@ValueSource(ints = {-1, -2, -3, -4, -99, 0})
	void create_quantityLessThen0_fail(int quantity) {
		//given & when & then
		assertThrows(IllegalArgumentException.class, () ->
			OrderItem.create(order, item, customOption, quantity, optionPrice));
	}

	@DisplayName("생성 - OrderItem() - order가 null 이면 생성에 실패한다.")
	@Test
	void create_OrderNull_fail() {
		//given & when & then
		assertThrows(NullPointerException.class, () -> {
			OrderItem.create(null, item, customOption, quantity, optionPrice);
		});
	}

	@DisplayName("생성 - create() - item 이 null 이면 생성에 실패한다.")
	@Test
	void create_itemNull_fail() {
		//given & when & then
		assertThrows(NullPointerException.class, () ->
			OrderItem.create(order, null, customOption, quantity, optionPrice));
	}

	@DisplayName("생성 - create() - customOption 이 null 이면 생성에 실패한다.")
	@Test
	void create_customOptionNull_fail() {
		//given & when & then
		assertThrows(NullPointerException.class, () ->
			OrderItem.create(order, item, null, quantity, optionPrice));
	}

	@DisplayName("생성 - create() - order와 연관관계가 맺어진다")
	@Test
	void create_RelationshipWithOrder() {
		//given & when
		OrderItem orderItem = OrderItem.create(order, item, customOption, 1, optionPrice);

		//then
		assertEquals(order, orderItem.getOrder());
		assertTrue(order.getOrderItems().contains(orderItem));
	}

	@DisplayName("생성 - create() - customOption 추가 가격을 상품 가격에 더하여 price에 정상 저장한다.")
	@ParameterizedTest
	@CsvSource(value = {
		" , 3, 3, 3, 3, 4, 4, 4, 500, 500, 1500", // espressoShotCount is null
		" , 4, 4, 4, 4, 4, 4, 4, 1000, 100, 0", // espressoShotCount is null
		" , 3, 3, 3, 3, 0, 0, 0, 1000, 100, 0", // espressoShotCount is null
		" , 9, 9, 9, 9, 0, 0, 0, 0, 0, 0", // espressoShotCount is null

		"0, , 0, 0, 0, 0, 8, 8, 10000, 10000, 160000", // vanillaSyrupCount is null
		"5, , 5, 5, 5, 5, 4, 4, 10000, 10000, 0", // vanillaSyrupCount is null
		"0, , 2, 3, 5, 1, 4, 4, 600, 600, 4800", // vanillaSyrupCount is null
		"5, , 5, 5, 9, 5, 9, 9, 600, 600, 7200", // vanillaSyrupCount is null
		"5, , 5, 5, 4, 5, 3, 2, 600, 600, 0", // vanillaSyrupCount is null

		"2, 3, , 5, 2, 2, 2, 2, 200, 200, 0", // classicSyrupCount is null
		"2, 3, , 5, 4, 5, , 2, 400, 400, 1600", // classicSyrupCount is null
		"2, 3, , 5, 3, 8, , 7, 8800, 800, 14400", // classicSyrupCount is null

		"0, 3, 2, , 5, 1, 4, 4, 600, 600, 4200", // hazelnutSyrupCount is null
		"9, 9, 9, , 9, 0, 0, 0, 0, 0, 0", // hazelnutSyrupCount is null
		"0, 0, 0, , 3, 3, 3, 3, 1000, 1000, 9000", // hazelnutSyrupCount is null
		"3, 3, 3, , 2, 2, 2, 2, 600, 600, 0", // hazelnutSyrupCount is null
	}, delimiterString = ",")
	void create_totalPrice(
		Integer defaultEspressoShotCount,
		Integer defaultVanillaSyrupCount,
		Integer defaultClassicSyrupCount,
		Integer defaultHazelnutSyrupCount,
		Integer espressoShotCount, Integer vanillaSyrupCount, Integer classicSyrupCount, Integer hazelnutSyrupCount,
		int espressoPrice, int syrupPrice, Integer expectedCost) {

		BeverageOption.Milk milkType = BeverageOption.Milk.OAT;

		BeverageOption.Coffee espressoType = BeverageOption.Coffee.DECAFFEINATED;

		BeverageOption.MilkAmount milkAmount = BeverageOption.MilkAmount.MEDIUM;

		BeverageOption.Size cupSize = BeverageOption.Size.VENTI;

		BeverageOption.CupType cupType = BeverageOption.CupType.DISPOSABLE;

		OptionPrice optionPrice = new OptionPrice(espressoPrice, syrupPrice);

		ItemCategory itemCategory = createReserveEspressoCategory();
		String name = "아이스 아메리카노";
		String description = "진한 에스프레소에 시원한 정수물과 얼음을 더하여 스타벅스의 깔끔하고 강렬한 에스프레소를 가장 부드럽고 시원하게 즐길 수 있는 커피";
		String englishName = "Iced Caffe Americano";
		Integer itemPrice = 4500;

		DefaultOption defaultOption = createDefaultOption(
			defaultEspressoShotCount, defaultVanillaSyrupCount, defaultClassicSyrupCount, defaultHazelnutSyrupCount,
			BeverageOption.Milk.OAT, BeverageOption.Coffee.DECAFFEINATED, BeverageOption.MilkAmount.MEDIUM);

		String image = "https://hkbks.com/api/image.jpg";

		Item createItem = Item.builder()
			.description(description)
			.name(name)
			.category(itemCategory)
			.englishName(englishName)
			.price(itemPrice)
			.image(image)
			.defaultOption(defaultOption)
			.build();

		CustomOption customOption = CustomOption.builder()
			.espressoType(espressoType)
			.espressoShotCount(espressoShotCount)
			.vanillaSyrupCount(vanillaSyrupCount)
			.hazelnutSyrupCount(hazelnutSyrupCount)
			.classicSyrupCount(classicSyrupCount)
			.milkType(milkType)
			.milkAmount(milkAmount)
			.cupType(cupType)
			.cupSize(cupSize)
			.build();

		//when
		OrderItem orderItem = OrderItem.create(order, createItem, customOption, quantity, optionPrice);

		//then
		assertEquals(expectedCost + itemPrice, orderItem.getPrice());
	}

	@DisplayName("getTotalprice() - OrderItem의 가격과 수량을 곱한 P값을 반환한다.")
	@Test
	void getTotalPrice_MultiplyPriceByQuantity() {
		//given
		Long userId = 1L;
		String storeId = "storeId";

		Order order = Order.create(null, userId, storeId);

		//when
		int quantity = 3;
		OrderItem orderItem = OrderItem.create(order, item, customOption, quantity, optionPrice);

		//then
		assertEquals(orderItem.getPrice() * orderItem.getQuantity(), orderItem.getTotalPrice());
	}

}