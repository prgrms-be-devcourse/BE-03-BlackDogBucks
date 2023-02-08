package com.prgrms.bdbks.domain.order.entity;

import static com.google.common.base.Preconditions.*;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.prgrms.bdbks.common.domain.AbstractTimeColumn;
import com.prgrms.bdbks.domain.coupon.entity.Coupon;
import com.prgrms.bdbks.domain.order.exception.AlreadyProgressOrderException;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "orders", indexes = @Index(name = "user_id_index", columnList = "user_id"))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends AbstractTimeColumn {

	@Id
	@Column(name = "id")
	@GeneratedValue(generator = "order_id_generator")
	@GenericGenerator(name = "order_id_generator", strategy = "com.prgrms.bdbks.domain.order.repository.OrderIdGenerator")
	private String id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "coupon_id")
	private Coupon coupon;

	@Column(name = "user_id", nullable = false)
	private Long userId;

	@Column(name = "store_id", nullable = false)
	private String storeId;

	@Column(name = "total_price", nullable = false)
	private Integer totalPrice = 0;

	@Column(name = "order_status", nullable = false, columnDefinition = "varchar(255)")
	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus;

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<OrderItem> orderItems = new ArrayList<>();

	@Builder
	protected Order(Coupon coupon, Long userId, String storeId, OrderStatus orderStatus) {
		checkNotNull(userId, "userId 는 null 일 수 없습니다.");
		checkNotNull(storeId, "storeId 는 null 일 수 없습니다.");
		checkNotNull(orderStatus, "orderStatus는 null 일 수 없습니다.");
		this.coupon = coupon;
		this.userId = userId;
		this.storeId = storeId;
		this.orderStatus = orderStatus;
	}

	public static Order create(Coupon coupon, Long userId, String storeId) {

		return Order.builder()
			.userId(userId)
			.coupon(coupon)
			.storeId(storeId)
			.orderStatus(OrderStatus.PAYMENT_COMPLETE)
			.build();
	}

	private void validateTotalPrice(long totalPrice) {
		checkArgument(totalPrice >= 0, "totalPrice 는 0보다 작을 수 없습니다.");
	}

	public void addOrderItem(OrderItem orderItem) {
		checkNotNull(orderItem, "orderItem 은 null 일 수 없습니다.");

		if (!this.orderItems.contains(orderItem)) {
			this.orderItems.add(orderItem);
			calculateTotalPrice();
		}
	}

	private void calculateTotalPrice() {
		int sumPrice = this.orderItems.stream()
			.mapToInt(OrderItem::getTotalPrice)
			.sum();

		if (this.coupon != null) {
			this.totalPrice = Math.max(sumPrice - coupon.getPrice(), 0);
		} else {
			this.totalPrice = sumPrice;
		}

		validateTotalPrice(this.totalPrice);
	}

	public int getTotalQuantity() {
		return orderItems.stream().mapToInt(OrderItem::getQuantity).sum();
	}

	public void accept() {
		if (orderStatus != OrderStatus.PAYMENT_COMPLETE) {
			throw new AlreadyProgressOrderException("이미 진행중인 주문입니다.");
		}

		this.orderStatus = OrderStatus.PREPARING;
	}

	public void reject() {
		if (orderStatus != OrderStatus.PAYMENT_COMPLETE) {
			throw new AlreadyProgressOrderException("이미 진행중인 주문입니다.");
		}

		this.orderStatus = OrderStatus.STORE_CANCEL;
	}

	public void useCoupon() {
		if (this.coupon != null) {
			this.coupon.use();
		}
	}
}
