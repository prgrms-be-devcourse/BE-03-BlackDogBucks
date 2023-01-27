package com.prgrms.bdbks.domain.order.entity;

import static com.google.common.base.Preconditions.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.prgrms.bdbks.domain.item.entity.DefaultOption;
import com.prgrms.bdbks.domain.item.entity.Item;
import com.prgrms.bdbks.domain.item.entity.OptionPrice;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "order_items")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id", nullable = false)
	private Order order;

	@NotNull
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id")
	private Item item;

	@NotNull
	@Column(name = "price", nullable = false)
	private Integer price;

	@NotNull
	@OneToOne
	@JoinColumn(name = "custom_option_id")
	private CustomOption customOption;

	@NotNull
	@Column(name = "quantity", nullable = false)
	private Integer quantity = 1;

	@Builder
	public OrderItem(Order order, Item item, CustomOption customOption, Integer quantity, Integer price) {
		checkNotNull(order, "order 는 null 일 수 없습니다.");
		checkNotNull(item, "item 은 null 일 수 없습니다.");
		checkNotNull(customOption, "customOption 은 null 일 수 없습니다.");
		checkNotNull(price, "price는 null 일 수 없습니다.");
		checkArgument(quantity >= 1, "item quantity 는 1개 이상이여야 합니다.");
		checkArgument(price >= 0, "price는 음수일 수 없습니다.");
		this.item = item;
		this.customOption = customOption;
		this.quantity = quantity;
		this.price = price;
		addOrder(order);
	}

	public int getTotalPrice() {
		return price * quantity;
	}

	public static OrderItem create(Order order, Item item, CustomOption customOption,
		int quantity, OptionPrice optionPrice) {

		DefaultOption defaultOption = item.getDefaultOption();

		checkNotNull(defaultOption, "defaultOption 은 null 일 수 없습니다.");
		checkNotNull(optionPrice);
		checkNotNull(item, "item 은 null 일 수 없습니다.");

		int totalPrice = item.getPrice(); // todo 통일.

		totalPrice += customOption.calculateAddCosts(defaultOption.getEspressoShotCount(),
			defaultOption.getVanillaSyrupCount(), defaultOption.getClassicSyrupCount(),
			defaultOption.getHazelnutSyrupCount(), optionPrice);

		return OrderItem.builder()
			.customOption(customOption)
			.item(item)
			.quantity(quantity)
			.price(totalPrice)
			.order(order)
			.build();
	}

	private void addOrder(Order order) {
		if (order != null && this.order != order) {
			this.order = order;
			order.addOrderItem(this);
		}
	}

}
