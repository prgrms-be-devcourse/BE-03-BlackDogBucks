package com.prgrms.bdbks.domain.order.dto;

import java.util.Arrays;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrdersBy {

	ORDER_ID("id"),
	CREATED_AT("createdAt");

	private final String fieldName;

	@JsonCreator
	public static OrdersBy ordersBy(String by) {
		return Arrays.stream(values())
			.filter(it -> Objects.equals(it.getFieldName().toUpperCase(), by.toUpperCase()))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("bind Error orders By"));
	}

}
