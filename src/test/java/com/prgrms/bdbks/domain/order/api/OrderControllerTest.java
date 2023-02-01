package com.prgrms.bdbks.domain.order.api;

import static com.prgrms.bdbks.domain.item.entity.BeverageOption.Coffee.*;
import static com.prgrms.bdbks.domain.item.entity.BeverageOption.CupType.*;
import static com.prgrms.bdbks.domain.item.entity.BeverageOption.Size.*;
import static com.prgrms.bdbks.domain.testutil.ItemObjectProvider.*;
import static com.prgrms.bdbks.domain.testutil.UserObjectProvider.*;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysema.commons.lang.Pair;
import com.prgrms.bdbks.domain.card.entity.Card;
import com.prgrms.bdbks.domain.card.repository.CardRepository;
import com.prgrms.bdbks.domain.coupon.entity.Coupon;
import com.prgrms.bdbks.domain.coupon.repository.CouponRepository;
import com.prgrms.bdbks.domain.item.entity.BeverageOption;
import com.prgrms.bdbks.domain.item.entity.Item;
import com.prgrms.bdbks.domain.item.entity.ItemCategory;
import com.prgrms.bdbks.domain.item.entity.OptionPrice;
import com.prgrms.bdbks.domain.item.repository.ItemCategoryRepository;
import com.prgrms.bdbks.domain.item.repository.ItemRepository;
import com.prgrms.bdbks.domain.order.dto.OrderAcceptRequest;
import com.prgrms.bdbks.domain.order.dto.OrderCreateRequest;
import com.prgrms.bdbks.domain.order.dto.OrderCreateResponse;
import com.prgrms.bdbks.domain.order.entity.CustomOption;
import com.prgrms.bdbks.domain.order.entity.Order;
import com.prgrms.bdbks.domain.order.entity.OrderItem;
import com.prgrms.bdbks.domain.order.entity.OrderStatus;
import com.prgrms.bdbks.domain.order.repository.OrderRepository;
import com.prgrms.bdbks.domain.order.service.OrderFacadeService;
import com.prgrms.bdbks.domain.payment.entity.Payment;
import com.prgrms.bdbks.domain.payment.entity.PaymentStatus;
import com.prgrms.bdbks.domain.payment.entity.PaymentType;
import com.prgrms.bdbks.domain.payment.repository.PaymentRepository;
import com.prgrms.bdbks.domain.star.entity.Star;
import com.prgrms.bdbks.domain.star.repository.StarRepository;
import com.prgrms.bdbks.domain.star.service.StarService;
import com.prgrms.bdbks.domain.store.entity.Store;
import com.prgrms.bdbks.domain.store.repository.StoreRepository;
import com.prgrms.bdbks.domain.store.service.StoreService;
import com.prgrms.bdbks.domain.testutil.CardObjectProvider;
import com.prgrms.bdbks.domain.testutil.CouponObjectProvider;
import com.prgrms.bdbks.domain.testutil.OrderObjectProvider;
import com.prgrms.bdbks.domain.testutil.StarObjectProvider;
import com.prgrms.bdbks.domain.testutil.StoreObjectProvider;
import com.prgrms.bdbks.domain.user.entity.Authority;
import com.prgrms.bdbks.domain.user.entity.User;
import com.prgrms.bdbks.domain.user.entity.UserAuthority;
import com.prgrms.bdbks.domain.user.repository.UserRepository;
import com.prgrms.bdbks.domain.user.role.Role;

import lombok.RequiredArgsConstructor;

@ActiveProfiles("test")
@Transactional
@SpringBootTest(properties = {"spring.config.location=classpath:application-test.yml"})
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
class OrderControllerTest {

	private static final String BASE_REQUEST_URI = "/api/v1/orders";

	private final MockMvc mockMvc;

	@MockBean
	private StoreService storeService;

	private final ObjectMapper objectMapper;

	private final OrderRepository orderRepository;

	private final ItemRepository itemRepository;

	private final CardRepository cardRepository;

	private final ItemCategoryRepository itemCategoryRepository;

	private final UserRepository userRepository;

	private final PaymentRepository paymentRepository;

	private final CouponRepository couponRepository;

	private final StarRepository starRepository;

	private final StarService starService;

	private final OrderFacadeService orderFacadeService;
	@Autowired
	private StoreRepository storeRepository;

