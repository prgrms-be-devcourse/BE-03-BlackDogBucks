package com.prgrms.bdbks.domain.order.api;

import static com.prgrms.bdbks.domain.testutil.ItemObjectProvider.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.bdbks.domain.item.entity.Item;
import com.prgrms.bdbks.domain.item.entity.ItemCategory;
import com.prgrms.bdbks.domain.item.entity.OptionPrice;
import com.prgrms.bdbks.domain.item.repository.ItemCategoryRepository;
import com.prgrms.bdbks.domain.item.repository.ItemRepository;
import com.prgrms.bdbks.domain.order.entity.CustomOption;
import com.prgrms.bdbks.domain.order.entity.Order;
import com.prgrms.bdbks.domain.order.entity.OrderItem;
import com.prgrms.bdbks.domain.order.repository.OrderRepository;
import com.prgrms.bdbks.domain.store.entity.Store;
import com.prgrms.bdbks.domain.store.service.StoreService;
import com.prgrms.bdbks.domain.testutil.OrderObjectProvider;
import com.prgrms.bdbks.domain.testutil.StoreObjectProvider;
import com.prgrms.bdbks.domain.testutil.UserObjectProvider;
import com.prgrms.bdbks.domain.user.entity.User;
import com.prgrms.bdbks.domain.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Transactional
@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
class OrderControllerTest {

	private static final String BASE_REQUEST_URI = "/api/v1/orders";

	private final MockMvc mockMvc;

	@MockBean
	private StoreService storeService;

	@MockBean
	private UserService userService;

	private final OrderRepository orderRepository;

	private final ItemRepository itemRepository;

	private final ItemCategoryRepository itemCategoryRepository;

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

		Order order = OrderObjectProvider.createOrder();

		CustomOption customOption = OrderObjectProvider.createCustomOption();
		OrderItem orderItem = OrderItem.create(order, icedAmericano, customOption, 2,
			new OptionPrice());

		orderRepository.save(order);

		String storeId = order.getStoreId();
		User user = UserObjectProvider.createUser(order.getUserId());

		given(userService.findUserById(order.getUserId()))
			.willReturn(user);

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

}