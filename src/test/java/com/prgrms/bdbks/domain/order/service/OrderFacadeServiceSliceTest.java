package com.prgrms.bdbks.domain.order.service;

import static com.prgrms.bdbks.domain.coupon.entity.Coupon.*;
import static com.prgrms.bdbks.domain.item.entity.BeverageOption.Coffee.*;
import static com.prgrms.bdbks.domain.item.entity.BeverageOption.CupType.*;
import static com.prgrms.bdbks.domain.item.entity.BeverageOption.Size.*;
import static com.prgrms.bdbks.domain.order.entity.OrderItem.*;
import static com.prgrms.bdbks.domain.testutil.ItemObjectProvider.createCustomOption;
import static com.prgrms.bdbks.domain.testutil.ItemObjectProvider.*;
import static com.prgrms.bdbks.domain.testutil.OrderObjectProvider.createCustomOption;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.prgrms.bdbks.common.exception.EntityNotFoundException;
import com.prgrms.bdbks.common.exception.PaymentException;
import com.prgrms.bdbks.domain.coupon.entity.Coupon;
import com.prgrms.bdbks.domain.coupon.service.CouponService;
import com.prgrms.bdbks.domain.item.dto.CustomItem;
import com.prgrms.bdbks.domain.item.entity.Item;
import com.prgrms.bdbks.domain.item.entity.OptionPrice;
import com.prgrms.bdbks.domain.item.service.ItemService;
import com.prgrms.bdbks.domain.order.converter.OrderMapper;
import com.prgrms.bdbks.domain.order.dto.OrderCreateRequest;
import com.prgrms.bdbks.domain.order.dto.OrderCreateRequest.PaymentOption;
import com.prgrms.bdbks.domain.order.dto.OrderDetailResponse;
import com.prgrms.bdbks.domain.order.entity.CustomOption;
import com.prgrms.bdbks.domain.order.entity.Order;
import com.prgrms.bdbks.domain.order.entity.OrderItem;
import com.prgrms.bdbks.domain.payment.dto.OrderPayment;
import com.prgrms.bdbks.domain.payment.entity.PaymentType;
import com.prgrms.bdbks.domain.payment.model.PaymentResult;
import com.prgrms.bdbks.domain.payment.service.PaymentFacadeService;
import com.prgrms.bdbks.domain.star.service.StarService;
import com.prgrms.bdbks.domain.store.entity.Store;
import com.prgrms.bdbks.domain.store.service.StoreService;
import com.prgrms.bdbks.domain.testutil.OrderObjectProvider;
import com.prgrms.bdbks.domain.testutil.StoreObjectProvider;
import com.prgrms.bdbks.domain.testutil.UserObjectProvider;
import com.prgrms.bdbks.domain.user.entity.User;
import com.prgrms.bdbks.domain.user.service.UserService;

import lombok.SneakyThrows;

@ExtendWith(MockitoExtension.class)
class OrderFacadeServiceSliceTest {

	@InjectMocks
	private OrderFacadeService orderFacadeService;

	@Mock
	private OrderService orderService;

	@Mock
	private StoreService storeService;

	@Mock
	private UserService userService;

	@Mock
	private ItemService itemService;

	@Mock
	private OptionPrice optionPrice;

	@Mock
	private CouponService couponService;

	@Mock
	private StarService starService;

	@Mock
	private PaymentFacadeService paymentFacadeService;

	@Spy
	private OrderMapper orderMapper = Mappers.getMapper(OrderMapper.class);

	private final CustomOption customOption = createCustomOption();

