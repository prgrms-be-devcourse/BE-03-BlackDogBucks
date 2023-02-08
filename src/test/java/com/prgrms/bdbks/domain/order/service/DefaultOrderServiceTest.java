package com.prgrms.bdbks.domain.order.service;

import static com.prgrms.bdbks.domain.testutil.ItemObjectProvider.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestConstructor;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mysema.commons.lang.Pair;
import com.prgrms.bdbks.common.dto.SliceResponse;
import com.prgrms.bdbks.config.jpa.JpaConfig;
import com.prgrms.bdbks.domain.card.entity.Card;
import com.prgrms.bdbks.domain.card.repository.CardRepository;
import com.prgrms.bdbks.domain.item.entity.BeverageOption;
import com.prgrms.bdbks.domain.item.entity.Item;
import com.prgrms.bdbks.domain.item.entity.ItemCategory;
import com.prgrms.bdbks.domain.item.repository.ItemCategoryRepository;
import com.prgrms.bdbks.domain.item.repository.ItemRepository;
import com.prgrms.bdbks.domain.order.dto.OrderByStoreResponse;
import com.prgrms.bdbks.domain.order.dto.OrderCreateRequest;
import com.prgrms.bdbks.domain.order.entity.OrderStatus;
import com.prgrms.bdbks.domain.payment.entity.PaymentType;
import com.prgrms.bdbks.domain.star.service.StarService;
import com.prgrms.bdbks.domain.store.entity.Store;
import com.prgrms.bdbks.domain.store.service.StoreService;
import com.prgrms.bdbks.domain.testutil.CardObjectProvider;
import com.prgrms.bdbks.domain.testutil.StoreObjectProvider;
import com.prgrms.bdbks.domain.testutil.UserObjectProvider;
import com.prgrms.bdbks.domain.user.entity.User;
import com.prgrms.bdbks.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Transactional
@SpringBootTest
@Import(JpaConfig.class)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
class DefaultOrderServiceTest {

	@MockBean
	private StoreService storeService;

	private final ItemRepository itemRepository;

	private final CardRepository cardRepository;

	private final ItemCategoryRepository itemCategoryRepository;

	private final UserRepository userRepository;

	private final OrderFacadeService orderFacadeService;

	private final OrderService orderService;

	@MockBean
	private StarService starService;

	@DisplayName("조회 - storeId와 orderStatus에 맞는 주문 list를 커서 기반 페이지로 조회할 수 있다. - 성공 ")
	@Test
	void findAllStoreOrdersBy_success() throws JsonProcessingException {

		createOrders();

		SliceResponse<OrderByStoreResponse> orders = orderService.findAllStoreOrdersBy("store1",
			OrderStatus.PAYMENT_COMPLETE,
			null,
			PageRequest.ofSize(10));

		List<OrderByStoreResponse> storeOrders = orders.getData();

		assertThat(storeOrders).hasSize(4);
		assertThat(storeOrders)
			.extracting(OrderByStoreResponse::getOrderStatus).contains(OrderStatus.PAYMENT_COMPLETE);

		assertThat(orders.getSize()).isEqualTo(4);
	}

	void createOrders() {

		AtomicInteger integer = new AtomicInteger();

		List<Pair> users = IntStream.range(0, 10)
			.mapToObj(i -> {

				User user = userRepository.save(
					UserObjectProvider.createUser(RandomStringUtils.randomAlphabetic(10),
						RandomStringUtils.randomAlphabetic(10) + "@email.com",
						"0107441484" + integer.getAndIncrement()));

				doNothing().when(starService).increaseCount(user.getId());

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
				null, card.getId());
			Store store = stores.get(i.getAndIncrement() % 3);

			List<OrderCreateRequest.Item> orderItemRequests = List.of(
				new OrderCreateRequest.Item(americano.getId(), 1, 4500, option),
				new OrderCreateRequest.Item(latte.getId(), 2, 4500, option)
			);

			OrderCreateRequest request = new OrderCreateRequest(store.getId(),
				orderItemRequests,
				paymentOption);

			orderFacadeService.createOrder(user.getId(), request);
		});

	}

}