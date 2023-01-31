package com.prgrms.bdbks.domain.store.dto;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StorePointRequest {

	@NotNull(message = "위도는 필수로 입력 해야 합니다.")
	@DecimalMin("-90.0")
	@DecimalMax("90.0")
	private Double latitude;

	@NotNull(message = "경도는 필수로 입력 해야 합니다.")
	@DecimalMin("-180.0")
	@DecimalMax("180.0")
	private Double longitude;
}
