package com.prgrms.bdbks.domain.order.service;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.bdbks.domain.coupon.entity.Coupon;
import com.prgrms.bdbks.domain.coupon.service.CouponService;
import com.prgrms.bdbks.domain.item.dto.CustomItem;
import com.prgrms.bdbks.domain.item.service.ItemService;
import com.prgrms.bdbks.domain.order.converter.OrderMapper;
import com.prgrms.bdbks.domain.order.dto.OrderCreateRequest;
import com.prgrms.bdbks.domain.order.dto.OrderDetailResponse;
import com.prgrms.bdbks.domain.order.entity.Order;
import com.prgrms.bdbks.domain.payment.service.PaymentService;
import com.prgrms.bdbks.domain.star.service.StarService;
import com.prgrms.bdbks.domain.store.entity.Store;
import com.prgrms.bdbks.domain.store.service.StoreService;
import com.prgrms.bdbks.domain.user.entity.User;
import com.prgrms.bdbks.domain.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderFacadeService {

	private final OrderService orderService;

	private final StoreService storeService;

	private final UserService userService;

	private final ItemService itemService;

	private final OrderMapper orderMapper;

	private final PaymentService paymentService;

	private final StarService starService;

	private final CouponService couponService;

	@Transactional
	public String createOrder(OrderCreateRequest request) {

		storeService.findById(request.getStoreId()); // wrapping 된 Object? <- exists
		userService.findUserById(request.getUserId());

		Coupon coupon = null;

		if (Objects.nonNull(request.getPaymentOption().getCouponId())) {
			// 쿠폰이 사용 가능 여부 체크
			coupon = couponService.getCouponByCouponId(request.getPaymentOption().getCouponId());
		}

		List<CustomItem> customItems = itemService.customItems(request.getOrderItems());
		Order order = orderService.createOrder(coupon, request.getUserId(), request.getStoreId(),
			customItems); // < couponRepository go to

		// 1. 결제로 보내 -> userId, 결제 타입, 쿠폰,
		// 2-1. chargeCard인 경우 chargeCard 가격 감소 payment 기록 save

		// 2-2. creditCard인 경우 기능 save
		// 3-1. 쿠폰 사용했을 경우 -> 쿠폰 상태 사용함으로 변경 - 결제 시 넘겨기 주

		// 3-2. 쿠폰 사용안했을 경우 -> 별 적립
		updateStar(request.getUserId(), coupon, order.getTotalQuantity());

		return order.getId();
	}

	private void updateStar(long userId, Coupon coupon, int totalOrderCount) {
		if (!Objects.isNull(coupon)) {
			starService.updateCount(userId, totalOrderCount);
		}
	}

	@Transactional(readOnly = true)
	public OrderDetailResponse findOrderById(String orderId) {
		Order findOrder = orderService.findByIdWithOrderItemsAndCustomOption(orderId);

		Store store = storeService.findById(findOrder.getStoreId());
		User user = userService.findUserById(findOrder.getUserId());

		List<OrderDetailResponse.OrderItemResponse> orderItemResponses = orderMapper.entityToOrderItemResponses(
			findOrder.getOrderItems());

		return orderMapper.entityToOrderDetailResponse(findOrder, orderItemResponses,
			store.getName(), user.getNickname());
	}

}
