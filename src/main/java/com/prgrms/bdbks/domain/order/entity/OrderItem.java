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

import com.prgrms.bdbks.domain.item.entity.Item;

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
	@OneToOne
	@JoinColumn(name = "custom_option_id")
	private CustomOption customOption;

	@NotNull
	@Column(name = "item_count", nullable = false)
	private int count = 1;

	@Builder
	public OrderItem(Order order, Item item, CustomOption customOption, int count) {
		checkArgument(count >= 1, "item 의 count 는 1개 이상이여야 합니다.");
		checkNotNull(order, "order 는 null 일 수 없습니다.");
		checkNotNull(item, "item 은 null 일 수 없습니다.");
		checkNotNull(customOption, "customOption 은 null 일 수 없습니다.");

		this.order = order;
		this.item = item;
		this.customOption = customOption;
		this.count = count;
	}

}
