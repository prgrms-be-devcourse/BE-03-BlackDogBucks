package com.prgrms.bdbks.domain.order.dto;

import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.prgrms.bdbks.domain.item.entity.BeverageOption;
import com.prgrms.bdbks.domain.payment.entity.PaymentType;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class OrderCreateRequest {

	@NotNull(message = "사용자 id가 입력되지않았습니다.")
	private Long userId;

	@NotBlank(message = "매장이 선택되지 않았습니다.")
	private String storeId;

	@NotNull(message = "주문 item이 선택되지않았습니다.")
	@Size(min = 1, message = "주문할 아이템은 최소 {min} 다.니합야 여개")
	private List<Item> orderItems;

	@NotNull(message = "결제 수단이 선택되지 않았습니다.")
	private PaymentOption paymentOption;

	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	@AllArgsConstructor
	public static class PaymentOption {

		@NotNull
		private PaymentType paymentType;

		private Long couponId;

		private String chargeCardId;
	}

	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	@AllArgsConstructor
	public static class Item {

		@NotNull
		private Long itemId;

		@Min(1)
		@NotNull
		private Integer quantity;

		@Min(0)
		@NotNull
		private int price;

		@NotNull
		private OrderCreateRequest.Item.Option option;

		@Getter
		@AllArgsConstructor
		@NoArgsConstructor(access = AccessLevel.PROTECTED)
		public static class Option {

			private Integer espressoShotCount;

			private Integer vanillaSyrupCount;

			private Integer classicSyrupCount;

			private Integer hazelnutSyrupCount;

			private BeverageOption.Milk milkType;

			private BeverageOption.Coffee espressoType;

			private BeverageOption.MilkAmount milkAmount;

			@NotNull(message = "컵 사이즈가 선택되지 않았습니다.")
			private BeverageOption.Size cupSize;

			@NotNull(message = "컵 타입이 선택되지 않았습니다.")
			private BeverageOption.CupType cupType;

		}

	}

}
