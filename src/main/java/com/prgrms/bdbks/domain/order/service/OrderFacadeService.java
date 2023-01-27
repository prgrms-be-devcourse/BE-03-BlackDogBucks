package com.prgrms.bdbks.domain.order.service;

import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;

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
import com.prgrms.bdbks.domain.payment.dto.OrderPayment;
import com.prgrms.bdbks.domain.payment.service.PaymentFacadeService;
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

	private final PaymentFacadeService paymentService;

	private final StarService starService;

	private final CouponService couponService;

	@Transactional
	public String createOrder(OrderCreateRequest request) {

		storeService.findById(request.getStoreId());
		userService.findUserById(request.getUserId());

		Coupon coupon = findCoupon(request.getPaymentOption().getCouponId());

		List<CustomItem> customItems = itemService.customItems(request.getOrderItems());
		Order order = orderService.createOrder(coupon, request.getUserId(), request.getStoreId(),
			customItems);

		paymentService.orderPay(new OrderPayment(order, request.getPaymentOption().getChargeCardId(),
			request.getPaymentOption().getPaymentType()));

		updateStar(request.getUserId(), coupon, order.getTotalQuantity());

		return order.getId();
	}

	@Nullable
	private Coupon findCoupon(Long couponId) {
		if (Objects.nonNull(couponId)) {
			return couponService.getCouponByCouponId(couponId);
		}
		return null;
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
