package com.prgrms.bdbks.domain.order.service;

import static com.prgrms.bdbks.domain.testutil.ItemObjectProvider.*;
import static com.prgrms.bdbks.domain.testutil.OrderObjectProvider.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.prgrms.bdbks.common.exception.EntityNotFoundException;
import com.prgrms.bdbks.domain.item.entity.Item;
import com.prgrms.bdbks.domain.item.entity.OptionPrice;
import com.prgrms.bdbks.domain.item.service.ItemService;
import com.prgrms.bdbks.domain.order.converter.OrderMapper;
import com.prgrms.bdbks.domain.order.dto.OrderDetailResponse;
import com.prgrms.bdbks.domain.order.entity.CustomOption;
import com.prgrms.bdbks.domain.order.entity.Order;
import com.prgrms.bdbks.domain.order.entity.OrderItem;
import com.prgrms.bdbks.domain.store.entity.Store;
import com.prgrms.bdbks.domain.store.service.StoreService;
import com.prgrms.bdbks.domain.testutil.OrderObjectProvider;
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
		OrderItem orderItem = OrderItem.create(order, icedAmericano, customOption, 2,
			optionPrice);

		Item caffeLatte = createCaffeLatte();

		OrderItem orderItem1 = OrderItem.create(order, caffeLatte, customOption, 3,
			optionPrice);

		String storeName = "노원점";
		Store store = Store.builder()
			.id(storeId)
			.name(storeName)
			.lotNumberAddress("lotNumberAddress")
			.roadNameAddress("강남구 이고싶다.")
			.position(new GeometryFactory().createPoint(new Coordinate(36.5, 138.8)))
			.build();

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
			.hasFieldOrPropertyWithValue("storeName", storeName)
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

}