	@SneakyThrows
	@DisplayName("findOrderById() - orderId 로 로 주문 정보를 조회할 수 있다. - 성공")
	@Test
	void findOrderById_success() {
		//given
		String orderId = "20230117BKDKS1234";
		String storeId = "storeId";
		Order order = OrderObjectProvider.createOrder();
		ReflectionTestUtils.setField(order, "id", orderId);

		Item icedAmericano = createIcedAmericano();
		create(order, icedAmericano, customOption, 2,
			optionPrice);

		Item caffeLatte = createCaffeLatte();

		create(order, caffeLatte, customOption, 3,
			optionPrice);

		Store store = StoreObjectProvider.creatStore(storeId);

		User user = UserObjectProvider.createUser(0L);
		String userNickname = "yeoungji";
		ReflectionTestUtils.setField(user, "nickname", userNickname);

		given(orderService.findByIdWithOrderItemsAndCustomOption(orderId))
			.willReturn(order);

		given(storeService.findById(order.getStoreId())).willReturn(store);
		given(userService.findUserById(order.getUserId())).willReturn(user);

		//when
		OrderDetailResponse orderDetailResponse = orderFacadeService.findOrderById(orderId);

		//then
		assertThat(orderDetailResponse)
			.hasFieldOrPropertyWithValue("orderId", orderId)
			.hasFieldOrPropertyWithValue("orderCount", order.getOrderItems().size())
			.hasFieldOrPropertyWithValue("orderStatus", order.getOrderStatus().getKorStatus())
			.hasFieldOrPropertyWithValue("storeName", store.getName())
			.hasFieldOrPropertyWithValue("nickname", userNickname);

		assertThat(orderDetailResponse.getOrderItems())
			.hasSize(2);

		assertThat(orderDetailResponse.getOrderItems())

			.extracting("itemName").contains(icedAmericano.getName(), caffeLatte.getName());

		assertThat(orderDetailResponse.getOrderItems()).extracting("itemName", "cupSize", "cupType", "image")
			.contains(
				tuple(icedAmericano.getName(), customOption.getCupSize().name(), customOption.getCupType().getKorName(),
					icedAmericano.getImage()),
				tuple(caffeLatte.getName(), customOption.getCupSize().name(), customOption.getCupType().getKorName(),
					caffeLatte.getImage())
			);

		verify(orderService).findByIdWithOrderItemsAndCustomOption(orderId);
		verify(storeService).findById(order.getStoreId());
		verify(userService).findUserById(order.getUserId());

	}

	@DisplayName("findOrderById() - orderId가 없는 경우 주문 정보를 조회에 실패한다. - 실패")
	@Test
	void findOrderById_fail() {
		//given
		String orderId = "20230117BKDKS1234";
		given(orderService.findByIdWithOrderItemsAndCustomOption(orderId))
			.willThrow(EntityNotFoundException.class);

		//when
		assertThrows(EntityNotFoundException.class, () ->
			orderFacadeService.findOrderById(orderId));

		//then
		verify(orderService, atLeast(1)).findByIdWithOrderItemsAndCustomOption(orderId);
	}

	@DisplayName("주문 생성 - 없는 쿠폰 사용시 주문에 실패한다 - 실패")
	@Test
	void createOrder_couponId_fail() {
		//given
		String orderId = "20230117BKDKS1234";
		String storeId = "storeId";
		Order order = OrderObjectProvider.createOrder();
		ReflectionTestUtils.setField(order, "id", orderId);

		Item icedAmericano = createIcedAmericano();
		OrderItem.create(order, icedAmericano, customOption, 2, optionPrice);

		Item caffeLatte = createCaffeLatte();
		OrderItem.create(order, caffeLatte, customOption, 3, optionPrice);

		Store store = StoreObjectProvider.creatStore(storeId);

		User user = UserObjectProvider.createUser(0L);
		String userNickname = "yeoungji";
		ReflectionTestUtils.setField(user, "nickname", userNickname);

		Long couponId = 1L;

		Long userId = 1L;

		String chargeCardId = UUID.randomUUID().toString();

		OrderCreateRequest.Item.Option option = new OrderCreateRequest.Item.Option(
			1, 0, 0, 0,
			null, ESPRESSO, null, VENTI, PERSONAL);

		List<OrderCreateRequest.Item> orderItemRequests = List.of(
			new OrderCreateRequest.Item(1L, 1, 4500, option));

		PaymentOption paymentOption = new PaymentOption(PaymentType.ORDER,
			couponId, chargeCardId);

		OrderCreateRequest request = new OrderCreateRequest(userId, storeId, orderItemRequests, paymentOption);

		//when
		given(storeService.findById(storeId))
			.willReturn(store);
		given(userService.findUserById(userId))
			.willReturn(user);

		given(couponService.getCouponByCouponId(couponId))
			.willThrow(EntityNotFoundException.class);

		assertThrows(EntityNotFoundException.class, () ->
			orderFacadeService.createOrder(request));

		//then
		verify(storeService).findById(request.getStoreId());
		verify(userService).findUserById(request.getUserId());
		verify(couponService).getCouponByCouponId(couponId);
	}

