package com.prgrms.bdbks.domain.order.service;

import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.bdbks.common.dto.SliceResponse;
import com.prgrms.bdbks.domain.card.service.CardService;
import com.prgrms.bdbks.domain.coupon.entity.Coupon;
import com.prgrms.bdbks.domain.coupon.service.CouponService;
import com.prgrms.bdbks.domain.item.dto.CustomItem;
import com.prgrms.bdbks.domain.item.service.ItemService;
import com.prgrms.bdbks.domain.order.converter.OrderMapper;
import com.prgrms.bdbks.domain.order.dto.OrderByStoreResponse;
import com.prgrms.bdbks.domain.order.dto.OrderCreateRequest;
import com.prgrms.bdbks.domain.order.dto.OrderCreateResponse;
import com.prgrms.bdbks.domain.order.dto.OrderDetailResponse;
import com.prgrms.bdbks.domain.order.entity.Order;
import com.prgrms.bdbks.domain.order.entity.OrderStatus;
import com.prgrms.bdbks.domain.payment.model.PaymentResult;
import com.prgrms.bdbks.domain.payment.service.PaymentService;
import com.prgrms.bdbks.domain.star.dto.StarExchangeResponse;
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

	private final StarService starService;

	private final CouponService couponService;

	private final CardService cardService;

	private final PaymentService paymentService;

	@Transactional
	public OrderCreateResponse createOrder(Long orderUserId, OrderCreateRequest request) {
		storeService.findById(request.getStoreId());
		userService.findUserById(orderUserId);

		Coupon coupon = findCouponIfExists(request.getPaymentOption().getCouponId());

		List<CustomItem> customItems = itemService.customItems(request.getOrderItems());

		Order order = orderService.createOrder(coupon, orderUserId, request.getStoreId(),
			customItems);

		cardService.pay(order.getUserId(), request.getPaymentOption().getChargeCardId(),
			order.getTotalPrice());

		order.useCoupon();

		PaymentResult paymentResult = paymentService.orderPay(order, request.getPaymentOption().getChargeCardId(),
			order.getTotalPrice());

		increaseStar(orderUserId, coupon);

		return new OrderCreateResponse(order.getId(), paymentResult.getPaymentId());
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

	@Transactional(readOnly = true)
	public SliceResponse<OrderByStoreResponse> findAllStoreOrdersBy(Long userId, OrderStatus orderStatus,
		String cursorOrderId, Pageable pageable) {

		Store store = storeService.findByUserId(userId);

		return orderService.findAllStoreOrdersBy(store.getId(), orderStatus, cursorOrderId, pageable);
	}

	@Transactional
	public void acceptOrder(String orderId, Long adminUserId) {
		Order order = orderService.findById(orderId);
		userService.hasStore(adminUserId, order.getStoreId());

		order.accept();

		StarExchangeResponse starExchangeResponse = starService.exchangeCoupon(order.getUserId());

		couponService.createByStar(starExchangeResponse.getUserId(), starExchangeResponse.isCanExchange());
	}

	@Transactional
	public void rejectOrder(String orderId, Long adminUserId) {
		Order order = orderService.findById(orderId);
		userService.hasStore(adminUserId, order.getStoreId());

		order.reject();

		if (Objects.nonNull(order.getCoupon())) {
			couponService.refundCoupon(order.getCoupon().getId());
		}

		paymentService.orderPayRefund(orderId);

		starService.cancel(order.getUserId());
	}

	@Nullable
	private Coupon findCouponIfExists(Long couponId) {
		if (Objects.nonNull(couponId)) {
			return couponService.getCouponByCouponId(couponId);
		}
		return null;
	}

	private void increaseStar(long userId, Coupon coupon) {
		if (Objects.isNull(coupon)) {
			starService.increaseCount(userId);
		}
	}

}
