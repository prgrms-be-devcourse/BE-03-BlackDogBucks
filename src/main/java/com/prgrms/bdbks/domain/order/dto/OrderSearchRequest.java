package com.prgrms.bdbks.domain.order.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.domain.Sort;

import com.prgrms.bdbks.domain.order.entity.OrderStatus;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class OrderSearchRequest {

	@NotNull(message = "userId는 null 이여서는 안됩니다 ")
	private Long userId;

	@NotNull(message = "orderStatus는 null 이여서는 안됩니다.")
	private OrderStatus orderStatus;

	@Size(min = 0, max = 1000, message = "사이즈는 {min} 에서 {max} 1000 사이 여야합니다. default 10")
	private Integer pageSize = 10;

	private String cursorOrderId;

	@NotNull(message = "by는 null 이여서는 안됩니다.")
	private OrdersBy by;

	@NotNull(message = "direction은 null 이여서는 안됩니다. ")
	private Sort.Direction direction = Sort.Direction.ASC;

}