	@DisplayName("주문 생성 - 결제 예외 발생시 주문 실패한다")
	@Test
	void createOrder_paymentFail_fail() {
		//given
		long userId = 23423L;
		User user = UserObjectProvider.createUser(userId);
		String storeId = "storeId";
		Store store = StoreObjectProvider.creatStore(storeId);
		String chargeCardId = "chargeCardId";

		Order order = OrderObjectProvider.createOrder();
		ReflectionTestUtils.setField(order, "id", "20230117BKDKS1234");
		OrderCreateRequest.Item.Option option = new OrderCreateRequest.Item.Option(
			1, 0, 0, 0,
			null, ESPRESSO, null, VENTI, PERSONAL);

		List<OrderCreateRequest.Item> orderItemRequests = List.of(new OrderCreateRequest.Item(1L, 1, 4500, option));

		PaymentOption paymentOption = new PaymentOption(PaymentType.ORDER, null,
			chargeCardId);

		OrderCreateRequest request = new OrderCreateRequest(userId, storeId, orderItemRequests, paymentOption);

		OrderPayment orderPayment = new OrderPayment(order, request.getPaymentOption().getChargeCardId(),
			request.getPaymentOption().getPaymentType());
		Item icedAmericano = createIcedAmericano();

		CustomOption customOption = createCustomOption(option);

		List<CustomItem> customItems = List.of(new CustomItem(icedAmericano, customOption, 1));

		given(storeService.findById(request.getStoreId()))
			.willReturn(store);
		given(userService.findUserById(request.getUserId()))
			.willReturn(user);
		given(itemService.customItems(request.getOrderItems()))
			.willReturn(customItems);
		given(orderService.createOrder(null, userId, storeId, customItems))
			.willReturn(order);
		given(paymentFacadeService.orderPay(orderPayment))
			.willThrow(PaymentException.class);

		//when
		assertThrows(PaymentException.class, () ->
			orderFacadeService.createOrder(request));

		//then
		verify(storeService).findById(request.getStoreId());
		verify(userService).findUserById(request.getUserId());
		verify(paymentFacadeService).orderPay(orderPayment);
		verify(itemService).customItems(request.getOrderItems());
		verify(orderService).createOrder(null, userId, storeId, customItems);
	}

	@DisplayName("주문 생성 - 존재하지 않는 store일 경우 주문에 실패한다. - 실패")
	@Test
	void createOrder_storeId_fail() {
		//given
		long userId = 23423L;
		String storeId = "storeId";
		String chargeCardId = "chargeCardId";

		OrderCreateRequest.Item.Option option = new OrderCreateRequest.Item.Option(
			1, 0, 0, 0,
			null, ESPRESSO, null, VENTI, PERSONAL);

		List<OrderCreateRequest.Item> orderItemRequests = List.of(new OrderCreateRequest.Item(1L, 1, 4500, option));

		PaymentOption paymentOption = new PaymentOption(PaymentType.ORDER, null, chargeCardId);
		OrderCreateRequest request = new OrderCreateRequest(userId, storeId, orderItemRequests, paymentOption);

		given(storeService.findById(request.getStoreId()))
			.willThrow(EntityNotFoundException.class);

		//when
		assertThrows(EntityNotFoundException.class, () ->
			orderFacadeService.createOrder(request));

		//then
		verify(storeService).findById(request.getStoreId());
	}

	@DisplayName("주문 생성 - 존재하지 않는 user의 경우 주문에 실패한다. - 실패")
	@Test
	void createOrder_userId_fail() {
		//given
		long userId = 23423L;
		String storeId = "storeId";
		Store store = StoreObjectProvider.creatStore(storeId);

		String chargeCardId = "chargeCardId";

		OrderCreateRequest.Item.Option option = new OrderCreateRequest.Item.Option(
			1, 0, 0, 0,
			null, ESPRESSO, null, VENTI, PERSONAL);

		List<OrderCreateRequest.Item> orderItemRequests = List.of(
			new OrderCreateRequest.Item(1L, 1, 4500, option));

		PaymentOption paymentOption = new PaymentOption(PaymentType.ORDER, null, chargeCardId);

		OrderCreateRequest request = new OrderCreateRequest(userId, storeId, orderItemRequests, paymentOption);

		given(storeService.findById(request.getStoreId())).willReturn(store);
		given(userService.findUserById(request.getUserId())).willThrow(EntityNotFoundException.class);

		//when
		assertThrows(EntityNotFoundException.class, () ->
			orderFacadeService.createOrder(request));

		//then
		verify(storeService).findById(request.getStoreId());
	}

