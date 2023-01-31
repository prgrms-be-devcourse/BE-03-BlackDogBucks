package com.prgrms.bdbks.domain.order.entity;

import java.util.Arrays;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Getter;

@Getter
public enum OrderStatus {

	PAYMENT_COMPLETE("결제 완료"),
	PAYMENT_CANCEL("결제 취소"),
	STORE_CANCEL("매장 취소"),
	PREPARING("준비 중"),
	READY_COMPLETE("준비 완료"),
	RECEIVED("수령 완료");

	private final String korStatus;

	OrderStatus(String korStatus) {
		this.korStatus = korStatus;
	}

	@JsonCreator
	public static OrderStatus orderStatus(String status) {
		return Arrays.stream(values())
			.filter(it -> Objects.equals(it.name(), status.toUpperCase()))
			.findFirst()
			.orElseThrow();
	}

}

