package com.prgrms.bdbks.domain.order.converter;

import java.util.List;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import com.prgrms.bdbks.domain.item.entity.BeverageOption;
import com.prgrms.bdbks.domain.order.dto.OrderDetailResponse;
import com.prgrms.bdbks.domain.order.entity.Order;
import com.prgrms.bdbks.domain.order.entity.OrderItem;
import com.prgrms.bdbks.domain.order.entity.OrderStatus;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper {

	@IterableMapping(qualifiedByName = "entityToOrderItemResponse")
	List<OrderDetailResponse.OrderItemResponse> entityToOrderItemResponses(List<OrderItem> orderItems);

	@Mapping(source = "order.id", target = "orderId")
	@Mapping(source = "order.orderItems", target = "orderCount", qualifiedByName = "sizeToOrderItem")
	@Mapping(source = "order.orderStatus", target = "orderStatus", qualifiedByName = "korStatus")
	OrderDetailResponse entityToOrderDetailResponse(Order order, List<OrderDetailResponse.OrderItemResponse> orderItems,
		String storeName, String nickname);

	@Named("sizeToOrderItem")
	default int sizeToOrderItem(List<OrderItem> orderItems) {
		return orderItems.size();
	}

	@Named("korStatus")
	default String korStatus(OrderStatus orderStatus) {
		return orderStatus.getKorStatus();
	}

	@Named("entityToOrderItemResponse")
	@Mapping(source = "item.name", target = "itemName")
	@Mapping(source = "customOption.cupSize", target = "cupSize")
	@Mapping(source = "customOption.cupType", target = "cupType", qualifiedByName = "korName")
	@Mapping(source = "item.image", target = "image")
	OrderDetailResponse.OrderItemResponse entityToOrderItemResponse(OrderItem orderItem);

	@Named("korName")
	default String korCupType(BeverageOption.CupType cupType) {
		return cupType.getKorName();
	}

}