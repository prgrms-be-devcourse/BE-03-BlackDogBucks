package com.prgrms.bdbks.domain.store.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StoreCreateRequest {

	@NotBlank(message = "매장 번호는 필수로 입력 해야 합니다.")
	private String id;

	@NotBlank(message = "매장 이름은 필수로 입력 해야 합니다.")
	private String name;

	@NotBlank(message = "매장 지번 주소는 필수로 입력 해야 합니다.")
	private String lotNumberAddress;

	@NotBlank(message = "매장 도로명 주소는 필수로 입력 해야 합니다.")
	private String roadNameAddress;

	@NotNull(message = "위도는 필수로 입력 해야 합니다.")
	private double latitude;

	@NotNull(message = "경도는 필수로 입력 해야 합니다.")
	private double longitude;
}
