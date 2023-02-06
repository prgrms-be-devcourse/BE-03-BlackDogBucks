package com.prgrms.bdbks.domain.order.service;

import static com.prgrms.bdbks.domain.coupon.entity.Coupon.*;
import static com.prgrms.bdbks.domain.item.entity.BeverageOption.Coffee.*;
import static com.prgrms.bdbks.domain.item.entity.BeverageOption.CupType.*;
import static com.prgrms.bdbks.domain.item.entity.BeverageOption.Size.*;
import static com.prgrms.bdbks.domain.testutil.ItemObjectProvider.createCustomOption;
import static com.prgrms.bdbks.domain.testutil.ItemObjectProvider.*;
import static com.prgrms.bdbks.domain.testutil.OrderObjectProvider.*;
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

import com.prgrms.bdbks.common.exception.AuthorityNotFoundException;
import com.prgrms.bdbks.common.exception.EntityNotFoundException;
import com.prgrms.bdbks.common.exception.PaymentException;
import com.prgrms.bdbks.domain.card.dto.CardPayResponse;
import com.prgrms.bdbks.domain.card.service.CardService;
import com.prgrms.bdbks.domain.coupon.entity.Coupon;
import com.prgrms.bdbks.domain.coupon.service.CouponService;
import com.prgrms.bdbks.domain.item.dto.CustomItem;
import com.prgrms.bdbks.domain.item.entity.Item;
import com.prgrms.bdbks.domain.item.entity.OptionPrice;
import com.prgrms.bdbks.domain.item.service.ItemService;
import com.prgrms.bdbks.domain.order.converter.OrderMapper;
import com.prgrms.bdbks.domain.order.dto.OrderCreateRequest;
import com.prgrms.bdbks.domain.order.dto.OrderCreateRequest.PaymentOption;
import com.prgrms.bdbks.domain.order.dto.OrderCreateResponse;
import com.prgrms.bdbks.domain.order.dto.OrderDetailResponse;
import com.prgrms.bdbks.domain.order.entity.CustomOption;
import com.prgrms.bdbks.domain.order.entity.Order;
import com.prgrms.bdbks.domain.order.entity.OrderItem;
import com.prgrms.bdbks.domain.order.entity.OrderStatus;
import com.prgrms.bdbks.domain.order.exception.AlreadyProgressOrderException;
import com.prgrms.bdbks.domain.payment.entity.PaymentType;
import com.prgrms.bdbks.domain.payment.model.PaymentResult;
import com.prgrms.bdbks.domain.payment.service.PaymentService;
import com.prgrms.bdbks.domain.star.dto.StarExchangeResponse;
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
	private CardService cardService;

	@Mock
	private PaymentService paymentService;

	@Spy
	private OrderMapper orderMapper = Mappers.getMapper(OrderMapper.class);

	private final CustomOption customOption = createCustomOption(
		new OrderCreateRequest.Item.Option(0, 0, 0, 0, Milk.OAT, ESPRESSO,
			MilkAmount.MEDIUM, VENTI, PERSONAL)
	);

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
		OrderItem.create(order, icedAmericano, customOption, 2,
			optionPrice);

		Item caffeLatte = createCaffeLatte();

		OrderItem.create(order, caffeLatte, customOption, 3,
			optionPrice);

		Store store = StoreObjectProvider.creatStore(storeId);

		User user = UserObjectProvider.createUser(0L);
		String userNickname = "yeoungji";
		ReflectionTestUtils.setField(user, "nickname", userNickname);

		given(orderService.findByIdWithOrderItemsAndCustomOption(orderId))
			.willReturn(order);
		given(storeService.findById(order.getStoreId()))
			.willReturn(store);
		given(userService.findUserById(order.getUserId()))
			.willReturn(user);

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

		OrderCreateRequest request = new OrderCreateRequest(storeId, orderItemRequests, paymentOption);

		given(storeService.findById(storeId))
			.willReturn(store);
		given(userService.findUserById(userId))
			.willReturn(user);
		given(couponService.getCouponByCouponId(couponId))
			.willThrow(EntityNotFoundException.class);

		//when
		assertThrows(EntityNotFoundException.class, () ->
			orderFacadeService.createOrder(userId, request));

		//then
		verify(storeService).findById(request.getStoreId());
		verify(userService).findUserById(userId);
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

		OrderCreateRequest request = new OrderCreateRequest(storeId, orderItemRequests, paymentOption);

		CardPayResponse cardPayResponse = new CardPayResponse(request.getPaymentOption().getChargeCardId(),
			order.getTotalPrice());

		Item icedAmericano = createIcedAmericano();

		CustomOption customOption = createCustomOption(option);

		List<CustomItem> customItems = List.of(new CustomItem(icedAmericano, customOption, 1));

		given(storeService.findById(request.getStoreId()))
			.willReturn(store);
		given(userService.findUserById(userId))
			.willReturn(user);
		given(itemService.customItems(request.getOrderItems()))
			.willReturn(customItems);
		given(orderService.createOrder(null, userId, storeId, customItems))
			.willReturn(order);
		given(cardService.pay(order.getUserId(), request.getPaymentOption().getChargeCardId(), order.getTotalPrice()))
			.willReturn(cardPayResponse);
		given(paymentService.orderPay(order, chargeCardId, order.getTotalPrice()))
			.willThrow(PaymentException.class);

		//when
		assertThrows(PaymentException.class, () ->
			orderFacadeService.createOrder(userId, request));

		//then
		verify(storeService).findById(request.getStoreId());
		verify(userService).findUserById(userId);
		verify(paymentService).orderPay(order, chargeCardId, order.getTotalPrice());
		verify(itemService).customItems(request.getOrderItems());
		verify(cardService).pay(order.getUserId(), request.getPaymentOption().getChargeCardId(), order.getTotalPrice());
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
		OrderCreateRequest request = new OrderCreateRequest(storeId, orderItemRequests, paymentOption);

		given(storeService.findById(request.getStoreId()))
			.willThrow(EntityNotFoundException.class);

		//when
		assertThrows(EntityNotFoundException.class, () ->
			orderFacadeService.createOrder(userId, request));

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

		OrderCreateRequest request = new OrderCreateRequest(storeId, orderItemRequests, paymentOption);

		given(storeService.findById(request.getStoreId())).willReturn(store);
		given(userService.findUserById(userId)).willThrow(EntityNotFoundException.class);

		//when
		assertThrows(EntityNotFoundException.class, () ->
			orderFacadeService.createOrder(userId, request));

		//then
		verify(storeService).findById(request.getStoreId());
		verify(userService).findUserById(userId);
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

		OrderCreateRequest request = new OrderCreateRequest(storeId, orderItemRequests, paymentOption);

		PaymentResult paymentResult = new PaymentResult("paymentID");

		CardPayResponse cardPayResponse = new CardPayResponse(request.getPaymentOption().getChargeCardId(),
			order.getTotalPrice());

		Item icedAmericano = createIcedAmericano();

		CustomOption customOption = createCustomOption(option);

		List<CustomItem> customItems = List.of(new CustomItem(icedAmericano, customOption, 1));

		given(itemService.customItems(request.getOrderItems()))
			.willReturn(customItems);
		given(storeService.findById(request.getStoreId()))
			.willReturn(store);
		given(orderService.createOrder(coupon, userId, storeId, customItems))
			.willReturn(order);
		given(userService.findUserById(userId))
			.willReturn(user);
		given(couponService.getCouponByCouponId(couponId))
			.willReturn(coupon);
		given(cardService.pay(order.getUserId(), request.getPaymentOption().getChargeCardId(), order.getTotalPrice()))
			.willReturn(cardPayResponse);
		given(paymentService.orderPay(order, chargeCardId, order.getTotalPrice()))
			.willReturn(paymentResult);

		//when
		OrderCreateResponse orderCreateResponse = orderFacadeService.createOrder(userId, request);

		//then
		assertThat(orderCreateResponse.getOrderId()).isEqualTo(orderId);

		verify(storeService).findById(request.getStoreId());
		verify(userService).findUserById(userId);
		verify(couponService).getCouponByCouponId(couponId);
		verify(paymentService).orderPay(order, chargeCardId, order.getTotalPrice());
		verify(itemService).customItems(request.getOrderItems());
		verify(cardService).pay(order.getUserId(), request.getPaymentOption().getChargeCardId(), order.getTotalPrice());
		verify(orderService).createOrder(coupon, userId, storeId, customItems);
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
		OrderCreateRequest request = new OrderCreateRequest(storeId, orderItemRequests, paymentOption);

		PaymentResult paymentResult = new PaymentResult("paymentID");

		CardPayResponse cardPayResponse = new CardPayResponse(request.getPaymentOption().getChargeCardId(),
			order.getTotalPrice());

		Item icedAmericano = createIcedAmericano();

		CustomOption customOption = createCustomOption(option);

		List<CustomItem> customItems = List.of(new CustomItem(icedAmericano, customOption, 1));

		given(itemService.customItems(request.getOrderItems()))
			.willReturn(customItems);
		given(storeService.findById(request.getStoreId()))
			.willReturn(store);
		given(orderService.createOrder(null, userId, storeId, customItems))
			.willReturn(order);
		given(userService.findUserById(userId))
			.willReturn(user);
		given(cardService.pay(order.getUserId(), request.getPaymentOption().getChargeCardId(), order.getTotalPrice()))
			.willReturn(cardPayResponse);
		given(paymentService.orderPay(order, chargeCardId, order.getTotalPrice()))
			.willReturn(paymentResult);

		doNothing().when(starService).increaseCount(userId);

		//when
		OrderCreateResponse response = orderFacadeService.createOrder(userId, request);

		//then
		assertThat(response.getOrderId()).isEqualTo(orderId);

		assertThat(response.getPaymentId()).isEqualTo("paymentID");

		verify(storeService).findById(request.getStoreId());
		verify(userService).findUserById(userId);
		verify(paymentService).orderPay(order, chargeCardId, order.getTotalPrice());
		verify(itemService).customItems(request.getOrderItems());
		verify(orderService).createOrder(null, userId, storeId, customItems);
		verify(starService).increaseCount(userId);
		verify(cardService).pay(order.getUserId(), request.getPaymentOption().getChargeCardId(), order.getTotalPrice());
	}

	@DisplayName("주문 승인 실패 - 올바른 주문 아이디가 아니다.")
	@Test
	void acceptOrder_OrderNotFound_fail() {
		//given
		String orderId = "orderId";
		Long adminUserId = 10L;
		given(orderService.findById(orderId))
			.willThrow(EntityNotFoundException.class);

		//when
		assertThrows(EntityNotFoundException.class, () ->
			orderFacadeService.acceptOrder(orderId, adminUserId));
		//then
		verify(orderService).findById(orderId);
	}

	@DisplayName("주문 승인 실패 - 해당 매장의 관리자가 아니다. ")
	@Test
	void acceptOrder_notHasStore_fail() {
		//given
		String orderId = "orderId";
		Long adminUserId = 10L;

		Order order = OrderObjectProvider.createOrder();
		ReflectionTestUtils.setField(order, "id", orderId);

		given(orderService.findById(orderId))
			.willReturn(order);

		given(userService.hasStore(adminUserId, order.getStoreId()))
			.willThrow(AuthorityNotFoundException.class);

		//when
		assertThrows(AuthorityNotFoundException.class,
			() -> orderFacadeService.acceptOrder(orderId, adminUserId));

		//then
		verify(orderService).findById(orderId);
		verify(userService).hasStore(adminUserId, order.getStoreId());
	}

	@DisplayName("주문 승인 실패 - 주문의 상태가 결제완료가 아니다 ")
	@Test
	void acceptOrder_illegalOrderStatus_fail() {
		//given
		String orderId = "orderId";
		Long adminUserId = 10L;

		Order order = OrderObjectProvider.createOrder();
		ReflectionTestUtils.setField(order, "id", orderId);
		ReflectionTestUtils.setField(order, "orderStatus", OrderStatus.PREPARING);
		given(orderService.findById(orderId))
			.willReturn(order);
		given(userService.hasStore(adminUserId, order.getStoreId()))
			.willReturn(true);
		//when
		assertThrows(AlreadyProgressOrderException.class,
			() -> orderFacadeService.acceptOrder(orderId, adminUserId));

		//then
		verify(orderService).findById(orderId);
		verify(userService).hasStore(adminUserId, order.getStoreId());
	}

	@DisplayName("주문 승인 - 주문이 정상 승인된다.")
	@Test
	void acceptOrder_success() {
		//given
		long adminUserId = 1111L;
		long userId = 23423L;

		String storeId = "storeId";
		StarExchangeResponse starExchangeResponse = new StarExchangeResponse(userId, false);
		Order order = createOrder(null, userId, storeId);

		given(orderService.findById(order.getId()))
			.willReturn(order);
		given(userService.hasStore(adminUserId, order.getStoreId()))
			.willReturn(true);
		given(starService.exchangeCoupon(userId)).willReturn(starExchangeResponse);

		//when
		assertDoesNotThrow(() -> orderFacadeService.acceptOrder(order.getId(), adminUserId));

		//then
		verify(orderService).findById(order.getId());
		verify(userService).hasStore(adminUserId, storeId);

		assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.PREPARING);
		verify(starService).exchangeCoupon(userId);
	}

	@DisplayName("주문 거절 - 주문이 정상 거절된다.")
	@Test
	void rejectOrder_success() {
		//given

		//when

		//then
	}

	@DisplayName("주문 거절 실패 - 올바른 주문 아이디가 아니다.")
	@Test
	void rejectOrder_OrderNotFound_fail() {
		//given
		String orderId = "orderId";
		Long adminUserId = 10L;
		given(orderService.findById(orderId))
			.willThrow(EntityNotFoundException.class);

		//when
		assertThrows(EntityNotFoundException.class, () ->
			orderFacadeService.rejectOrder(orderId, adminUserId));
		//then
		verify(orderService).findById(orderId);
	}

	@DisplayName("주문 거절 실패 - 해당 매장의 관리자가 아니다. ")
	@Test
	void rejectOrder_notHasStore_fail() {
		//given
		String orderId = "orderId";
		Long adminUserId = 10L;
		String storeId = "storeId";

		Order order = OrderObjectProvider.createOrder();
		ReflectionTestUtils.setField(order, "id", orderId);

		given(orderService.findById(orderId))
			.willReturn(order);

		given(userService.hasStore(adminUserId, order.getStoreId()))
			.willThrow(AuthorityNotFoundException.class);

		//when
		assertThrows(AuthorityNotFoundException.class,
			() -> orderFacadeService.rejectOrder(orderId, adminUserId));

		//then
		verify(orderService).findById(orderId);
		verify(userService).hasStore(adminUserId, order.getStoreId());
	}

	@DisplayName("주문 거절 실패 - 주문의 상태가 결제완료가 아니다 ")
	@Test
	void rejectOrder_illegalOrderStatus_fail() {
		//given
		String orderId = "orderId";
		Long adminUserId = 10L;

		Order order = OrderObjectProvider.createOrder();
		ReflectionTestUtils.setField(order, "id", orderId);
		ReflectionTestUtils.setField(order, "orderStatus", OrderStatus.PREPARING);
		given(orderService.findById(orderId))
			.willReturn(order);
		given(userService.hasStore(adminUserId, order.getStoreId()))
			.willReturn(true);
		//when
		assertThrows(AlreadyProgressOrderException.class,
			() -> orderFacadeService.rejectOrder(orderId, adminUserId));

		//then
		verify(orderService).findById(orderId);
		verify(userService).hasStore(adminUserId, order.getStoreId());
	}

}