	@DisplayName("주문 생성 - 주문을 정상 생성한다 - 성공")
	@Test
	void createOrder_success() {
		//given
		long userId = 23423L;
		User user = UserObjectProvider.createUser(userId);

		String storeId = "storeId";
		Store store = StoreObjectProvider.creatStore(storeId);

		String chargeCardId = "chargeCardId";
		String orderId = "20230117BKDKS1234";
		Order order = OrderObjectProvider.createOrder();
		ReflectionTestUtils.setField(order, "id", orderId);

		Long couponId = 1L;
		Coupon coupon = createCoupon(userId);
		ReflectionTestUtils.setField(coupon, "id", couponId);

		OrderCreateRequest.Item.Option option = new OrderCreateRequest.Item.Option(
			1, 0, 0, 0,
			null, ESPRESSO, null, VENTI, PERSONAL);

		List<OrderCreateRequest.Item> orderItemRequests = List.of(
			new OrderCreateRequest.Item(1L, 1, 4500,
				option));
		PaymentOption paymentOption = new PaymentOption(PaymentType.ORDER,
			couponId, chargeCardId);

		OrderCreateRequest request = new OrderCreateRequest(userId, storeId, orderItemRequests, paymentOption);

		OrderPayment orderPayment = new OrderPayment(order, request.getPaymentOption().getChargeCardId(),
			request.getPaymentOption().getPaymentType());

		PaymentResult paymentResult = new PaymentResult("paymentID");

		Item icedAmericano = createIcedAmericano();

		CustomOption customOption = createCustomOption(option);

		List<CustomItem> customItems = List.of(new CustomItem(icedAmericano, customOption, 1));

		given(itemService.customItems(request.getOrderItems()))
			.willReturn(customItems);
		given(storeService.findById(request.getStoreId()))
			.willReturn(store);
		given(orderService.createOrder(coupon, userId, storeId, customItems))
			.willReturn(order);
		given(userService.findUserById(request.getUserId()))
			.willReturn(user);
		given(couponService.getCouponByCouponId(couponId))
			.willReturn(coupon);
		given(paymentFacadeService.orderPay(orderPayment))
			.willReturn(paymentResult);

		doNothing().when(starService).updateCount(userId, order.getTotalQuantity());

		//when
		String createOrderId = orderFacadeService.createOrder(request);

		//then
		assertThat(createOrderId).isEqualTo(orderId);

		verify(storeService).findById(request.getStoreId());
		verify(userService).findUserById(request.getUserId());
		verify(couponService).getCouponByCouponId(couponId);
		verify(paymentFacadeService).orderPay(orderPayment);
		verify(itemService).customItems(request.getOrderItems());
		verify(orderService).createOrder(coupon, userId, storeId, customItems);
		verify(starService).updateCount(userId, order.getTotalQuantity());
	}

	@DisplayName("주문 생성 - 쿠폰이 없는 주문을 정상 생성한다 - 성공")
	@Test
	void createOrder_couponNull_success() {
		//given
		long userId = 23423L;
		User user = UserObjectProvider.createUser(userId);
		String storeId = "storeId";
		Store store = StoreObjectProvider.creatStore(storeId);
		String chargeCardId = "chargeCardId";
		String orderId = "20230117BKDKS1234";
		Order order = OrderObjectProvider.createOrder();
		ReflectionTestUtils.setField(order, "id", orderId);

		OrderCreateRequest.Item.Option option = new OrderCreateRequest.Item.Option(
			1, 0, 0, 0,
			null, ESPRESSO, null, VENTI, PERSONAL);

		List<OrderCreateRequest.Item> orderItemRequests = List.of(new OrderCreateRequest.Item(1L, 1, 4500, option));

		PaymentOption paymentOption = new PaymentOption(PaymentType.ORDER,
			null, chargeCardId);
		OrderCreateRequest request = new OrderCreateRequest(userId, storeId, orderItemRequests, paymentOption);

		OrderPayment orderPayment = new OrderPayment(order, request.getPaymentOption().getChargeCardId(),
			request.getPaymentOption().getPaymentType());

		PaymentResult paymentResult = new PaymentResult("paymentID");

		Item icedAmericano = createIcedAmericano();

		CustomOption customOption = createCustomOption(option);

		List<CustomItem> customItems = List.of(new CustomItem(icedAmericano, customOption, 1));

		given(itemService.customItems(request.getOrderItems()))
			.willReturn(customItems);
		given(storeService.findById(request.getStoreId()))
			.willReturn(store);
		given(orderService.createOrder(null, userId, storeId, customItems))
			.willReturn(order);
		given(userService.findUserById(request.getUserId()))
			.willReturn(user);
		given(paymentFacadeService.orderPay(orderPayment))
			.willReturn(paymentResult);

		//when
		String createOrderId = orderFacadeService.createOrder(request);

		//then
		assertThat(createOrderId).isEqualTo(orderId);

		verify(storeService).findById(request.getStoreId());
		verify(userService).findUserById(request.getUserId());
		verify(paymentFacadeService).orderPay(orderPayment);
		verify(itemService).customItems(request.getOrderItems());
		verify(orderService).createOrder(null, userId, storeId, customItems);
	}

}