	@DisplayName("조회 - 존재하지않는 id 조회시 404 notFound 를 반환한다. - 실패")
	@Test
	void findOrderById_fail() throws Exception {
		//given
		String orderId = "20230117BKDKS1234";

		// when
		mockMvc.perform(get(BASE_REQUEST_URI + "/{orderId}", orderId)
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isNotFound());
	}

	@DisplayName("조회 - 주문 조회에 성공한다.")
	@Test
	void findOrderById_success() throws Exception {
		//given
		ItemCategory itemCategory = createReserveEspressoCategory();
		itemCategoryRepository.save(itemCategory);

		Item icedAmericano = createIcedAmericano(itemCategory);// defaultOption
		itemRepository.save(icedAmericano);

		User user = createUser();
		userRepository.save(user);

		Order order = OrderObjectProvider.createOrder(user.getId());
		CustomOption customOption = OrderObjectProvider.createCustomOption();
		OrderItem.create(order, icedAmericano, customOption, 1, new OptionPrice());

		orderRepository.save(order);

		String storeId = order.getStoreId();
		Store store = StoreObjectProvider.creatStore(storeId);
		given(storeService.findById(storeId))
			.willReturn(store);

		//when
		mockMvc.perform(get(BASE_REQUEST_URI + "/{orderId}", order.getId())
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(jsonPath("$.orderId").value(order.getId()))
			.andExpect(jsonPath("$.orderCount").value(order.getOrderItems().size()))
			.andExpect(jsonPath("$.orderItems.length()").value(1))
			.andExpect(jsonPath("$.orderItems.[0].itemName").value(icedAmericano.getName()))
			.andExpect(jsonPath("$.orderItems.[0].cupSize").value(customOption.getCupSize().name()))
			.andExpect(jsonPath("$.orderItems.[0].cupType").value(customOption.getCupType().getKorName()))
			.andExpect(jsonPath("$.orderItems.[0].image").value(icedAmericano.getImage()))
			.andExpect(jsonPath("$.storeName").value(store.getName()))
			.andExpect(jsonPath("$.nickname").value(user.getNickname()))

			.andDo(document("order-detail",
					preprocessRequest(prettyPrint()),
					preprocessResponse(prettyPrint()),
					pathParameters(
						parameterWithName("orderId").description("주문 id")
					),
					responseFields(
						fieldWithPath("orderId").type(JsonFieldType.STRING).description("주문 Id"),
						fieldWithPath("orderCount").type(JsonFieldType.NUMBER).description("주문 총 수량"),
						fieldWithPath("orderStatus").type(JsonFieldType.STRING).description("주문 상태"),
						fieldWithPath("orderItems[]").type(JsonFieldType.ARRAY).description("주문 아아템 리스트"),
						fieldWithPath("orderItems[].itemName").type(JsonFieldType.STRING).description("아이템 이름"),
						fieldWithPath("orderItems[].cupSize").type(JsonFieldType.STRING).description("주문 컵사이즈"),
						fieldWithPath("orderItems[].cupType").type(JsonFieldType.STRING).description("주문 컵타입"),
						fieldWithPath("orderItems[].image").type(JsonFieldType.STRING).description("아이템 이미지"),
						fieldWithPath("storeName").type(JsonFieldType.STRING).description("매장 이름"),
						fieldWithPath("nickname").type(JsonFieldType.STRING).description("사용자 닉네임")
					)
				)
			);
	}

	@DisplayName("주문 생성 - 쿠폰이 없어도 주문을 정상 생성한다.")
	@Test
	void createOrder_Success() throws Exception {
		//given
		ItemCategory itemCategory = createReserveEspressoCategory();
		itemCategoryRepository.save(itemCategory);

		Item icedAmericano = createIcedAmericano(itemCategory); // defaultOption
		itemRepository.save(icedAmericano);

		String storeId = "storeId";
		Store store = StoreObjectProvider.creatStore(storeId);
		given(storeService.findById(storeId))
			.willReturn(store);

		User user = createUser();
		userRepository.save(user);

		Star star = StarObjectProvider.createStar(user, (short)1);
		starRepository.save(star);

		Card chargeCard = CardObjectProvider.createCard(user);
		chargeCard.chargeAmount(10000);

		String chargeCardId = cardRepository.save(chargeCard).getChargeCardId();

		OrderCreateRequest.Item.Option option = new OrderCreateRequest.Item.Option(
			1, 0, 0, 0,
			Milk.OAT, DECAFFEINATED, MilkAmount.MEDIUM, VENTI, PERSONAL);

		List<OrderCreateRequest.Item> orderItemRequests = List.of(
			new OrderCreateRequest.Item(icedAmericano.getId(), 1, 4500, option));

		OrderCreateRequest.PaymentOption paymentOption = new OrderCreateRequest.PaymentOption(PaymentType.ORDER,
			null, chargeCardId);

		OrderCreateRequest request = new OrderCreateRequest(user.getId(), storeId, orderItemRequests, paymentOption);

		given(storeService.findById(storeId))
			.willReturn(store);

		//when
		MvcResult result = mockMvc.perform(RestDocumentationRequestBuilders.post(BASE_REQUEST_URI)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))
			)
			.andExpect(status().isCreated())
			.andExpect(header().string("Location", containsString(BASE_REQUEST_URI)))
			.andDo(print())
			.andDo(document("order-create",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("userId").type(JsonFieldType.NUMBER).description("사용자 ID"),
					fieldWithPath("storeId").type(JsonFieldType.STRING).description("매장 ID"),
					fieldWithPath("orderItems[].itemId").type(JsonFieldType.NUMBER).description("아이템 ID"),
					fieldWithPath("orderItems[].quantity").type(JsonFieldType.NUMBER).description("아이템 수량"),
					fieldWithPath("orderItems[].price").type(JsonFieldType.NUMBER).description("아이템 가격"),
					fieldWithPath("orderItems[].option").type(JsonFieldType.OBJECT).description("아이템 커스텀 옵션"),

					fieldWithPath("orderItems[].option.espressoShotCount").type(JsonFieldType.NUMBER)
						.description("에스프에소 샷 카운트 수 "),
					fieldWithPath("orderItems[].option.vanillaSyrupCount").type(JsonFieldType.NUMBER)
						.description("바닐라 시럽  카운트 수"),
					fieldWithPath("orderItems[].option.classicSyrupCount").type(JsonFieldType.NUMBER)
						.description("클래식 시럽  카운트 수"),
					fieldWithPath("orderItems[].option.hazelnutSyrupCount").type(JsonFieldType.NUMBER)
						.description("헤이즐넛 시럽 카운트 수"),
					fieldWithPath("orderItems[].option.milkType").type(JsonFieldType.STRING).description("우유 타입"),
					fieldWithPath("orderItems[].option.espressoType").type(JsonFieldType.STRING)
						.description("에스프레소 타입"),
					fieldWithPath("orderItems[].option.milkAmount").type(JsonFieldType.STRING).description("우유 양"),
					fieldWithPath("orderItems[].option.cupSize").type(JsonFieldType.STRING).description("컵 사이즈"),
					fieldWithPath("orderItems[].option.cupType").type(JsonFieldType.STRING).description("컵 타입"),
					fieldWithPath("paymentOption").type(JsonFieldType.OBJECT).description("결제 옵션"),
					fieldWithPath("paymentOption.paymentType").type(JsonFieldType.STRING).description("결제 타입"),
					fieldWithPath("paymentOption.couponId").type(JsonFieldType.NULL).description("쿠폰 ID"),
					fieldWithPath("paymentOption.chargeCardId").type(JsonFieldType.STRING).description("충전 카드 ID")
				), responseFields(
					fieldWithPath("orderId").type(JsonFieldType.STRING).description("주문 ID"),
					fieldWithPath("paymentId").type(JsonFieldType.STRING).description("결제 ID")
				)
			))
			.andReturn();

		//then
		OrderCreateResponse response = objectMapper.readValue(result.getResponse().getContentAsString(),
			OrderCreateResponse.class);

		String orderId = response.getOrderId();
		String paymentId = response.getPaymentId();

		Order order = orderRepository.findById(orderId).get();

		List<OrderItem> orderItems = order.getOrderItems();
		assertThat(order)
			.hasFieldOrPropertyWithValue("coupon", null)
			.hasFieldOrPropertyWithValue("userId", user.getId())
			.hasFieldOrPropertyWithValue("storeId", storeId)
			.hasFieldOrPropertyWithValue("totalPrice", 4500)
			.hasFieldOrPropertyWithValue("orderStatus", OrderStatus.PAYMENT_COMPLETE);

		assertThat(orderItems).size().isEqualTo(1);
		OrderItem orderItem = orderItems.get(0);

		assertThat(orderItem)
			.hasFieldOrPropertyWithValue("order", order)
			.hasFieldOrPropertyWithValue("price", 4500)
			.hasFieldOrPropertyWithValue("quantity", 1)
			.hasFieldOrPropertyWithValue("item", icedAmericano);

		CustomOption customOption = orderItem.getCustomOption();

		assertThat(customOption)
			.hasFieldOrPropertyWithValue("espressoShotCount", option.getEspressoShotCount())
			.hasFieldOrPropertyWithValue("vanillaSyrupCount", option.getVanillaSyrupCount())
			.hasFieldOrPropertyWithValue("classicSyrupCount", option.getClassicSyrupCount())
			.hasFieldOrPropertyWithValue("hazelnutSyrupCount", option.getHazelnutSyrupCount())
			.hasFieldOrPropertyWithValue("milkType", option.getMilkType())
			.hasFieldOrPropertyWithValue("espressoType", option.getEspressoType())
			.hasFieldOrPropertyWithValue("milkAmount", option.getMilkAmount());

		Payment payment = paymentRepository.findById(paymentId).get();

		assertThat(payment)
			.hasFieldOrPropertyWithValue("chargeCardId", chargeCardId)
			.hasFieldOrPropertyWithValue("price", 4500)
			.hasFieldOrPropertyWithValue("paymentStatus", PaymentStatus.APPROVE)
			.hasFieldOrPropertyWithValue("order", order);

		Card findCard = cardRepository.findById(chargeCard.getChargeCardId()).get();

		assertThat(findCard)
			.hasFieldOrPropertyWithValue("amount", 10000 - payment.getPrice());

		assertThat(star)
			.hasFieldOrPropertyWithValue("count", Integer.valueOf("2"));

		verify(storeService).findById(storeId);

	}

	@DisplayName("주문 생성 - 쿠폰을 사용하면 주문금액을 쿠폰 금액만큼 감소시키고 주문 금액이 0원이여도 주문을 정상 생성한다.")
	@Test
	void createOrder_useCouponPriceThen0_Success() throws Exception {
		//given
		ItemCategory itemCategory = createReserveEspressoCategory();
		itemCategoryRepository.save(itemCategory);

		Item icedAmericano = createIcedAmericano(itemCategory); // defaultOption
		itemRepository.save(icedAmericano);

		String storeId = "storeId";
		Store store = StoreObjectProvider.creatStore(storeId);
		given(storeService.findById(storeId))
			.willReturn(store);

		User user = createUser();
		userRepository.save(user);

		Card chargeCard = CardObjectProvider.createCard(user);
		chargeCard.chargeAmount(10000);

		String chargeCardId = cardRepository.save(chargeCard).getChargeCardId();

		OrderCreateRequest.Item.Option option = new OrderCreateRequest.Item.Option(
			1, 0, 0, 0,
			Milk.OAT, DECAFFEINATED, MilkAmount.MEDIUM, VENTI, PERSONAL);

		List<OrderCreateRequest.Item> orderItemRequests = List.of(
			new OrderCreateRequest.Item(icedAmericano.getId(), 1, 4500, option));

		Coupon coupon = CouponObjectProvider.createCoupon(user.getId(), "테스트 축하 쿠폰", 10000,
			LocalDateTime.now().plusDays(7));

		couponRepository.save(coupon);

		Star star = StarObjectProvider.createStar(user, (short)1);
		starRepository.save(star);

		OrderCreateRequest.PaymentOption paymentOption = new OrderCreateRequest.PaymentOption(PaymentType.ORDER,
			coupon.getId(), chargeCardId);

		OrderCreateRequest request = new OrderCreateRequest(user.getId(), storeId, orderItemRequests, paymentOption);

		given(storeService.findById(storeId))
			.willReturn(store);

		//when
		MvcResult result = mockMvc.perform(RestDocumentationRequestBuilders.post(BASE_REQUEST_URI)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))
			)
			.andExpect(status().isCreated())
			.andExpect(header().string("Location", containsString(BASE_REQUEST_URI)))
			.andDo(print())
			.andReturn();

		//then
		OrderCreateResponse response = objectMapper.readValue(result.getResponse().getContentAsString(),
			OrderCreateResponse.class);

		String orderId = response.getOrderId();
		String paymentId = response.getPaymentId();

		Order order = orderRepository.findById(orderId).get();

		List<OrderItem> orderItems = order.getOrderItems();
		assertThat(order)
			.hasFieldOrPropertyWithValue("coupon", coupon)
			.hasFieldOrPropertyWithValue("userId", user.getId())
			.hasFieldOrPropertyWithValue("storeId", storeId)
			.hasFieldOrPropertyWithValue("totalPrice", 0)
			.hasFieldOrPropertyWithValue("orderStatus", OrderStatus.PAYMENT_COMPLETE);

		assertThat(orderItems).size().isEqualTo(1);
		OrderItem orderItem = orderItems.get(0);

		assertThat(orderItem)
			.hasFieldOrPropertyWithValue("order", order)
			.hasFieldOrPropertyWithValue("price", 4500)
			.hasFieldOrPropertyWithValue("quantity", 1)
			.hasFieldOrPropertyWithValue("item", icedAmericano);

		CustomOption customOption = orderItem.getCustomOption();

		assertThat(customOption)
			.hasFieldOrPropertyWithValue("espressoShotCount", option.getEspressoShotCount())
			.hasFieldOrPropertyWithValue("vanillaSyrupCount", option.getVanillaSyrupCount())
			.hasFieldOrPropertyWithValue("classicSyrupCount", option.getClassicSyrupCount())
			.hasFieldOrPropertyWithValue("hazelnutSyrupCount", option.getHazelnutSyrupCount())
			.hasFieldOrPropertyWithValue("milkType", option.getMilkType())
			.hasFieldOrPropertyWithValue("espressoType", option.getEspressoType())
			.hasFieldOrPropertyWithValue("milkAmount", option.getMilkAmount());

		Payment payment = paymentRepository.findById(paymentId).get();

		assertThat(payment)
			.hasFieldOrPropertyWithValue("chargeCardId", chargeCardId)
			.hasFieldOrPropertyWithValue("price", 0)
			.hasFieldOrPropertyWithValue("paymentStatus", PaymentStatus.APPROVE)
			.hasFieldOrPropertyWithValue("order", order);

		Card findCard = cardRepository.findById(chargeCard.getChargeCardId()).get();

		assertThat(findCard)
			.hasFieldOrPropertyWithValue("amount", 10000 - payment.getPrice());

		Coupon findCoupon = couponRepository.findById(order.getCoupon().getId()).get();

		assertThat(findCoupon)
			.hasFieldOrPropertyWithValue("used", true);

		assertThat(star)
			.hasFieldOrPropertyWithValue("count", 1);

		verify(storeService).findById(storeId);
	}

	@DisplayName("주문 생성 - 쿠폰을 사용하면 주문금액을 쿠폰 금액만큼 감소된 주문을 정상 생성한다.")
	@Test
	void createOrder_useCoupon_Success() throws Exception {
		//given
		ItemCategory itemCategory = createReserveEspressoCategory();
		itemCategoryRepository.save(itemCategory);

		Item icedAmericano = createIcedAmericano(itemCategory); // defaultOption
		itemRepository.save(icedAmericano);

		String storeId = "storeId";
		Store store = StoreObjectProvider.creatStore(storeId);
		given(storeService.findById(storeId))
			.willReturn(store);

		User user = createUser();
		userRepository.save(user);

		Card chargeCard = CardObjectProvider.createCard(user);
		chargeCard.chargeAmount(10000);

		String chargeCardId = cardRepository.save(chargeCard).getChargeCardId();

		OrderCreateRequest.Item.Option option = new OrderCreateRequest.Item.Option(
			1, 0, 0, 0,
			Milk.OAT, DECAFFEINATED, MilkAmount.MEDIUM, VENTI, PERSONAL);

		List<OrderCreateRequest.Item> orderItemRequests = List.of(
			new OrderCreateRequest.Item(icedAmericano.getId(), 1, 4500, option));

		Coupon coupon = CouponObjectProvider.createCoupon(user.getId(), "테스트 축하 쿠폰", 1000,
			LocalDateTime.now().plusDays(7));

		couponRepository.save(coupon);

		Star star = StarObjectProvider.createStar(user, (short)1);
		starRepository.save(star);

		OrderCreateRequest.PaymentOption paymentOption = new OrderCreateRequest.PaymentOption(PaymentType.ORDER,
			coupon.getId(), chargeCardId);

		OrderCreateRequest request = new OrderCreateRequest(user.getId(), storeId, orderItemRequests, paymentOption);

		given(storeService.findById(storeId))
			.willReturn(store);

		//when
		MvcResult result = mockMvc.perform(RestDocumentationRequestBuilders.post(BASE_REQUEST_URI)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))
			)
			.andExpect(status().isCreated())
			.andExpect(header().string("Location", containsString(BASE_REQUEST_URI)))
			.andDo(print())
			.andReturn();

		//then
		OrderCreateResponse response = objectMapper.readValue(result.getResponse().getContentAsString(),
			OrderCreateResponse.class);

		String orderId = response.getOrderId();
		String paymentId = response.getPaymentId();

		Order order = orderRepository.findById(orderId).get();

		List<OrderItem> orderItems = order.getOrderItems();
		assertThat(order)
			.hasFieldOrPropertyWithValue("coupon", coupon)
			.hasFieldOrPropertyWithValue("userId", user.getId())
			.hasFieldOrPropertyWithValue("storeId", storeId)
			.hasFieldOrPropertyWithValue("totalPrice", 3500)
			.hasFieldOrPropertyWithValue("orderStatus", OrderStatus.PAYMENT_COMPLETE);

		assertThat(orderItems).size().isEqualTo(1);
		OrderItem orderItem = orderItems.get(0);

		assertThat(orderItem)
			.hasFieldOrPropertyWithValue("order", order)
			.hasFieldOrPropertyWithValue("price", 4500)
			.hasFieldOrPropertyWithValue("quantity", 1)
			.hasFieldOrPropertyWithValue("item", icedAmericano);

		CustomOption customOption = orderItem.getCustomOption();

		assertThat(customOption)
			.hasFieldOrPropertyWithValue("espressoShotCount", option.getEspressoShotCount())
			.hasFieldOrPropertyWithValue("vanillaSyrupCount", option.getVanillaSyrupCount())
			.hasFieldOrPropertyWithValue("classicSyrupCount", option.getClassicSyrupCount())
			.hasFieldOrPropertyWithValue("hazelnutSyrupCount", option.getHazelnutSyrupCount())
			.hasFieldOrPropertyWithValue("milkType", option.getMilkType())
			.hasFieldOrPropertyWithValue("espressoType", option.getEspressoType())
			.hasFieldOrPropertyWithValue("milkAmount", option.getMilkAmount());

		Payment payment = paymentRepository.findById(paymentId).get();

		assertThat(payment)
			.hasFieldOrPropertyWithValue("chargeCardId", chargeCardId)
			.hasFieldOrPropertyWithValue("price", 3500)
			.hasFieldOrPropertyWithValue("paymentStatus", PaymentStatus.APPROVE)
			.hasFieldOrPropertyWithValue("order", order);

		Card findCard = cardRepository.findById(chargeCard.getChargeCardId()).get();

		assertThat(findCard)
			.hasFieldOrPropertyWithValue("amount", 10000 - payment.getPrice());

		Coupon findCoupon = couponRepository.findById(order.getCoupon().getId()).get();

		assertThat(findCoupon)
			.hasFieldOrPropertyWithValue("used", true);

		assertThat(star)
			.hasFieldOrPropertyWithValue("count", 1);

		verify(storeService).findById(storeId);
	}

	@DisplayName("조회 - storeId와 orderStatus에 맞는 주문 list를 커서 기반 페이지로 조회할 수 있다.")
	@Test
	void findStoreOrders_success() throws Exception {

		createOrders();

		MultiValueMap<String, String> param = new LinkedMultiValueMap<>();

		User adminUser = createUser("spdlqj@naver.com", "naver@naver.com", "01074414048");
		userRepository.save(adminUser);

		String storeId = "store1";
		given(storeService.findByUserId(adminUser.getId()))
			.willReturn(StoreObjectProvider.creatStore(storeId));

		String requestOrderStatus = OrderStatus.PAYMENT_COMPLETE.name();

		param.add("userId", adminUser.getId().toString());
		param.add("pageSize", "10");
		param.add("by", "CREATED_AT");
		param.add("direction", "DESC");
		param.add("orderStatus", requestOrderStatus);

		mockMvc.perform(get(BASE_REQUEST_URI)
				.contentType(MediaType.APPLICATION_JSON)
				.params(param)
			).andExpect(status().isOk())
			.andDo(print())
			.andExpect(jsonPath("$.hasNext").value(false))
			.andExpect(jsonPath("$.data[*].storeId").value(hasItem(storeId)))
			.andExpect(jsonPath("$.data[*].orderStatus").value(hasItem(requestOrderStatus)))

			.andDo(document("orders-by-store",
					preprocessRequest(prettyPrint()),
					preprocessResponse(prettyPrint()),
					requestParameters(
						parameterWithName("userId").description("요청 userId"),
						parameterWithName("pageSize").description("요청 페이지 크기"),
						parameterWithName("by").description("정렬 기준"),
						parameterWithName("direction").description("정렬 방법"),
						parameterWithName("orderStatus").description("주문 상태"),
						parameterWithName("cursorOrderId").description("커서 OrderId").optional()
					),
					responseFields(
						fieldWithPath("data").type(JsonFieldType.ARRAY).description("데이터 양"),
						fieldWithPath("hasNext").type(JsonFieldType.BOOLEAN).description("다음 데이터 여부"),
						fieldWithPath("size").type(JsonFieldType.NUMBER).description("데이터 수"),

						fieldWithPath("data[].orderId").type(JsonFieldType.STRING).description("주문 번호"),
						fieldWithPath("data[].storeId").type(JsonFieldType.STRING).description("매장 아이디"),
						fieldWithPath("data[].orderStatus").type(JsonFieldType.STRING).description("주문 상태"),
						fieldWithPath("data[].nickname").type(JsonFieldType.STRING).description("주문 유저 명"),
						fieldWithPath("data[].items[]").type(JsonFieldType.ARRAY).description("주문 아이템들"),
						fieldWithPath("data[].items[].[].itemName").type(JsonFieldType.STRING).description("주문 아이템들"),
						fieldWithPath("data[].items[].[].quantity").type(JsonFieldType.NUMBER).description("주문 수"),
						fieldWithPath("data[].items[].[].espressoShotCount").type(JsonFieldType.NUMBER)
							.description("에스프레소 샷 양"),
						fieldWithPath("data[].items[].[].vanillaSyrupCount").type(JsonFieldType.NUMBER)
							.description("바닐라 시럽 양"),
						fieldWithPath("data[].items[].[].classicSyrupCount").type(JsonFieldType.NUMBER)
							.description("클래식 시럽 양"),
						fieldWithPath("data[].items[].[].hazelnutSyrupCount").type(JsonFieldType.NUMBER)
							.description("헤이즐넛 시럽 양"),
						fieldWithPath("data[].items[].[].milkType").type(JsonFieldType.STRING).description("우유 타입"),
						fieldWithPath("data[].items[].[].espressoType").type(JsonFieldType.STRING)
							.description("커피 에스프레소 타입"),
						fieldWithPath("data[].items[].[].milkAmount").type(JsonFieldType.STRING).description("우유 양"),
						fieldWithPath("data[].items[].[].cupSize").type(JsonFieldType.STRING).description("컵 사이즈"),
						fieldWithPath("data[].items[].[].cupType").type(JsonFieldType.STRING).description("컵 타입")

					)
				)
			);

	}

	@DisplayName("수정 - 주문 승인 요청을 정상 반영한다. - 성공")
	@Test
	void acceptOrder_success() throws Exception {
		//given
		ItemCategory itemCategory = createReserveEspressoCategory();
		itemCategoryRepository.save(itemCategory);

		Item icedAmericano = createIcedAmericano(itemCategory);// defaultOption
		itemRepository.save(icedAmericano);

		String storeId = "storeId";
		Store store = StoreObjectProvider.creatStore(storeId);

		storeRepository.save(store);

		User user = createUser();
		Authority authority = new Authority(Role.ADMIN);
		UserAuthority userAuthority = new UserAuthority(null, authority, user, store);
		user.getUserAuthorities().add(userAuthority);
		userRepository.save(user);

		Order order = OrderObjectProvider.createOrder(null, user.getId(), storeId);
		CustomOption customOption = OrderObjectProvider.createCustomOption();
		OrderItem.create(order, icedAmericano, customOption, 1, new OptionPrice());

		orderRepository.save(order);
		starService.create(user);

		OrderAcceptRequest orderAcceptRequest = new OrderAcceptRequest(user.getId());

		mockMvc.perform(patch(BASE_REQUEST_URI + "/{orderId}/accept", order.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(orderAcceptRequest))
			)
			.andExpect(status().isOk())
			.andDo(print())
			.andDo(document("order-accept",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				pathParameters(
					parameterWithName("orderId").description("주문 Id")
				),
				requestFields(
					fieldWithPath("userId").type(JsonFieldType.NUMBER).description("요청 userId")
				)
			));

		Order findOrder = orderRepository.findById(order.getId()).get();

		assertThat(findOrder.getOrderStatus()).isEqualTo(OrderStatus.PREPARING);
	}

	@DisplayName("수정 - 주문 거절 요청을 정상 반영한다. - 성공")
	@Test
	void rejectOrder_success() throws Exception {
		//given
		ItemCategory itemCategory = createReserveEspressoCategory();
		itemCategoryRepository.save(itemCategory);

		Item icedAmericano = createIcedAmericano(itemCategory);// defaultOption
		itemRepository.save(icedAmericano);

		String storeId = "storeId";
		Store store = StoreObjectProvider.creatStore(storeId);

		storeRepository.save(store);

		User user = createUser();
		Authority authority = new Authority(Role.ADMIN);
		UserAuthority userAuthority = new UserAuthority(null, authority, user, store);
		user.getUserAuthorities().add(userAuthority);
		userRepository.save(user);

		Order order = OrderObjectProvider.createOrder(null, user.getId(), storeId);
		CustomOption customOption = OrderObjectProvider.createCustomOption();
		OrderItem.create(order, icedAmericano, customOption, 1, new OptionPrice());

		orderRepository.save(order);
		starService.create(user);

		OrderAcceptRequest orderAcceptRequest = new OrderAcceptRequest(user.getId());

		mockMvc.perform(patch(BASE_REQUEST_URI + "/{orderId}/reject", order.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(orderAcceptRequest))
			)
			.andExpect(status().isOk())
			.andDo(print())
			.andDo(document("order-reject",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				pathParameters(
					parameterWithName("orderId").description("주문 Id")
				),
				requestFields(
					fieldWithPath("userId").type(JsonFieldType.NUMBER).description("요청 userId")
				)
			));

		Order findOrder = orderRepository.findById(order.getId()).get();

		assertThat(findOrder.getOrderStatus()).isEqualTo(OrderStatus.STORE_CANCEL);
	}

	void createOrders() {

		AtomicInteger integer = new AtomicInteger();

		List<Pair> users = IntStream.range(0, 10)
			.mapToObj(i -> {

				User user = userRepository.save(
					createUser(RandomStringUtils.randomAlphabetic(10),
						RandomStringUtils.randomAlphabetic(10) + "@email.com",
						"0107441484" + integer.getAndIncrement()));

				starService.create(user);

				Card chargeCard = CardObjectProvider.createCard(user);
				chargeCard.chargeAmount(100000);
				cardRepository.save(chargeCard);

				return new Pair(user, chargeCard);
			}).collect(Collectors.toList());

		ItemCategory itemCategory = createReserveEspressoCategory();
		itemCategoryRepository.save(itemCategory);

		Item americano = createIcedAmericano(itemCategory);
		Item latte = createCaffeLatte(itemCategory);
		itemRepository.save(americano);
		itemRepository.save(latte);

		List<Store> stores = List.of(StoreObjectProvider.creatStore("store1"),
			StoreObjectProvider.creatStore("store2"),
			StoreObjectProvider.creatStore("store3"));

		stores.forEach(s -> {
			given(storeService.findById(s.getId()))
				.willReturn(s);
		});

		OrderCreateRequest.Item.Option option = new OrderCreateRequest.Item.Option(
			1, 0, 0, 0,
			BeverageOption.Milk.OAT, BeverageOption.Coffee.DECAFFEINATED, BeverageOption.MilkAmount.MEDIUM,
			BeverageOption.Size.VENTI, BeverageOption.CupType.PERSONAL);

		AtomicInteger i = new AtomicInteger();

		users.forEach(u -> {

			User user = (User)u.getFirst();
			Card card = (Card)u.getSecond();

			OrderCreateRequest.PaymentOption paymentOption = new OrderCreateRequest.PaymentOption(PaymentType.ORDER,
				null, card.getChargeCardId());
			Store store = stores.get(i.getAndIncrement() % 3);

			List<OrderCreateRequest.Item> orderItemRequests = List.of(
				new OrderCreateRequest.Item(americano.getId(), 1, 4500, option),
				new OrderCreateRequest.Item(latte.getId(), 2, 4500, option)
			);

			OrderCreateRequest request = new OrderCreateRequest(user.getId(),
				store.getId(), orderItemRequests,
				paymentOption);

			orderFacadeService.createOrder(request);
		});

	}
}
